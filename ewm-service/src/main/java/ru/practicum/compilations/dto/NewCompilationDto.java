package ru.practicum.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    private List<Long> events;      // Список идентификаторов событий, входящих в подборку
    private boolean pinned;         // Закреплена ли подборка на главной странице сайта
    private String title;           // Заголовок подборки
}
