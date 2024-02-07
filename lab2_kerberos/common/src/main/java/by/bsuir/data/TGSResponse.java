package by.bsuir.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TGSResponse {

    private String tgs;
    private String clientServiceSessionKey;
}
