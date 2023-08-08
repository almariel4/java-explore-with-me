package ru.practicum.evmservice.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.evmservice.requests.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> getAllByEventId(Long eventId);

    @Query("select r from Request r " +
            "where r.requester.id = ?1")
    List<Request> getAllByRequester(Long userId);

    @Query("select r from Request r " +
            "where r.requester.id = ?1 " +
            "and r.event.id = ?2")
    Request getRequestByRequesterAndEvent(Long requesterId, Long eventId);

}
