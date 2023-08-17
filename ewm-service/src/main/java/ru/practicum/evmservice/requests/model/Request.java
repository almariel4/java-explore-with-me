package ru.practicum.evmservice.requests.model;

import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;
import ru.practicum.evmservice.events.model.Event;
import ru.practicum.evmservice.events.model.EventStatus;
import ru.practicum.evmservice.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {

    @Id
    @ReadOnlyProperty
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // Идентификатор заявки
    private LocalDateTime created;         // Дата и время создания заявки
    @ManyToOne
    @JoinColumn(name = "event", referencedColumnName = "id")
    private Event event;             // Идентификатор события
    @ManyToOne
    @JoinColumn(name = "requester", referencedColumnName = "id")
    private User requester;         // Идентификатор пользователя, отправившего заявку
    @Enumerated(EnumType.STRING)
//    @Type(type = "ru.practicum.utils.EnumTypePostgreSql")
    private EventStatus status;      // Статус заявки
}
