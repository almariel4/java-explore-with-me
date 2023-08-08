package ru.practicum.evmservice.events.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    private String annotation;              // Новая аннотация
    private Long category;                  // Новая категория
    @Size(min = 20, max = 7000)
    private String description;             // Новое описание
    private String eventDate;               // Новые дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    @Valid
    private Location location;              // Широта и долгота места проведения события
    private Boolean paid;                   // Новое значение флага о платности мероприятия
    @PositiveOrZero
    private Long participantLimit;       // Новый лимит пользователей
    private Boolean requestModeration;      // Нужна ли премодерация заявок на участие
    private StateAction stateAction;    // Новое состояние события
    @Size(min = 3, max = 120)
    private String title;                   // Новый заголовок
}
