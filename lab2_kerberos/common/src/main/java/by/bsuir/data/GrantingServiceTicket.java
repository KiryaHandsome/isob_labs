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
public class GrantingServiceTicket {

    private String client;
    private String service;
    private LocalDateTime time;
    private long validityPeriod;
    private String clientServiceSessionKey;
}
