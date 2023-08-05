package ru.practicum.events.service;

import dto.EndpointHitDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.*;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ApiError;
import ru.practicum.exceptions.ConditionsException;
import ru.practicum.exceptions.IncorrectRequestEcxeption;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.service.StatsService;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventsService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final RequestRepository requestRepository;

    private final CategoryRepository categoryRepository;

    private final StatsService statsService;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<EventShortDto> getEventsByUser(Long userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Event event = eventRepository.findById(userId).orElseThrow(NotFoundException::new);
        return eventRepository.getAllByUser(userId, pageable).stream()
                .map(EventMapper.INSTANCE::EventToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto addEventByUser(Long userId, NewEventDto newEventDto) {
        Category category = categoryRepository.getReferenceById(newEventDto.getCategory());
        User user = userRepository.getReferenceById(userId);

        Event event = new Event(null,
                newEventDto.getAnnotation(), category, 0L, LocalDateTime.now(), newEventDto.getDescription(),
                LocalDateTime.parse(newEventDto.getEventDate(), formatter), user, newEventDto.getLocation(),
                newEventDto.isPaid(), newEventDto.getParticipantLimit().longValue(), LocalDateTime.now(), newEventDto.isRequestModeration(),
                EventState.PENDING, newEventDto.getTitle(), 0L, null);
        eventRepository.save(event);
        return EventMapper.INSTANCE.EventToEventFullDto(event);
    }

    @Override
    public EventFullDto getEventByUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + userId + " was not found", LocalDateTime.now().toString())));
        eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + eventId + " was not found", LocalDateTime.now().toString())));

        return EventMapper.INSTANCE.EventToEventFullDto(eventRepository.getEventByUser(userId, eventId));
    }

    @Override
    public EventShortDto changeEventByUser(Long userId, Long eventId, NewEventDto newEventDto) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + userId + " was not found", LocalDateTime.now().toString())));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + eventId + " was not found", LocalDateTime.now().toString())));
        if (!event.getState().equals(EventState.PENDING) ||
                !event.getState().equals(EventState.CANCELED)) {
            throw new IncorrectRequestEcxeption(new ApiError(
                    HttpStatus.FORBIDDEN.toString(), "For the requested operation the conditions are not met.",
                    "Only pending or canceled events can be changed", LocalDateTime.now().toString()));
        }
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConditionsException(new ApiError(
                    HttpStatus.BAD_REQUEST.toString(), "Incorrectly made request.",
                    "Event must not be published", LocalDateTime.now().toString()));
        }
        return EventMapper.INSTANCE.EventToEventShortDto(eventRepository.save(event));
    }

    @Override
    public List<ParticipationRequestDto> getRequestsOfEventByUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        return requestRepository.getAllByRequesterAndId(userId, eventId).stream()
                .map(RequestMapper.INSTANCE::RequestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult changeRequestStatusOfEvent(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            return null;
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new IncorrectRequestEcxeption(new ApiError());
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new IncorrectRequestEcxeption(new ApiError(HttpStatus.CONFLICT.toString(),
                    "For the requested operation the conditions are not met.",
                    "The participant limit has been reached", LocalDateTime.now().toString()));
        }

        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();
        EventStatus status = eventRequestStatusUpdateRequest.getStatus();
        for (Long requestId : eventRequestStatusUpdateRequest.getRequestIds()) {
            ParticipationRequestDto participationRequestDto = RequestMapper.INSTANCE.RequestToParticipationRequestDto(
                    requestRepository.getReferenceById(requestId));
            switch (status) {
                case CONFIRMED:
                    if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                        confirmed.add(participationRequestDto);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    } else {
                        rejected.add(participationRequestDto);
                    }
                    break;
                case REJECTED:
                    rejected.add(participationRequestDto);
                    break;
            }
        }
        eventRequestStatusUpdateResult.setConfirmedRequests(confirmed);
        eventRequestStatusUpdateResult.setRejectedRequests(rejected);
        return eventRequestStatusUpdateResult;
    }

    @Override
    public List<EventFullDto> searchForEventsByAdmin(int[] users, String[] states, int[] categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        List<EventFullDto> eventFullDtos;
        eventFullDtos = eventRepository.getAll(users, states, categories, rangeStart, rangeEnd, pageable).stream()
                .map(EventMapper.INSTANCE::EventToEventFullDto)
                .collect(Collectors.toList());
        return eventFullDtos;
    }

    @Override
    public EventFullDto editEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + eventId + " was not found", LocalDateTime.now().toString())));

        if (updateEventAdminRequest.getStateAction().equals(EventStateAdmin.PUBLISH_EVENT) &&
                !event.getState().equals(EventState.PENDING)) {
            throw new IncorrectRequestEcxeption(new ApiError(HttpStatus.FORBIDDEN.toString(),
                    "For the requested operation the conditions are not met.",
                    "Cобытие можно публиковать, только если оно в состоянии ожидания публикации",
                    LocalDateTime.now().toString()));
        }
        if (updateEventAdminRequest.getStateAction().equals(EventStateAdmin.REJECT_EVENT) &&
                event.getState().equals(EventState.PUBLISHED)) {
            throw new IncorrectRequestEcxeption(new ApiError(HttpStatus.FORBIDDEN.toString(),
                    "For the requested operation the conditions are not met.",
                    "Событие можно отклонить, только если оно еще не опубликовано",
                    LocalDateTime.now().toString()));
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(updateEventAdminRequest.getEventDate());
            LocalDateTime createdOn = event.getCreatedOn();
            if (eventDate.isBefore(createdOn.minusHours(1))) {
                throw new IncorrectRequestEcxeption(new ApiError(HttpStatus.FORBIDDEN.toString(),
                        "For the requested operation the conditions are not met.",
                        "Дата начала изменяемого события должна быть не ранее чем за час от даты публикации.",
                        LocalDateTime.now().toString()));
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
            event.setEventDate(LocalDateTime.parse(updateEventAdminRequest.getEventDate()));
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(updateEventAdminRequest.getLocation());
        }
        if (updateEventAdminRequest.isPaid() != event.isPaid()) {
            event.setPaid(updateEventAdminRequest.isPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.isRequestModeration() != event.isRequestModeration()) {
            event.setRequestModeration(updateEventAdminRequest.isRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        return EventMapper.INSTANCE.EventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getEventsByFilter(String ip, String text, List<Long> categories, boolean paid,
                                                String rangeStart, String rangeEnd, boolean onlyAvailable,
                                                String sort, Pageable pageable) {
        List<EventFullDto> eventFullDtos = new ArrayList<>();
        if (rangeStart != null && rangeEnd != null) {
            List<Event> events = eventRepository.getEventsByFilter(text, categories, paid, rangeStart, rangeEnd, pageable);
            if (onlyAvailable) {
                eventFullDtos = events.stream()
                        .filter(event -> event.getParticipantLimit() < event.getConfirmedRequests())
                        .map(EventMapper.INSTANCE::EventToEventFullDto)
                        .collect(Collectors.toList());
            }
        }
        if (rangeStart == null && rangeEnd == null) {
            List<Event> events = eventRepository.getEventsAfterCurrentDate(text, categories, paid, pageable);
            if (onlyAvailable) {
                eventFullDtos = events.stream()
                        .filter(event -> event.getParticipantLimit() < event.getConfirmedRequests())
                        .map(EventMapper.INSTANCE::EventToEventFullDto)
                        .collect(Collectors.toList());
            }
        }

        if (!eventFullDtos.isEmpty()) {
            switch (sort) {
                case "EVENT_DATE":
                    eventFullDtos.sort(((o1, o2) -> o2.getEventDate().compareTo(o1.getEventDate())));
                    break;
                case "VIEWS":
                    eventFullDtos.sort(((o1, o2) -> o2.getViews().compareTo(o1.getViews())));
            }
        }

        statsService.saveEndpointHit(new EndpointHitDto(
                null, "ewm-service", "/events", ip, LocalDateTime.now().toString()));

        return eventFullDtos;
    }

    @Override
    public EventFullDto getEventInfo(String ip, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + eventId + " was not found", LocalDateTime.now().toString())));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new IncorrectRequestEcxeption(new ApiError(HttpStatus.FORBIDDEN.toString(),
                    "For the requested operation the conditions are not met.",
                    "Событие должно быть опубликовано",
                    LocalDateTime.now().toString()));
        }
        statsService.saveEndpointHit(new EndpointHitDto(
                null, "ewm-service", "/events", ip, LocalDateTime.now().toString()));
        return EventMapper.INSTANCE.EventToEventFullDto(event);
    }
}
