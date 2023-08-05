package ru.practicum.compilations.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.mapper.CompilationMapper;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ApiError;
import ru.practicum.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, Pageable pageable) {
        List<CompilationDto> compilationDtos;
        if (pinned) {
            compilationDtos = compilationRepository.getAllByPinnedIsTrue(pageable).stream()
                    .map(CompilationMapper.INSTANCE::CompilationToCompilationDto)
                    .collect(Collectors.toList());
        } else {
            compilationDtos = compilationRepository.findAll(pageable).stream()
                    .map(CompilationMapper.INSTANCE::CompilationToCompilationDto)
                    .collect(Collectors.toList());
        }
        for (CompilationDto compilationDto : compilationDtos) {
            compilationDto.setEvents(getEventList(compilationDto.getId()));
        }
        return compilationDtos;
    }

    @Override
    public CompilationDto getCompilationsById(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() ->
                        (new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                                "The required object was not found.",
                                "Event with id=" + compId + " was not found", LocalDateTime.now().toString()))));
        return CompilationMapper.INSTANCE.CompilationToCompilationDto(compilation);

    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.save(
                CompilationMapper.INSTANCE.NewCompilationDtoToCompilation(newCompilationDto));
        return CompilationMapper.INSTANCE.CompilationToCompilationDto(compilation);
    }

    @Override
    public void deleteCompilation(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() ->
                        (new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                                "The required object was not found.",
                                "Event with id=" + compId + " was not found", LocalDateTime.now().toString()))));
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto updateCompilation(long compId, NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() ->
                        (new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                                "The required object was not found.",
                                "Event with id=" + compId + " was not found", LocalDateTime.now().toString()))));
        return CompilationMapper.INSTANCE.CompilationToCompilationDto(
                compilationRepository.save(compilation));
    }

    private List<EventShortDto> getEventList(Long compId) {
        return eventRepository.getAllByCompilationId(compId).stream()
                .map(EventMapper.INSTANCE::EventToEventShortDto).collect(Collectors.toList());
    }
}
