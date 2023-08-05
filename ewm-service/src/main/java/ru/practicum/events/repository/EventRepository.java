package ru.practicum.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.events.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> getAllByCompilationId(Long compId);

    @Query("select e from Event e " +
            "where e.initiator.id = ?1 ")
    List<Event> getAllByUser(Long userId, Pageable pageable);

    @Query("select e from Event e " +
            "where e.id = ?2 " +
            "and e.initiator.id = ?1 ")
    Event getEventByUser(long userId, long eventId);

//    public List<EventFullDto> searchForEventsByAdmin(int[] users, String[] states, int[] categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {

  /*  List<Event> getAllByStateIn(Collection<EventState> state, Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id = ?1")
    List<Event> getAllByCategory(int[] categories, Pageable pageable);

    List<Event> getAllByEventDateAfter(String rangeStart, Pageable pageable);

    List<Event> getAllByEventDateBefore(String rangeEnd, Pageable pageable);

    @Query("select e from Event e " +
            "where e.initiator.id = ?1 " +
            "and e.state in ?2")
    List<Event> getAllByUserAndStates(int[] users, String[] states, Pageable pageable);

    @Query("select e from Event e " +
            "where e.initiator.id = ?1 " +
            "and e.category.id in ?2")
    List<Event> getAllByUserAndCategories(int[] users, int[] categories, Pageable pageable);

    @Query("select e from Event e " +
            "where e.initiator.id = ?1 " +
            "and e.eventDate >= ?2")
    List<Event> getAllByUserAndStart(int[] users, LocalDateTime rangeStart, Pageable pageable);

    @Query("select e from Event e " +
            "where e.initiator.id = ?1 " +
            "and e.eventDate <= ?2")
    List<Event> getAllByUserAndEnd(int[] users, LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.state in ?2")
    List<Event> getAllByCategoriesAndState(int[] categories, String[] states, Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.eventDate >= ?2")
    List<Event> getAllByCategoriesAndStart(int[] categories, LocalDateTime rangeStart, Pageable pageable);

    @Query("select e from Event e " +
            "where e.category.id in ?1 " +
            "and e.eventDate <= ?2")
    List<Event> getAllByCategoriesAndEnd(int[] categories, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> getAllByEventDateBetween(String eventDate, String eventDate2, Pageable pageable);

    @Query("select e from Event e " +
            "where e.initiator.id = ?1 " +
            "and e.state in ?2 " +
            "and e.category.id in ?3")
    List<Event> getAllByUserAndStateAndCategory(int[] users, String[] states, int[] categories);

    @Query("select e from Event e " +
            "where (upper(e.annotation)) like upper(concat('%', ?1, '%'))")
    List<Event> getEventsWithText(String text, Pageable pageable);*/

    // ---
    @Query("select e from Event e " +
            "where e.initiator.id in (:users) or :users is null " +
            "and e.state in (:states) or :states is null " +
            "and e.category.id in (:categories) or :categories is null " +
            "and e.eventDate > :rangeStart or :rangeStart is null " +
            "and e.eventDate < :rangeEnd or :rangeEnd is null")
    List<Event> getAll(@Param("users") int[] users,
                       @Param("states") String[] states,
                       @Param("categories") int[] categories,
                       @Param("rangeStart") LocalDateTime rangeStart,
                       @Param("rangeEnd") LocalDateTime rangeEnd,
                       Pageable pageable);

    @Query("select e from Event e " +
            "where upper(e.annotation) like concat('%',upper(:text),'%') " +
            "or upper(e.description) like concat('%',upper(:text),'%') " +
            "or :text is null " +
            "and e.category.id in (:categories) or :categories is null " +
            "and e.paid in (:paid) or :paid is null " +
            "and e.eventDate between :rangeStart and :rangeEnd")
    List<Event> getEventsByFilter(@Param("text") String text,
                                  @Param("categories") List<Long> categories,
                                  @Param("paid") boolean paid,
                                  @Param("rangeStart") String rangeStart,
                                  @Param("rangeEnd") String rangeEnd,
                                  Pageable pageable);

    @Query("select e from Event e " +
            "where upper(e.annotation) like concat('%',upper(:text),'%') " +
            "or upper(e.description) like concat('%',upper(:text),'%') " +
            "or :text is null " +
            "and e.category.id in (:categories) or :categories is null " +
            "and e.paid in (:paid) or :paid is null " +
            "and e.eventDate < current_date ")
    List<Event> getEventsAfterCurrentDate(@Param("text") String text,
                                          @Param("categories") List<Long> categories,
                                          @Param("paid") boolean paid,
                                          Pageable pageable);
}
