package by.bsuir.server;

import by.bsuir.server.domain.Resource;
import by.bsuir.server.domain.Role;
import by.bsuir.server.domain.User;
import by.bsuir.server.repository.ResourceRepository;
import by.bsuir.server.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository,
                                               PasswordEncoder passwordEncoder,
                                               ResourceRepository resourceRepository) {
        return args -> {
            List<User> users = List.of(
                    new User("user", passwordEncoder.encode("password"), Role.USER),
                    new User("admin", passwordEncoder.encode("password"), Role.ADMIN)
            );
            users.forEach(userRepository::save);

            if (resourceRepository.findAll().isEmpty()) {
                resourceRepository.saveAll(List.of(
                        new Resource(null, "first"),
                        new Resource(null, "second"),
                        new Resource(null, "data"),
                        new Resource(null, "fourth")
                ));
            }
        };
    }
}
