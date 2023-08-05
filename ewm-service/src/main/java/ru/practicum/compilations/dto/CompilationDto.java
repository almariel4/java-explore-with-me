package ru.practicum.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.events.dto.EventShortDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CompilationDto {

    private List<EventShortDto> events;     // Список событий входящих в подборку
    private Long id;
    private boolean pinned;     // Закреплена ли подборка на главной странице сайта
    private String title;       // Заголовок подборки
}
