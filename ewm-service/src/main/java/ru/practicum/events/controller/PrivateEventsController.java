package ru.practicum.events.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.model.EventRequestStatusUpdateRequest;
import ru.practicum.events.model.EventRequestStatusUpdateResult;
import ru.practicum.events.service.EventsService;
import ru.practicum.exceptions.ApiError;
import ru.practicum.exceptions.ConditionsException;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.utils.PageRequestUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class PrivateEventsController {

    private final EventsService eventsService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventsByUser(@PathVariable long userId,
                                               @RequestParam(value = "from", defaultValue = "0") Integer from,
                                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequestUtil.createPageRequest(from, size);
        return eventsService.getEventsByUser(userId, pageable);
    }

    @PostMapping("/{userId}/events")
    public EventFullDto addEventByUser(@PathVariable Long userId,
                                       @RequestBody NewEventDto newEventDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(), formatter);
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConditionsException(new ApiError());
        }
//        TODO: обработать 409 - поля на входе не должны быть пустыми
        return eventsService.addEventByUser(userId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByUser(@PathVariable long userId,
                                       @PathVariable long eventId) {
        return eventsService.getEventByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventShortDto changeEventByUser(@PathVariable long userId,
                                           @PathVariable long eventId,
                                           @RequestBody NewEventDto newEventDto) {
        return eventsService.changeEventByUser(userId, eventId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsOfEventByUser(@PathVariable long userId,
                                                                  @PathVariable long eventId) {
        return eventsService.getRequestsOfEventByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestStatusOfEvent(@PathVariable long userId,
                                                                     @PathVariable long eventId,
                                                                     @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return eventsService.changeRequestStatusOfEvent(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
