package ru.practicum.evmservice.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.evmservice.comments.model.CommentStatus;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @Size(min = 5, max = 7000)
    private String text;
    private LocalDateTime created;
    private CommentStatus commentStatus;
    private Long author_id;
    private Long event_id;
}
