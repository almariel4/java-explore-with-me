package ru.practicum.evmservice.requests.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.practicum.evmservice.events.model.Event;
import ru.practicum.evmservice.requests.dto.ParticipationRequestDto;
import ru.practicum.evmservice.requests.model.Request;
import ru.practicum.evmservice.users.model.User;

@Component
@Mapper(componentModel = "spring")
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(target = "created", source = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "requester", target = "requester", qualifiedByName = "setUserToLong")
    @Mapping(source = "event", target = "event", qualifiedByName = "setEventToLong")
    ParticipationRequestDto requestToParticipationRequestDto(Request request);

    @Named("setUserToLong")
    static Long userId(User user) {
        return user.getId();
    }

    @Named("setEventToLong")
    static Long eventId(Event event) {
        return event.getId();
    }


}
