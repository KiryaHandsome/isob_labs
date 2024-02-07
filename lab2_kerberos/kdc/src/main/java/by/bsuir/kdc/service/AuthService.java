package by.bsuir.kdc.service;

import by.bsuir.data.AuthRequest;
import by.bsuir.data.AuthResponse;
import by.bsuir.data.TicketGrantingTicket;
import by.bsuir.des.EncryptionUtils;
import by.bsuir.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${tgt.duration}")
    private Long tgtDuration;

    @Value("${tgs.id}")
    private String tgs;

    private final KDCDatabase kdcDatabase;

    public byte[] authenticate(AuthRequest request) {
        String client = request.client();
        if (!kdcDatabase.hasClient(client)) {
            throw new RuntimeException("There is no such user");
        }
        String sessionKey = generateSessionKey(client);
        TicketGrantingTicket tgt = generateTGT(request.client(), sessionKey);
        byte[] tgtEncrypted = EncryptionUtils.encrypt(JsonUtil.toJson(tgt).getBytes(), kdcDatabase.getSecret("kdc"));
        kdcDatabase.addSessionKey(client, sessionKey);
        var response = new AuthResponse(Base64.encodeBase64String(tgtEncrypted), sessionKey);
        return EncryptionUtils.encrypt(JsonUtil.toJson(response).getBytes(), kdcDatabase.getSecret(client));
    }

    private String generateSessionKey(String client) {
        return client + "SessionKey";
    }

    private TicketGrantingTicket generateTGT(String username, String sessionKey) {
        LocalDateTime now = LocalDateTime.now();
        return TicketGrantingTicket.builder()
                .client(username)
                .tgs(tgs)
                .time(now)
                .sessionKey(sessionKey)
                .validityPeriod(tgtDuration)
                .build();
    }
}
