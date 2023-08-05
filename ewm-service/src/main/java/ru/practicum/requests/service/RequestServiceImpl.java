package ru.practicum.requests.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventState;
import ru.practicum.events.model.EventStatus;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ApiError;
import ru.practicum.exceptions.IncorrectRequestEcxeption;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getRequestByUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + userId + " was not found", LocalDateTime.now().toString())));
        return requestRepository.getAllByRequester(userId).stream()
                .map(RequestMapper.INSTANCE::RequestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto addRequestByUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + userId + " was not found", LocalDateTime.now().toString())));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + eventId + " was not found", LocalDateTime.now().toString())));
        Request request = requestRepository.getRequestByRequesterAndEvent(userId, eventId);
        if (request != null) {
            throw new IncorrectRequestEcxeption((new ApiError(HttpStatus.CONFLICT.toString(),
                    "Incorrectly made request.",
                    "Нельзя добавить повторный запрос", LocalDateTime.now().toString())));
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new IncorrectRequestEcxeption((new ApiError(HttpStatus.CONFLICT.toString(),
                    "Incorrectly made request.",
                    "Инициатор события не может добавить запрос на участие в своём событии", LocalDateTime.now().toString())));
        }
        if (event.getState().equals(EventState.PENDING)) {
            throw new IncorrectRequestEcxeption((new ApiError(HttpStatus.CONFLICT.toString(),
                    "Incorrectly made request.",
                    "Если у события достигнут лимит запросов на участие - необходимо вернуть ошибку", LocalDateTime.now().toString())));
        }
        request = new Request(null, LocalDateTime.now().toString(), eventId, userId, null);
        if (!event.isRequestModeration()) {
            request.setStatus(EventStatus.CONFIRMED.toString());
        }
        return RequestMapper.INSTANCE.RequestToParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + userId + " was not found", LocalDateTime.now().toString())));
        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + userId + " was not found", LocalDateTime.now().toString())));
        request.setStatus("PENDING");
        return RequestMapper.INSTANCE.RequestToParticipationRequestDto(request);
    }
}
