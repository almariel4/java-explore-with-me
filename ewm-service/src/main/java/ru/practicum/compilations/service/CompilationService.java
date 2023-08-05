package ru.practicum.compilations.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilations(boolean pinned, Pageable pageable);

    CompilationDto getCompilationsById(long compId);

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(long compId);

    CompilationDto updateCompilation(long compId, NewCompilationDto newCompilationDto);
}
