package ru.practicum.events.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.events.model.Location;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    private String annotation;              // Краткое описание события
    private Long category;                  // id категории к которой относится событие
    private String description;             // Полное описание события
    private String eventDate;               // Дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    private Location location;              // Широта и долгота места проведения события
    private boolean paid;                   // Нужно ли оплачивать участие в событии
    private Integer participantLimit;       // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private boolean requestModeration;      // Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически
    private String title;                   // Заголовок события
}
