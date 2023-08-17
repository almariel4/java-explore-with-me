package ru.practicum.evmservice.requests.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.evmservice.events.model.Event;
import ru.practicum.evmservice.events.model.EventState;
import ru.practicum.evmservice.events.model.EventStatus;
import ru.practicum.evmservice.events.repository.EventRepository;
import ru.practicum.evmservice.exceptions.ApiError;
import ru.practicum.evmservice.exceptions.IncorrectRequestEcxeption;
import ru.practicum.evmservice.exceptions.NotFoundException;
import ru.practicum.evmservice.requests.dto.ParticipationRequestDto;
import ru.practicum.evmservice.requests.mapper.RequestMapper;
import ru.practicum.evmservice.requests.model.Request;
import ru.practicum.evmservice.requests.repository.RequestRepository;
import ru.practicum.evmservice.users.model.User;
import ru.practicum.evmservice.users.repository.UserRepository;
import ru.practicum.evmservice.utils.DateFormatUtil;

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
                .map(RequestMapper.INSTANCE::requestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto addRequestByUser(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
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
        if (event.getState() != null) {
            if (!event.getState().equals(EventState.PUBLISHED)) {
                throw new IncorrectRequestEcxeption((new ApiError(HttpStatus.CONFLICT.toString(),
                        "Incorrectly made request.",
                        "Нельзя принять участие в неопубликованном событии", LocalDateTime.now().toString())));
            }
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new IncorrectRequestEcxeption(new ApiError(HttpStatus.CONFLICT.toString(),
                    "For the requested operation the conditions are not met.",
                    "The participant limit has been reached", DateFormatUtil.formatDateToString(LocalDateTime.now())));
        }
        request = Request.builder()
                .event(event)
                .requester(user)
                .created(LocalDateTime.now())
                .build();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(EventStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request.setStatus(EventStatus.PENDING);
        }
        return RequestMapper.INSTANCE.requestToParticipationRequestDto(requestRepository.save(request));
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
        if (request.getStatus().equals(EventStatus.CONFIRMED)) {
            throw new IncorrectRequestEcxeption(new ApiError(HttpStatus.CONFLICT.toString(),
                    "For the requested operation the conditions are not met.",
                    "Нельзя отменить уже принятую заявку на участие в событии", DateFormatUtil.formatDateToString(LocalDateTime.now())));

        }
        request.setStatus(EventStatus.CANCELED);
        return RequestMapper.INSTANCE.requestToParticipationRequestDto(request);
    }
}
