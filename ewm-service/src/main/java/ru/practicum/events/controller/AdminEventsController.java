package ru.practicum.events.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.model.UpdateEventAdminRequest;
import ru.practicum.events.service.EventsService;
import ru.practicum.utils.PageRequestUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@AllArgsConstructor
public class AdminEventsController {

    private final EventsService eventsService;

    @GetMapping
    public List<EventFullDto> searchForEventsByAdmin(@RequestParam(required = false) int[] users,
                                                     @RequestParam(required = false) String[] states,
                                                     @RequestParam(required = false) int[] categories,
                                                     @RequestParam(required = false) String rangeStart,
                                                     @RequestParam(required = false) String rangeEnd,
                                                     @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime formattedRangeStart;
        LocalDateTime formattedRangeEnd;
        if (rangeStart != null) {
            formattedRangeStart = LocalDateTime.parse(rangeStart, formatter);
        } else {
            formattedRangeStart = null;
        }
        if (rangeEnd != null) {
            formattedRangeEnd = LocalDateTime.parse(rangeEnd, formatter);
        } else {
            formattedRangeEnd = null;
        }
        Pageable pageable = PageRequestUtil.createPageRequest(from, size);
        return eventsService.searchForEventsByAdmin(users, states, categories, formattedRangeStart, formattedRangeEnd, pageable);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto editEventByAdmin(@PathVariable long eventId,
                                         @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return eventsService.editEventByAdmin(eventId, updateEventAdminRequest);
    }

}
