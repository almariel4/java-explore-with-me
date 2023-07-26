package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    ViewStatsDto viewStatsToViewStatsDto(ViewStats viewStats);

    @Mapping(target = "timestamp", source = "timestamp", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EndpointHit endpointHitDtoToEndpointHit(EndpointHitDto endpointHitDto);
}
