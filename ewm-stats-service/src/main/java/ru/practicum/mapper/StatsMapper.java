package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

@Component
@Mapper(componentModel = "spring")
public interface StatsMapper {

    StatsMapper INSTANCE = Mappers.getMapper(StatsMapper.class);

    //    @Mapping(target = "id", source = "id")
    ViewStatsDto viewStatsToViewStatsDto(ViewStats viewStats);

    ViewStats viewStatsDtoToViewStats(ViewStatsDto viewStatsDto);

    EndpointHit endpointHitDtoToEndpointHit(EndpointHitDto endpointHitDto);
}
