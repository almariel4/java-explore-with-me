package ru.practicum.evmservice.events.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evmservice.events.dto.EventFullDto;
import ru.practicum.evmservice.events.dto.EventShortDto;
import ru.practicum.evmservice.events.dto.NewEventDto;
import ru.practicum.evmservice.events.model.EventRequestStatusUpdateRequest;
import ru.practicum.evmservice.events.model.EventRequestStatusUpdateResult;
import ru.practicum.evmservice.events.model.UpdateEventAdminRequest;
import ru.practicum.evmservice.events.service.EventsService;
import ru.practicum.evmservice.requests.dto.ParticipationRequestDto;
import ru.practicum.evmservice.utils.PageRequestUtil;

import javax.validation.Valid;
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
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEventByUser(@PathVariable Long userId,
                                       @Valid @RequestBody NewEventDto newEventDto) {
        return eventsService.addEventByUser(userId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByUser(@PathVariable long userId,
                                       @PathVariable long eventId) {
        return eventsService.getEventByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto changeEventByUser(@PathVariable long userId,
                                          @PathVariable long eventId,
                                          @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return eventsService.changeEventByUser(userId, eventId, updateEventAdminRequest);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsOfEventByUser(@PathVariable long userId,
                                                                  @PathVariable long eventId) {
        return eventsService.getRequestsOfEventByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestStatusOfEvent(@PathVariable long userId,
                                                                     @PathVariable long eventId,
                                                                     @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return eventsService.changeRequestStatusOfEvent(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
