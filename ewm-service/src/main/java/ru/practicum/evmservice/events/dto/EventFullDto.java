package ru.practicum.evmservice.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.evmservice.categories.dto.CategoryDto;
import ru.practicum.evmservice.events.model.EventState;
import ru.practicum.evmservice.events.model.Location;
import ru.practicum.evmservice.users.dto.UserShotDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {

    private String annotation;              // Краткое описание
    private CategoryDto category;           // Категория
    private Long confirmedRequests = 0L;         // Количество одобренных заявок на участие в данном событии
    private String createdOn;               // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
    private String description;             // Полное описание события
    private String eventDate;               // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    private Long id;
    private UserShotDto initiator;          // Пользователь (краткая информация)
    private Location location;              // Широта и долгота места проведения события
    private boolean paid;                   // Нужно ли оплачивать участие
    private Long participantLimit;          // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private String publishedOn;             // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
    private boolean requestModeration;      // Нужна ли пре-модерация заявок на участие
    private EventState state;               // Список состояний жизненного цикла события
    private String title;                   // Заголовок
    private Long views;                     // Количество просмотров события
}
