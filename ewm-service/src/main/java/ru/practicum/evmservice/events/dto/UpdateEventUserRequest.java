package ru.practicum.evmservice.events.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.evmservice.events.model.Location;
import ru.practicum.evmservice.events.model.StateAction;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {

    @Size(min = 20, max = 2000)
    private String annotation;              // Новая аннотация
    private Long category;                  // Новая категория
    @Size(min = 20, max = 7000)
    private String description;             // Новое описание
    private String eventDate;               // Новые дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    private Location location;              // Широта и долгота места проведения события
    private boolean paid;                   // Широта и долгота места проведения события
    @PositiveOrZero
    private Integer participantLimit;       // Новый лимит пользователей
    private boolean requestModeration;      // Нужна ли пре-модерация заявок на участие
    private StateAction stateAction;        // Изменение сотояния события
    @Size(min = 3, max = 120)
    private String title;                   // Новый заголовок
}
