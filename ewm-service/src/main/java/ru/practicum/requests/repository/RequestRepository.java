package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.requests.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    //    @Query("select r from Request r " +
//            "where r.requester = ?1 " +
//            "and r.id = ?2")
//    List<ParticipationRequestDto> getRequestsOfEventByUser(Long userId, Long requestId);
    List<Request> getAllByRequesterAndId(Long userId, Long requestId);

    List<Request> getAllByRequester(Long userId);

    Request getRequestByRequesterAndEvent(Long requesterId, Long eventId);

}
