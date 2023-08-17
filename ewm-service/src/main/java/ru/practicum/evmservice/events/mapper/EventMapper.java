package ru.practicum.evmservice.events.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.practicum.evmservice.categories.model.Category;
import ru.practicum.evmservice.events.dto.EventFullDto;
import ru.practicum.evmservice.events.dto.EventShortDto;
import ru.practicum.evmservice.events.dto.NewEventDto;
import ru.practicum.evmservice.events.model.Event;

@Component
@Mapper(componentModel = "spring")
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(source = "category", target = "category", qualifiedByName = "longCategoryToCategory")
    @Mapping(target = "eventDate", source = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Event newEventDtoToEvent(NewEventDto newEventDto);

    @Named("longCategoryToCategory")
    static Category category(Long category) {
        return null;
    }

    EventShortDto eventToEventShortDto(Event event);

    @Mapping(target = "eventDate", source = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventFullDto eventToEventFullDto(Event event);

}