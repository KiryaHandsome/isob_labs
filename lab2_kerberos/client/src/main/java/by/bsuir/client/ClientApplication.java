package by.bsuir.client;

import by.bsuir.data.AuthBlock;
import by.bsuir.data.AuthRequest;
import by.bsuir.data.AuthResponse;
import by.bsuir.data.ServiceRequest;
import by.bsuir.data.ServiceResponse;
import by.bsuir.data.TGSRequest;
import by.bsuir.data.TGSResponse;
import by.bsuir.des.EncryptionUtils;
import by.bsuir.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@SpringBootApplication
public class ClientApplication {

    private static final Logger log = LoggerFactory.getLogger(ClientApplication.class);
    private final String clientName = "client";
    private final String clientSecretKey = "pswd";
    private final String serviceName = "service";
    private final String authUrl = "http://localhost:8080/authenticate";
    private final String serviceTicketUrl = "http://localhost:8080/grant-ticket";
    private final String serviceUrl = "http://localhost:8082/client-auth";
    private final String checkServiceUrl = "http://localhost:8082/check";
    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return cmd -> {
            AuthResponse authResponse = getTGT();
            TGSResponse tgsResponse = getTGS(authResponse);

            AuthBlock authBlock = buildAuthBlock();
            ServiceResponse serviceResponse = getServiceConsent(tgsResponse, authBlock);
            checkTime(serviceResponse, authBlock);

            performCheckRequest(tgsResponse.getClientServiceSessionKey());
        };
    }

    private AuthResponse getTGT() {
        var request = new AuthRequest(clientName);
        ResponseEntity<byte[]> response = restTemplate.postForEntity(authUrl, request, byte[].class);
        AuthResponse authResponse = decryptAndParseFromJson(response.getBody(), clientSecretKey, AuthResponse.class);
        log.info("Auth response: Tgt={}, sessionKey={}", new String(authResponse.tgt()), authResponse.sessionKey());
        return authResponse;
    }

    private TGSResponse getTGS(AuthResponse authResponse) {
        AuthBlock authBlock = buildAuthBlock();
        TGSRequest tgsRequest = buildTgsRequest(authResponse.tgt(), authBlock, authResponse.sessionKey());
        ResponseEntity<byte[]> tgsResponseEntity = restTemplate.postForEntity(serviceTicketUrl, tgsRequest, byte[].class);
        TGSResponse response = decryptAndParseFromJson(tgsResponseEntity.getBody(), authResponse.sessionKey(), TGSResponse.class);
        log.info("Tgs response: Tgs={}, clientServiceSK={}", new String(response.getTgs()), response.getClientServiceSessionKey());
        return response;
    }

    private ServiceResponse getServiceConsent(TGSResponse tgsResponse, AuthBlock authBlock) {
        String clientServiceSessionKey = tgsResponse.getClientServiceSessionKey();
        String authBlockJson = JsonUtil.toJson(authBlock);
        ServiceRequest serviceRequest = new ServiceRequest(tgsResponse.getTgs(), EncryptionUtils.encrypt(authBlockJson.getBytes(), clientServiceSessionKey));
        ResponseEntity<ServiceResponse> serviceResponseEntity = restTemplate.postForEntity(serviceUrl, serviceRequest, ServiceResponse.class);
        return serviceResponseEntity.getBody();
    }

    private void checkTime(ServiceResponse serviceResponse, AuthBlock authBlock) {
        if (!serviceResponse.getTime().truncatedTo(ChronoUnit.SECONDS).isEqual(authBlock.time().plusSeconds(1).truncatedTo(ChronoUnit.SECONDS))) {
            log.error("Service time is invalid!!!");
            throw new RuntimeException();
        }
    }

    private <T> T decryptAndParseFromJson(byte[] data, String secretKey, Class<T> type) {
        String str = EncryptionUtils.decrypt(data, secretKey);
        return JsonUtil.fromJson(str, type);
    }

    private void performCheckRequest(String clientServiceSessionKey) {
        byte[] dataToSend = EncryptionUtils.encrypt("Hello from client!!!".getBytes(), clientServiceSessionKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set("client", clientName);
        HttpEntity<String> entity = new HttpEntity<>(new String(dataToSend), headers);
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                checkServiceUrl,
                HttpMethod.POST,
                entity,
                byte[].class
        );
        String responseFromService = EncryptionUtils.decrypt(responseEntity.getBody(), clientServiceSessionKey);
        log.info("Response from service: {}", responseFromService);
    }

    private AuthBlock buildAuthBlock() {
        return new AuthBlock(LocalDateTime.now(), clientName);
    }

    private TGSRequest buildTgsRequest(byte[] tgt, AuthBlock authBlock, String sessionKey) {
        return TGSRequest.builder()
                .service(serviceName)
                .tgt(tgt)
                .authBlock(EncryptionUtils.encrypt(JsonUtil.toJson(authBlock).getBytes(), sessionKey))
                .build();
    }

}
