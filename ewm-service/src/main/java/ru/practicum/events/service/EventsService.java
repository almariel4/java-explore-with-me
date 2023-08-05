package ru.practicum.events.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.model.EventRequestStatusUpdateRequest;
import ru.practicum.events.model.EventRequestStatusUpdateResult;
import ru.practicum.events.model.UpdateEventAdminRequest;
import ru.practicum.requests.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsService {

    List<EventShortDto> getEventsByUser(Long userId, Pageable pageable);

    EventFullDto addEventByUser(Long userId, NewEventDto newEventDto);

    EventFullDto getEventByUser(Long userId, Long eventId);

    EventShortDto changeEventByUser(Long userId, Long eventId, NewEventDto newEventDto);

    List<ParticipationRequestDto> getRequestsOfEventByUser(Long userId, Long eventId);

    EventRequestStatusUpdateResult changeRequestStatusOfEvent(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<EventFullDto> searchForEventsByAdmin(int[] users, String[] states, int[] categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    EventFullDto editEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventFullDto> getEventsByFilter(String ip, String text, List<Long> categories, boolean paid, String rangeStat, String rangeEnd, boolean onlyAvailable, String sort, Pageable pageable);

    EventFullDto getEventInfo(String ip, Long eventId);
}
