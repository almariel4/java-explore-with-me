package ru.practicum.evmservice.compilations.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.practicum.evmservice.compilations.dto.CompilationDto;
import ru.practicum.evmservice.compilations.dto.NewCompilationDto;
import ru.practicum.evmservice.compilations.model.Compilation;

@Component
@Mapper(componentModel = "spring")
public interface CompilationMapper {

    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    CompilationDto compilationToCompilationDto(Compilation compilation);

    @Mapping(target = "events", ignore = true)
    Compilation newCompilationDtoToCompilation(NewCompilationDto newCompilationDto);

}
