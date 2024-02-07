package by.bsuir.service.service;

import by.bsuir.data.AuthBlock;
import by.bsuir.data.GrantingServiceTicket;
import by.bsuir.data.ServiceRequest;
import by.bsuir.data.ServiceResponse;
import by.bsuir.des.EncryptionUtils;
import by.bsuir.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${tgs-service.secret-key}")
    private String tgsServiceSecretKey;

    private final SessionManager sessionManager;

    public byte[] authenticateClient(ServiceRequest serviceRequest) {
        String tgsJson = EncryptionUtils.decrypt(Base64.decodeBase64(serviceRequest.getTgs()), tgsServiceSecretKey);
        GrantingServiceTicket ticket = JsonUtil.fromJson(tgsJson, GrantingServiceTicket.class);

        String clientServiceSessionKey = ticket.getClientServiceSessionKey();
        String authBlockJson = EncryptionUtils.decrypt(Base64.decodeBase64(serviceRequest.getAuthBlock()), clientServiceSessionKey);
        AuthBlock authBlock = JsonUtil.fromJson(authBlockJson, AuthBlock.class);

        if (!isSameClient(authBlock, ticket)) {
            throw new RuntimeException("Clients in ticket and auth block are different");
        }
        ServiceResponse response = new ServiceResponse(ticket.getTime().plusSeconds(1));
        sessionManager.addSession(authBlock.client(), clientServiceSessionKey);
        return EncryptionUtils.encrypt(JsonUtil.toJson(response).getBytes(), clientServiceSessionKey);
    }

    private boolean isSameClient(AuthBlock authBlock, GrantingServiceTicket ticket) {
        return authBlock.client().equals(ticket.getClient());
    }
}
