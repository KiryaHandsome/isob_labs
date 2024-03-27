package by.bsuir.server.web;

import by.bsuir.server.domain.Role;
import by.bsuir.server.service.AuthService;
import by.bsuir.server.web.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Role login(@RequestBody LoginRequest loginRequest) {
        log.info("Request login {}", loginRequest);
        return authService.login(loginRequest);
    }
}
