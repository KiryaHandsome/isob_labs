package by.bsuir.kdc.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class KDCDatabase {

    private final Map<String, String> secrets = Map.of(
            "client", "clientSecret",
            "service", "serviceSecret",
            "kdc", "kdcSecret",
            "tgs_service", "tgsServiceSecret"
    );

    private final Map<String, String> clientsSessionKeys = new HashMap<>();


    public boolean hasClient(String client) {
        return secrets.containsKey(client);
    }

    public String getSecret(String client) {
        return Optional.ofNullable(secrets.get(client))
                .orElseThrow(() -> new RuntimeException("There is no client=" + client));
    }

    public String getSessionKey(String client) {
        return Optional.ofNullable(clientsSessionKeys.get(client))
                .orElseThrow(() -> new RuntimeException("No session key for client=" + client));
    }

    public void addSessionKey(String client, String sessionKey) {
        clientsSessionKeys.put(client, sessionKey);
    }
}
