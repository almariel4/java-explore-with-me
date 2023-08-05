package ru.practicum.requests.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.Request;

@Component
@Mapper(componentModel = "spring")
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    ParticipationRequestDto RequestToParticipationRequestDto(Request request);

    Request ParticipationRequestDtoToRequest(ParticipationRequestDto participationRequestDto);

}
