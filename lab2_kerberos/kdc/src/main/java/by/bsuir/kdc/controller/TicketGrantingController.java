package by.bsuir.kdc.controller;

import by.bsuir.data.TGSRequest;
import by.bsuir.kdc.service.TicketGrantingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class TicketGrantingController {

    private final TicketGrantingService ticketGrantingService;

    @PostMapping("/grant-ticket")
    public byte[] grantTicket(@RequestBody TGSRequest request) {
        log.debug("Request to TGS: service={}, tgt={}, authBlock={}",
                request.getService(), request.getTgt(), request.getAuthBlock());
        return ticketGrantingService.grant(request);
    }
}
