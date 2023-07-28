package ru.practicum.controller;

import dto.EndpointHitDto;
import dto.ViewStatsDto;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
@RequestMapping
@AllArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping({"/hit"})
    public void saveEndpointHit(@RequestBody EndpointHitDto endpointHit) {
        statsService.saveEndpointHit(endpointHit);
    }

    @SneakyThrows
    @GetMapping({"/stats"})
    public List<ViewStatsDto> getStats(@RequestParam String start,
                                       @RequestParam String end,
                                       @RequestParam(required = false) String[] uris,
                                       @RequestParam(required = false) boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime formattedStart = LocalDateTime.parse(start, formatter);
        LocalDateTime formattedEnd = LocalDateTime.parse(end, formatter);
        return statsService.getStats(formattedStart, formattedEnd, uris, unique);
    }
}
