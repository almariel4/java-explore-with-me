package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.StatsService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class StatsController {

    private StatsService statsService;

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
        LocalDateTime encodedStart = LocalDateTime.parse(URLEncoder.encode(start, StandardCharsets.UTF_8.toString()));
        LocalDateTime encodedEnd = LocalDateTime.parse(URLEncoder.encode(end, StandardCharsets.UTF_8.toString()));
        return statsService.getStats(encodedStart, encodedEnd, uris, unique);
    }

}
