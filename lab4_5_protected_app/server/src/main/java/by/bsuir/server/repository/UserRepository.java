package by.bsuir.server.repository;

import by.bsuir.server.domain.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {

    private final Map<String, User> users = new HashMap<>();

    public Optional<User> findByUsername(String username) {
        User user = users.getOrDefault(username, null);
        return Optional.ofNullable(user);
    }

    public void save(User user) {
        users.put(user.getUsername(), user);
    }
}
