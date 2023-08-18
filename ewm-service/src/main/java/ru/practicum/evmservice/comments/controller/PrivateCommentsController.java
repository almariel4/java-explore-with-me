package ru.practicum.evmservice.comments.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evmservice.comments.dto.CommentDto;
import ru.practicum.evmservice.comments.service.CommentsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/comments")
@AllArgsConstructor
public class PrivateCommentsController {

    private final CommentsService commentsService;

    @GetMapping("/{userId}/event/{eventId}")
    public List<CommentDto> getAllUserCommentsByEvent(@PathVariable long userId, @PathVariable long eventId) {
        return commentsService.getAllUserCommentsByEvent(userId, eventId);
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createCommentByUser(@PathVariable long userId, @Valid @RequestBody CommentDto commentDto) {
        return commentsService.createCommentByUser(userId, commentDto);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public CommentDto updateCommentByUser(@PathVariable long userId, @PathVariable long commentId, @Valid @RequestBody CommentDto commentDto) {
        return commentsService.updateCommentByUser(userId, commentId, commentDto);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByUser(@PathVariable long userId, @PathVariable long commentId) {
        commentsService.deleteCommentByUser(userId, commentId);
    }
}
