package ru.practicum.evmservice.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.evmservice.events.model.EventStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ParticipationRequestDto {

    private Long id;                // Идентификатор заявки
    private String created;         // Дата и время создания заявки
    private Long event;             // Идентификатор события
    private Long requester;         // Идентификатор пользователя, отправившего заявку
    private EventStatus status;      // Статус заявки
}
