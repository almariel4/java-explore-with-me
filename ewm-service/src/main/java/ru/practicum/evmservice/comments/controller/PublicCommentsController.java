package ru.practicum.evmservice.comments.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.evmservice.comments.dto.CommentDto;
import ru.practicum.evmservice.comments.service.CommentsService;

import java.util.List;

@RestController
@RequestMapping(path = "/comments")
@AllArgsConstructor
public class PublicCommentsController {

    private final CommentsService commentsService;

    @GetMapping("{eventId}")
    List<CommentDto> getAllCommentsByEvent(@PathVariable long eventId) {
        return commentsService.getAllCommentsByEvent(eventId);
    }
}
