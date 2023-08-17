package ru.practicum.evmservice.compilations.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.evmservice.compilations.dto.CompilationDto;
import ru.practicum.evmservice.compilations.dto.NewCompilationDto;
import ru.practicum.evmservice.compilations.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilations(boolean pinned, Pageable pageable);

    CompilationDto getCompilationById(long compId);

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(long compId);

    CompilationDto updateCompilation(long compId, UpdateCompilationRequest updateCompilationRequest);
}
