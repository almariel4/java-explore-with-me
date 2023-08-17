package ru.practicum.evmservice.comments.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evmservice.comments.dto.CommentDto;
import ru.practicum.evmservice.comments.service.CommentsService;

@RestController
@RequestMapping(path = "/admin/comments")
@AllArgsConstructor
public class AdminCommentsController {

    private final CommentsService commentsService;

    @PatchMapping("/{commentId}")
    public CommentDto moderateCommentByAdmin(@PathVariable long commentId, @RequestParam String status) {
        return commentsService.moderateCommentByAdmin(commentId, status);
    }


    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@PathVariable long commentId) {
        commentsService.deleteCommentByAdmin(commentId);
    }

}
