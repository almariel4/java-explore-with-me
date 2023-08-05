package ru.practicum.compilations.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.service.CompilationService;
import ru.practicum.utils.PageRequestUtil;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@AllArgsConstructor
public class PublicCompilationsController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequestUtil.createPageRequest(from, size);
        return compilationService.getCompilations(pinned, pageable);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationsById(@PathVariable long compId) {
        return compilationService.getCompilationsById(compId);
    }
}
