package ru.practicum.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ParticipationRequestDto {

    private String created;         // Дата и время создания заявки
    private Long event;             // Идентификатор события
    private Long id;                // Идентификатор заявки
    private Long requester;         // Идентификатор пользователя, отправившего заявку
    private String status;      // Статус заявки
}
