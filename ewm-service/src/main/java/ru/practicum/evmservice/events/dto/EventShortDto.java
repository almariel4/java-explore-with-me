package ru.practicum.evmservice.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.evmservice.categories.dto.CategoryDto;
import ru.practicum.evmservice.users.dto.UserShotDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {

    private String annotation;          // Краткое описание
    private CategoryDto category;       // Категория
    private Long confirmedRequests;     // Количество одобренных заявок на участие в данном событии
    private String eventDate;           // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    private Long id;
    private UserShotDto initiator;      // Пользователь (краткая информация)
    private boolean paid;               // Нужно ли оплачивать участие
    private String title;               // Заголовок
    private Long views;                 // Количество просмотров события
}
