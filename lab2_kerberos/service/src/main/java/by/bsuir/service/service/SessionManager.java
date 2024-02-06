package by.bsuir.service.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SessionManager {

    private final Map<String, String> sessions = new HashMap<>();

    public void addSession(String client, String secret) {
        sessions.put(client, secret);
    }

    public String getSessionKey(String client) {
        return Optional.ofNullable(sessions.get(client))
                .orElseThrow(() -> new RuntimeException("no session key for client=" + client));
    }
}
