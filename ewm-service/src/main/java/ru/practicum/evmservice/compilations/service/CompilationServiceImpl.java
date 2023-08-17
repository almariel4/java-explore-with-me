package ru.practicum.evmservice.compilations.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.evmservice.compilations.dto.CompilationDto;
import ru.practicum.evmservice.compilations.dto.NewCompilationDto;
import ru.practicum.evmservice.compilations.dto.UpdateCompilationRequest;
import ru.practicum.evmservice.compilations.mapper.CompilationMapper;
import ru.practicum.evmservice.compilations.model.Compilation;
import ru.practicum.evmservice.compilations.repository.CompilationRepository;
import ru.practicum.evmservice.events.model.Event;
import ru.practicum.evmservice.events.repository.EventRepository;
import ru.practicum.evmservice.exceptions.ApiError;
import ru.practicum.evmservice.exceptions.IncorrectRequestEcxeption;
import ru.practicum.evmservice.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, Pageable pageable) {
        List<CompilationDto> compilationDtos;
        compilationDtos = compilationRepository.getAllByPinned(pinned, pageable).stream()
                .map(CompilationMapper.INSTANCE::compilationToCompilationDto)
                .collect(Collectors.toList());
        return compilationDtos;
    }

    @Override
    public CompilationDto getCompilationById(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() ->
                        new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                                "The required object was not found.",
                                "Event with id=" + compId + " was not found", LocalDateTime.now().toString())));
        return CompilationMapper.INSTANCE.compilationToCompilationDto(compilation);
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>();
        if (newCompilationDto.getEvents() != null) {
            events = new HashSet<>(eventRepository.getAllByIdIn(newCompilationDto.getEvents()));
        }
        Compilation compilation = CompilationMapper.INSTANCE.newCompilationDtoToCompilation(newCompilationDto);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
        return CompilationMapper.INSTANCE.compilationToCompilationDto(compilation);
    }

    @Override
    public void deleteCompilation(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() ->
                        (new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                                "The required object was not found.",
                                "Event with id=" + compId + " was not found", LocalDateTime.now().toString()))));
        if (!compilation.getEvents().isEmpty()) {
            throw new IncorrectRequestEcxeption(new ApiError(HttpStatus.CONFLICT.toString(),
                    "Нельзя удалить подборку с привязанными событиями",
                    "Нельзя удалить подборку с привязанными событиями", LocalDateTime.now().toString()));
        }
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto updateCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() ->
                        (new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                                "The required object was not found.",
                                "Event with id=" + compId + " was not found", LocalDateTime.now().toString()))));
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.isPinned() != compilation.isPinned()) {
            compilation.setPinned(updateCompilationRequest.isPinned());
        }
        if (updateCompilationRequest.getEvents() != null) {
            Set<Event> events = new HashSet<>(eventRepository.getAllByIdIn(updateCompilationRequest.getEvents()));
            compilation.setEvents(events);
        }
        return CompilationMapper.INSTANCE.compilationToCompilationDto(compilationRepository.save(compilation));
    }
}
