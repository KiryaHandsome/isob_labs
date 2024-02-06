package by.bsuir.kdc.controller;

import by.bsuir.data.AuthRequest;
import by.bsuir.kdc.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/authenticate")
    public byte[] authenticate(@RequestBody AuthRequest request) {
        log.debug("Authenticate request for client with id: {}", request.client());
        return authService.authenticate(request);
    }

}
