package by.bsuir.server.service;

import by.bsuir.server.domain.Role;
import by.bsuir.server.exception.InvalidPasswordException;
import by.bsuir.server.exception.UserNotFoundException;
import by.bsuir.server.repository.UserRepository;
import by.bsuir.server.web.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Role login(LoginRequest loginRequest) {
        var user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new UserNotFoundException("User with such username not found"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        return user.getRole();
    }
}
