package ru.practicum.evmservice.events.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.evmservice.events.dto.EventFullDto;
import ru.practicum.evmservice.events.dto.EventShortDto;
import ru.practicum.evmservice.events.dto.NewEventDto;
import ru.practicum.evmservice.events.model.EventRequestStatusUpdateRequest;
import ru.practicum.evmservice.events.model.EventRequestStatusUpdateResult;
import ru.practicum.evmservice.events.model.EventState;
import ru.practicum.evmservice.events.model.UpdateEventAdminRequest;
import ru.practicum.evmservice.requests.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventsService {

    List<EventShortDto> getEventsByUser(Long userId, Pageable pageable);

    EventFullDto addEventByUser(Long userId, NewEventDto newEventDto);

    EventFullDto getEventByUser(Long userId, Long eventId);

    EventFullDto changeEventByUser(Long userId, Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<ParticipationRequestDto> getRequestsOfEventByUser(Long userId, Long eventId);

    EventRequestStatusUpdateResult changeRequestStatusOfEvent(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<EventFullDto> searchForEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    EventFullDto editEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventFullDto> getEventsByFilter(String ip, String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, boolean onlyAvailable, String sort, Pageable pageable, HttpServletRequest request);

    EventFullDto getEventInfo(HttpServletRequest request, Long eventId);
}
