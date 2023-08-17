package ru.practicum.evmservice.requests.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evmservice.requests.dto.ParticipationRequestDto;
import ru.practicum.evmservice.requests.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class PrivateRequestsController {

    private final RequestService requestService;

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequestByUser(@PathVariable long userId) {
        return requestService.getRequestByUser(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequestByUser(@PathVariable long userId,
                                                    @RequestParam long eventId) {
        return requestService.addRequestByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByUser(@PathVariable long userId,
                                                       @PathVariable long requestId) {
        return requestService.cancelRequestByUser(userId, requestId);
    }

}
