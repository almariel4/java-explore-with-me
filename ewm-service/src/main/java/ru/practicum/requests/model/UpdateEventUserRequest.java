package ru.practicum.requests.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.events.model.Location;
import ru.practicum.events.model.StateAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {

    private String annotation;              // Новая аннотация
    private Long category;                  // Новая категория
    private String description;             // Новое описание
    private String eventDate;               // Новые дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    private Location location;              // Широта и долгота места проведения события
    private boolean paid;                   // Широта и долгота места проведения события
    private Integer participantLimit;       // Новый лимит пользователей
    private boolean requestModeration;      // Нужна ли пре-модерация заявок на участие
    private StateAction stateAction;        // Изменение сотояния события
    private String title;                   // Новый заголовок
}
