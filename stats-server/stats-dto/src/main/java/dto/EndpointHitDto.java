package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EndpointHitDto {

    private Long id;
    private String app;
    private String uri;
    private String ip;

    private String timestamp;
}
