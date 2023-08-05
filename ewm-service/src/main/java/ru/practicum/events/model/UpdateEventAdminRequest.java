package ru.practicum.events.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventAdminRequest {

    private String annotation;              // Новая аннотация
    private Long category;                  // Новая категория
    private String description;             // Новое описание
    private String eventDate;               // Новые дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    private Location location;              // Широта и долгота места проведения события
    private boolean paid;                   // Новое значение флага о платности мероприятия
    private Long participantLimit;       // Новый лимит пользователей
    private boolean requestModeration;      // Нужна ли премодерация заявок на участие
    private EventStateAdmin stateAction;    // Новое состояние события
    private String title;                   // Новый заголовок
}
