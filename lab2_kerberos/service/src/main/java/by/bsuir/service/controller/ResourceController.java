package by.bsuir.service.controller;

import by.bsuir.des.EncryptionUtils;
import by.bsuir.service.service.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ResourceController {

    private final SessionManager sessionManager;

    @PostMapping("/check")
    public byte[] someResource(@RequestHeader("client") String client, @RequestBody byte[] request) {
        String sessionKey = sessionManager.getSessionKey(client);
        String data = EncryptionUtils.decrypt(request, sessionKey);
        log.info("Client {} sent next message: {}", client, data);
        return EncryptionUtils.encrypt("Hello from service!".getBytes(), sessionKey);
    }
}
