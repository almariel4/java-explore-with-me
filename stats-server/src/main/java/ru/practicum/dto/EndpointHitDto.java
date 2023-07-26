package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EndpointHitDto {

    @ReadOnlyProperty
    private Long id;
    private String app;
    private String uri;
    private String ip;

    private String timestamp;
}
