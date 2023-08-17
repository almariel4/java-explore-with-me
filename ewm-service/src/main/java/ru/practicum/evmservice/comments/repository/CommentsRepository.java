package ru.practicum.evmservice.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.evmservice.comments.model.Comment;
import ru.practicum.evmservice.events.model.Event;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c " +
            "where c.event.id = ?1")
    List<Comment> getCommentsByEventId(Long eventId);

    @Query("select c from Comment c " +
            "where c.author.id = ?1 " +
            "and c.event.id = ?2")
    List<Comment> getAllUserCommentsByEvent(Long userId, Long eventId);

    @Query("select c from Comment c " +
            "where c.author.id = ?1")
    List<Event> getAllEventsWithComments(Long userId);
}
