package ru.practicum.evmservice.events.service;

import dto.ViewStatsDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.evmservice.categories.model.Category;
import ru.practicum.evmservice.categories.repository.CategoryRepository;
import ru.practicum.evmservice.events.dto.EventFullDto;
import ru.practicum.evmservice.events.dto.EventShortDto;
import ru.practicum.evmservice.events.dto.NewEventDto;
import ru.practicum.evmservice.events.mapper.EventMapper;
import ru.practicum.evmservice.events.model.*;
import ru.practicum.evmservice.events.repository.EventRepository;
import ru.practicum.evmservice.exceptions.ApiError;
import ru.practicum.evmservice.exceptions.ConditionsException;
import ru.practicum.evmservice.exceptions.IncorrectRequestEcxeption;
import ru.practicum.evmservice.exceptions.NotFoundException;
import ru.practicum.evmservice.requests.dto.ParticipationRequestDto;
import ru.practicum.evmservice.requests.mapper.RequestMapper;
import ru.practicum.evmservice.requests.model.Request;
import ru.practicum.evmservice.requests.repository.RequestRepository;
import ru.practicum.evmservice.users.model.User;
import ru.practicum.evmservice.users.repository.UserRepository;
import ru.practicum.evmservice.utils.DateFormatUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.evmservice.events.model.StateAction.CANCEL_REVIEW;
import static ru.practicum.evmservice.events.model.StateAction.REJECT_EVENT;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventsService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final RequestRepository requestRepository;

    private final CategoryRepository categoryRepository;

    private final StatisticsService statsService;
    private final String appName = "ewm";

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<EventShortDto> getEventsByUser(Long userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        return eventRepository.getAllByUser(userId, pageable).stream()
                .map(EventMapper.INSTANCE::eventToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto addEventByUser(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate() != null) {
            LocalDateTime dateTime = DateFormatUtil.formatStringToDate(newEventDto.getEventDate());
            if (dateTime.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ConditionsException(new ApiError());
            }
        }

        Optional<Category> category = categoryRepository.findById(newEventDto.getCategory());
        Optional<User> user = userRepository.findById(userId);
        Event event = EventMapper.INSTANCE.newEventDtoToEvent(newEventDto);
        event.setCategory(category.get());
        event.setInitiator(user.get());
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0L);
        if (newEventDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0L);
        }
        if (newEventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        return EventMapper.INSTANCE.eventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEventByUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + userId + " was not found", LocalDateTime.now().format(formatter))));
        eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + eventId + " was not found", LocalDateTime.now().format(formatter))));

        return EventMapper.INSTANCE.eventToEventFullDto(eventRepository.getEventByUser(userId, eventId));
    }

    @Override
    public EventFullDto changeEventByUser(Long userId, Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + userId + " was not found", LocalDateTime.now().format(formatter))));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + eventId + " was not found", LocalDateTime.now().format(formatter))));
        if (event.getState() != null) {
            if (event.getState().equals(EventState.PUBLISHED)) {
                throw new IncorrectRequestEcxeption(new ApiError(
                        HttpStatus.FORBIDDEN.toString(), "For the requested operation the conditions are not met.",
                        "Only pending or canceled events can be changed", LocalDateTime.now().format(formatter)));
            }
            if (!event.getInitiator().getId().equals(userId)) {
                throw new ConditionsException(new ApiError(
                        HttpStatus.BAD_REQUEST.toString(), "Incorrectly made request.",
                        "Только инициатор события может изменять его", LocalDateTime.now().format(formatter)));
            }
            if (event.getState().equals(EventState.CANCELED)) {
                event.setState(EventState.PENDING);
            }
            if (event.getState().equals(EventState.PUBLISHED) && event.getInitiator().getId().equals(userId)) {
                throw new ConditionsException(new ApiError(
                        HttpStatus.CONFLICT.toString(), "Incorrectly made request.",
                        "Нельзя изменить событие от имени пользователя", LocalDateTime.now().format(formatter)));
            }
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            LocalDateTime dateTime = DateFormatUtil.formatStringToDate(updateEventAdminRequest.getEventDate());
            if (dateTime.isBefore(LocalDateTime.now())) {
                throw new ConditionsException(new ApiError(
                        HttpStatus.BAD_REQUEST.toString(), "Incorrectly made request.",
                        "Дата начала события не может быть в прошлом", LocalDateTime.now().format(formatter)));
            }
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(REJECT_EVENT) ||
                    updateEventAdminRequest.getStateAction().equals(CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            } else {
                event.setState(EventState.PENDING);
            }
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(DateFormatUtil.formatStringToDate(updateEventAdminRequest.getEventDate()));
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(updateEventAdminRequest.getLocation());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventAdminRequest.getCategory()).get());
        }
        return EventMapper.INSTANCE.eventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<ParticipationRequestDto> getRequestsOfEventByUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConditionsException(new ApiError(HttpStatus.FORBIDDEN.toString(),
                    "For the requested operation the conditions are not met.",
                    "Дата начала изменяемого события должна быть раньше текущей даты.",
                    LocalDateTime.now().format(formatter)));
        }
        return requestRepository.getAllByEventId(eventId).stream()
                .map(RequestMapper.INSTANCE::requestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult changeRequestStatusOfEvent(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()
                || eventRequestStatusUpdateRequest.getRequestIds().isEmpty()) {
            return new EventRequestStatusUpdateResult(List.of(), List.of());
        }

        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new IncorrectRequestEcxeption(new ApiError(HttpStatus.CONFLICT.toString(),
                    "For the requested operation the conditions are not met.",
                    "The participant limit has been reached", LocalDateTime.now().format(formatter)));
        }
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();
        EventStatus status = eventRequestStatusUpdateRequest.getStatus();
        for (Long requestId : eventRequestStatusUpdateRequest.getRequestIds()) {
            Request request = requestRepository.findById(requestId).get();
            if (!request.getStatus().equals(EventStatus.PENDING)) {
                throw new IncorrectRequestEcxeption(new ApiError(HttpStatus.CONFLICT.toString(),
                        "For the requested operation the conditions are not met.",
                        "Можно изменять только заявки в ожидании", LocalDateTime.now().format(formatter)));
            }
            switch (status) {
                case CONFIRMED:
                    if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                        request.setStatus(EventStatus.CONFIRMED);
                        requestRepository.save(request);
                        confirmed.add(RequestMapper.INSTANCE.requestToParticipationRequestDto(request));
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                        eventRepository.save(event);
                    } else {
                        request.setStatus(EventStatus.REJECTED);
                        requestRepository.save(request);
                        rejected.add(RequestMapper.INSTANCE.requestToParticipationRequestDto(request));
                    }
                    break;
                case REJECTED:
                    request.setStatus(EventStatus.REJECTED);
                    requestRepository.save(request);
                    rejected.add(RequestMapper.INSTANCE.requestToParticipationRequestDto(request));
                    break;
            }
        }
        eventRequestStatusUpdateResult.setConfirmedRequests(confirmed);
        eventRequestStatusUpdateResult.setRejectedRequests(rejected);
        return eventRequestStatusUpdateResult;
    }

    @Override
    public List<EventFullDto> searchForEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        List<EventFullDto> eventFullDtos;
        if (rangeStart == null || rangeEnd == null) {
            rangeStart = LocalDateTime.now();
            rangeEnd = LocalDateTime.now().plusYears(1);
        }
        if (rangeEnd.isBefore(rangeEnd)) {
            throw new ConditionsException(new ApiError(HttpStatus.FORBIDDEN.toString(),
                    "For the requested operation the conditions are not met.",
                    "Дата начала изменяемого события должна быть раньше текущей даты.",
                    LocalDateTime.now().format(formatter)));
        }
        eventFullDtos = eventRepository.getAll(users, states, categories, rangeStart, rangeEnd, pageable)
                .stream()
                .map(EventMapper.INSTANCE::eventToEventFullDto)
                .collect(Collectors.toList());
        return eventFullDtos;
    }

    @Override
    public EventFullDto editEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + eventId + " was not found", LocalDateTime.now().format(formatter))));
        if (updateEventAdminRequest.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(updateEventAdminRequest.getEventDate(), formatter);
            if (eventDate.isBefore(LocalDateTime.now())) {
                throw new ConditionsException(new ApiError(HttpStatus.FORBIDDEN.toString(),
                        "For the requested operation the conditions are not met.",
                        "Дата начала изменяемого события должна быть раньше текущей даты.",
                        LocalDateTime.now().format(formatter)));
            }
        }

        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventAdminRequest.getCategory()).orElseThrow(NotFoundException::new);
            event.setCategory(category);
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(DateFormatUtil.formatStringToDate(updateEventAdminRequest.getEventDate()));
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(updateEventAdminRequest.getLocation());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction() == StateAction.PUBLISH_EVENT) {
                if (event.getState().equals(EventState.PENDING)) {
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                } else {
                    throw new IncorrectRequestEcxeption(new ApiError(HttpStatus.FORBIDDEN.toString(),
                            "For the requested operation the conditions are not met.",
                            "Cобытие можно публиковать, только если оно в состоянии ожидания публикации",
                            LocalDateTime.now().format(formatter)));
                }
            }
            if (updateEventAdminRequest.getStateAction() == REJECT_EVENT &&
                    event.getState().equals(EventState.PUBLISHED)) {
                throw new IncorrectRequestEcxeption(new ApiError(HttpStatus.FORBIDDEN.toString(),
                        "For the requested operation the conditions are not met.",
                        "Событие можно отклонить, только если оно еще не опубликовано",
                        LocalDateTime.now().format(formatter)));
            } else if (updateEventAdminRequest.getStateAction() == REJECT_EVENT &&
                    !event.getState().equals(EventState.PUBLISHED)) {
                event.setState(EventState.CANCELED);
            }
        }
        return EventMapper.INSTANCE.eventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getEventsByFilter(String ip, String text, List<Long> categories, Boolean paid,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable,
                                                String sort, Pageable pageable, HttpServletRequest request) {
        List<EventFullDto> eventFullDtos;
        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new ConditionsException(new ApiError(HttpStatus.BAD_REQUEST.toString(),
                        "For the requested operation the conditions are not met.",
                        "Дата окончания события должна быть не ранее даты начала.",
                        LocalDateTime.now().format(formatter)));
            }
        }
        if (rangeStart == null || rangeEnd == null) {
            rangeStart = LocalDateTime.now();
            rangeEnd = LocalDateTime.now().plusYears(20000);
        }
        List<Event> events = eventRepository.getEventsByFilter(text, categories, paid, rangeStart, rangeEnd, pageable);
        if (onlyAvailable) {
            eventFullDtos = events.stream()
                    .filter(event -> event.getParticipantLimit() == 0 || event.getParticipantLimit() > event.getConfirmedRequests())
                    .map(EventMapper.INSTANCE::eventToEventFullDto)
                    .collect(Collectors.toList());
        } else {
            eventFullDtos = events.stream().map(EventMapper.INSTANCE::eventToEventFullDto)
                    .collect(Collectors.toList());
        }

        statsService.saveEndpointHit(
                appName, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().format(formatter));

        if (!eventFullDtos.isEmpty() && sort != null) {
            switch (sort) {
                case "EVENT_DATE":
                    eventFullDtos.sort(((o1, o2) -> o2.getEventDate().compareTo(o1.getEventDate())));
                    break;
                case "VIEWS":
                    eventFullDtos.sort(((o1, o2) -> o2.getViews().compareTo(o1.getViews())));
            }
        }
        return eventFullDtos;
    }

    @Override
    public EventFullDto getEventInfo(HttpServletRequest request, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + eventId + " was not found", LocalDateTime.now().format(formatter))));
        if (event.getState() == null || !event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                    "The required object was not found.",
                    "Событие должно быть опубликовано", LocalDateTime.now().format(formatter)));
        }
        statsService.saveEndpointHit(
                appName, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().format(formatter));
        String[] reqs = List.of(request.getRequestURI()).toArray(new String[0]);
        List<ViewStatsDto> statsDtos = statsService.getStats(event.getPublishedOn(), LocalDateTime.now(),
                reqs, true);
        event.setViews(statsDtos.get(0).getHits());
        return EventMapper.INSTANCE.eventToEventFullDto(event);
    }
}
