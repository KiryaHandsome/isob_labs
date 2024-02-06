package by.bsuir.data;

public record AuthResponse(
        byte[] tgt,
        String sessionKey
) {

}
