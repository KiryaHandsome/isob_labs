package by.bsuir.data;

import java.time.LocalDateTime;

public record AuthBlock(
        LocalDateTime time,
        String client
) {

}
