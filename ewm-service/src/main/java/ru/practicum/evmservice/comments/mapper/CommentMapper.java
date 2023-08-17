package ru.practicum.evmservice.comments.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.practicum.evmservice.comments.dto.CommentDto;
import ru.practicum.evmservice.comments.model.Comment;
import ru.practicum.evmservice.events.model.Event;
import ru.practicum.evmservice.users.model.User;

@Component
@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "author", target = "author_id", qualifiedByName = "UserToUserId")
    @Mapping(source = "event", target = "event_id", qualifiedByName = "EventToEventId")
    CommentDto commentToCommentDto(Comment comment);


    Comment commentDtoToComment(CommentDto commentDto);

    @Named("UserToUserId")
    static Long userId(User user) {
        return user.getId();
    }

    @Named("EventToEventId")
    static Long eventId(Event event) {
        return event.getId();
    }
}
