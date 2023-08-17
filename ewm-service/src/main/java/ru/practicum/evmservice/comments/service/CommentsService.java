package ru.practicum.evmservice.comments.service;

import ru.practicum.evmservice.comments.dto.CommentDto;
import ru.practicum.evmservice.comments.dto.EventComments;

import java.util.List;

public interface CommentsService {
    List<CommentDto> getAllCommentsByEvent(Long eventId);

    CommentDto moderateCommentByAdmin(Long commentId, String status);

    void deleteCommentByAdmin(Long commentId);

    List<CommentDto> getAllUserCommentsByEvent(Long userId, Long eventId);

    List<EventComments> getAllUserCommentsByAllEvents(Long userId);

    CommentDto createCommentByUser(Long userId, CommentDto commentDto);

    CommentDto updateCommentByUser(Long userId, Long commentId, CommentDto commentDto);

    void deleteCommentByUser(Long userId, Long commentId);
}
