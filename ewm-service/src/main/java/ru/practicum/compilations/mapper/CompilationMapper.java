package ru.practicum.compilations.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.model.Compilation;

@Component
@Mapper(componentModel = "spring")
public interface CompilationMapper {

    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    CompilationDto CompilationToCompilationDto(Compilation compilation);

    Compilation NewCompilationDtoToCompilation(NewCompilationDto newCompilationDto);

}
