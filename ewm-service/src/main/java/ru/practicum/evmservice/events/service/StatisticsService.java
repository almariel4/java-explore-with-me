package ru.practicum.evmservice.events.service;

import dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {

    void saveEndpointHit(String app, String uri, String ip, String timestamp);

    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique);
}
