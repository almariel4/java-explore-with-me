package ru.practicum.evmservice.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequest {

    private List<Long> events;      // Список id событий подборки для полной замены текущего списка
    private boolean pinned;         // Закреплена ли подборка на главной странице сайта
    @Size(max = 50)
    private String title;           // Заголовок подборки
}
