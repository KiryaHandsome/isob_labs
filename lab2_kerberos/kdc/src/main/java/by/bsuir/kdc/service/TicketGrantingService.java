package by.bsuir.kdc.service;

import by.bsuir.data.AuthBlock;
import by.bsuir.data.GrantingServiceTicket;
import by.bsuir.data.TGSRequest;
import by.bsuir.data.TGSResponse;
import by.bsuir.data.TicketGrantingTicket;
import by.bsuir.des.EncryptionUtils;
import by.bsuir.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketGrantingService {

    @Value("${tgs.duration}")
    private Long tgsDuration;
    private final KDCDatabase kdcDatabase;

    public byte[] grant(TGSRequest request) {
        byte[] tgtEncrypted = request.getTgt();
        String tgtDecrypted = EncryptionUtils.decrypt(tgtEncrypted, kdcDatabase.getSecret("kdc"));
        TicketGrantingTicket tgt = JsonUtil.fromJson(tgtDecrypted, TicketGrantingTicket.class);

        String clientTgsSessionKey = kdcDatabase.getSessionKey(tgt.getClient());
        String authBlockJson = EncryptionUtils.decrypt(request.getAuthBlock(), clientTgsSessionKey);
        AuthBlock authBlock = JsonUtil.fromJson(authBlockJson, AuthBlock.class);

        checkTgt(tgt, authBlock.client());

        String clientServiceSessionKey = generateClientServiceSessionKey(tgt.getClient(), request.getService());
        GrantingServiceTicket tgs = GrantingServiceTicket.builder()
                .clientServiceSessionKey(clientServiceSessionKey)
                .time(LocalDateTime.now())
                .validityPeriod(tgsDuration)
                .service(request.getService())
                .client(authBlock.client())
                .build();
        byte[] tgsEncrypted = EncryptionUtils.encrypt(JsonUtil.toJson(tgs).getBytes(), kdcDatabase.getSecret(request.getService()));
        TGSResponse response = new TGSResponse(tgsEncrypted, clientServiceSessionKey);

        return EncryptionUtils.encrypt(JsonUtil.toJson(response).getBytes(), clientTgsSessionKey);
    }

    private String generateClientServiceSessionKey(String client, String service) {
        return client + service + "SessionKey";
    }

    private void checkTgt(TicketGrantingTicket tgt, String client) {
        if (!tgt.getClient().equals(client)) {
            throw new RuntimeException("Client in ticket is different with auth block client");
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validTime = tgt.getTime().plusSeconds(tgt.getValidityPeriod());
        if (!validTime.isAfter(now)) {
            throw new RuntimeException("TGT has expired");
        }
    }
}
