package ru.practicum.evmservice.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    private List<Long> events;      // Список идентификаторов событий, входящих в подборку
    @NotNull
    private Boolean pinned = false;         // Закреплена ли подборка на главной странице сайта
    @NotBlank
    @Size(max = 50)
    private String title;           // Заголовок подборки
}
