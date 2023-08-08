package ru.practicum.evmservice.events.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evmservice.events.dto.EventFullDto;
import ru.practicum.evmservice.events.model.EventState;
import ru.practicum.evmservice.events.model.UpdateEventAdminRequest;
import ru.practicum.evmservice.events.service.EventsService;
import ru.practicum.evmservice.utils.DateFormatUtil;
import ru.practicum.evmservice.utils.PageRequestUtil;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@AllArgsConstructor
public class AdminEventsController {

    private final EventsService eventsService;

    @GetMapping
    public List<EventFullDto> searchForEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                                     @RequestParam(required = false) List<EventState> states,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(required = false) String rangeStart,
                                                     @RequestParam(required = false) String rangeEnd,
                                                     @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        LocalDateTime formattedRangeStart = DateFormatUtil.formatStringToDate(rangeStart);
        LocalDateTime formattedRangeEnd = DateFormatUtil.formatStringToDate(rangeEnd);
        Pageable pageable = PageRequestUtil.createPageRequest(from, size);
        return eventsService.searchForEventsByAdmin(users, states, categories, formattedRangeStart, formattedRangeEnd, pageable);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto editEventByAdmin(@PathVariable long eventId,
                                         @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return eventsService.editEventByAdmin(eventId, updateEventAdminRequest);
    }

}
