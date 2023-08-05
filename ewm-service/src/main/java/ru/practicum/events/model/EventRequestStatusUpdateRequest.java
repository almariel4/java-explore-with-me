package ru.practicum.events.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;      // Идентификаторы запросов на участие в событии текущего пользователя
    private EventStatus status;         // Новый статус запроса на участие в событии текущего пользователя
}
