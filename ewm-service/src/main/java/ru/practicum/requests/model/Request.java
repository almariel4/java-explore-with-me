package ru.practicum.requests.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @Id
    @ReadOnlyProperty
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // Идентификатор заявки
    private String created;         // Дата и время создания заявки
    private Long event;             // Идентификатор события
    private Long requester;         // Идентификатор пользователя, отправившего заявку
    private String status;      // Статус заявки
}
