package by.bsuir.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketGrantingTicket {

    private String client;
    private String tgs;
    private LocalDateTime time;
    private long validityPeriod;
    private String sessionKey;
}
