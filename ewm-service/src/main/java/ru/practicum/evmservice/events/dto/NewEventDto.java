package ru.practicum.evmservice.events.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.evmservice.events.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;              // Краткое описание события
    @NotNull
    private Long category;                  // id категории к которой относится событие
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;             // Полное описание события
    @NotNull
    private String eventDate;               // Дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    private Location location;              // Широта и долгота места проведения события
    private Boolean paid;                   // Нужно ли оплачивать участие в событии
    private Integer participantLimit;       // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private Boolean requestModeration;      // Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически
    @Size(min = 3, max = 120)
    private String title;                   // Заголовок события
}
