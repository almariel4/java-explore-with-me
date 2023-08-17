package ru.practicum.evmservice.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.evmservice.categories.model.Category;
import ru.practicum.evmservice.events.model.Event;
import ru.practicum.evmservice.events.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> getAllByIdIn(List<Long> events);

    List<Event> getEventByCategory(Category category);

    @Query("select e from Event e " +
            "where e.initiator.id = ?1 ")
    List<Event> getAllByUser(Long userId, Pageable pageable);

    @Query("select e from Event e " +
            "where e.id = ?2 " +
            "and e.initiator.id = ?1 ")
    Event getEventByUser(long userId, long eventId);

    @Query("select e from Event e " +
            "where (:users is null or e.initiator.id in (:users) ) " +
            "and (:states is null or e.state in (:states)) " +
            "and (:categories is null or e.category.id in (:categories) ) " +
            "and (e.eventDate between :rangeStart and :rangeEnd) " +
            "order by e.createdOn desc")
    List<Event> getAll(@Param("users") List<Long> users,
                       @Param("states") List<EventState> states,
                       @Param("categories") List<Long> categories,
                       @Param("rangeStart") LocalDateTime rangeStart,
                       @Param("rangeEnd") LocalDateTime rangeEnd,
                       Pageable pageable);

    @Query("select e from Event e " +
            "where ((lower(e.annotation) like concat('%',lower(:text) ,'%') " +
            "or lower(e.description) like concat('%',lower(:text) ,'%') " +
            "or :text is null)) " +
            "and (e.category.id in (:categories) or :categories is null) " +
            "and (e.paid in (:paid) or :paid is null)  " +
            "and (e.eventDate between :rangeStart and :rangeEnd)")
    List<Event> getEventsByFilter(@Param("text") String text,
                                  @Param("categories") List<Long> categories,
                                  @Param("paid") Boolean paid,
                                  @Param("rangeStart") LocalDateTime rangeStart,
                                  @Param("rangeEnd") LocalDateTime rangeEnd,
                                  Pageable pageable);
}
