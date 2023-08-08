package ru.practicum.evmservice.events.service;

import client.StatsClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.EndpointHitDto;
import dto.ViewStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatsClient statsClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void saveEndpointHit(String app, String uri, String ip, String timestamp) {
        statsClient.saveEndpointHit(new EndpointHitDto(app, uri, ip, timestamp));
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        String startDateTime = start.format(formatter);
        String endDateTime = end.format(formatter);
        ResponseEntity<Object> response = statsClient.getStats(startDateTime, endDateTime, uris, unique);
        return objectMapper.convertValue(response.getBody(), new TypeReference<>() {
        });
    }

}
