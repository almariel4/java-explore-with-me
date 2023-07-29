package ru.practicum.service;

import dto.EndpointHitDto;
import dto.ViewStatsDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public void saveEndpointHit(EndpointHitDto endpointHitDto) {
        statsRepository.save(StatsMapper.INSTANCE.endpointHitDtoToEndpointHit(endpointHitDto));
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        List<ViewStatsDto> viewStatsDtos = new ArrayList<>();
        if (uris != null) {
            if (unique) {
                viewStatsDtos = statsRepository.getStatisticsByUriUniqueIp(start, end, uris).stream()
                        .map(StatsMapper.INSTANCE::viewStatsToViewStatsDto)
                        .collect(Collectors.toList());
            } else if (uris.length > 0) {
                viewStatsDtos = statsRepository.getStatisticsByUri(start, end, uris).stream()
                        .map(StatsMapper.INSTANCE::viewStatsToViewStatsDto)
                        .collect(Collectors.toList());
                viewStatsDtos.sort((o1, o2) -> o2.getHits().compareTo(o1.getHits()));
            }
        } else if (unique) {
            viewStatsDtos = statsRepository.getAllStatisticsUnique(start, end).stream()
                    .map(StatsMapper.INSTANCE::viewStatsToViewStatsDto)
                    .collect(Collectors.toList());
        } else {
            viewStatsDtos = statsRepository.getAllStatistics(start, end).stream()
                    .map(StatsMapper.INSTANCE::viewStatsToViewStatsDto)
                    .collect(Collectors.toList());
        }
        return viewStatsDtos;
    }
}
