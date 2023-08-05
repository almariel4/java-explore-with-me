package ru.practicum.events.controller;

import lombok.AllArgsConstructor;
import org.mapstruct.Context;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.service.EventsService;
import ru.practicum.utils.PageRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@AllArgsConstructor
public class PublicEventsController {

    private final EventsService eventsService;

    @GetMapping
    public List<EventFullDto> getEventsByFilter(@Context HttpServletRequest request,
                                                @RequestParam(required = false) String text,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) boolean paid,
                                                @RequestParam(required = false) String rangeStat,
                                                @RequestParam(required = false) String rangeEnd,
                                                @RequestParam(required = false) boolean onlyAvailable,
                                                @RequestParam(required = false) String sort,
                                                @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        String ip = request.getRemoteAddr();
        Pageable pageable = PageRequestUtil.createPageRequest(from, size);
        return eventsService.getEventsByFilter(ip, text, categories, paid, rangeStat, rangeEnd, onlyAvailable, sort, pageable);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventInfo(@Context HttpServletRequest request, @PathVariable long id) {
        String ip = request.getRemoteAddr();
        return eventsService.getEventInfo(ip, id);
    }
}
