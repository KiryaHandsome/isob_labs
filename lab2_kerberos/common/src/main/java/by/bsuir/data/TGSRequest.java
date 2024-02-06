package by.bsuir.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TGSRequest {

    private String service;
    private byte[] tgt;
    private byte[] authBlock;
}
