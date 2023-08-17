package ru.practicum.evmservice.requests.service;

import ru.practicum.evmservice.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getRequestByUser(Long userId);

    ParticipationRequestDto addRequestByUser(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId);

}
