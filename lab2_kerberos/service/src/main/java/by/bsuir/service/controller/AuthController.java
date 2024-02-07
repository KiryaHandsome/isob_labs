package by.bsuir.service.controller;

import by.bsuir.data.ServiceRequest;
import by.bsuir.service.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/client-auth")
    public byte[] clientAuth(@RequestBody ServiceRequest request) {
        log.info("Client auth at service: {}", request);
        return authService.authenticateClient(request);
    }
}
