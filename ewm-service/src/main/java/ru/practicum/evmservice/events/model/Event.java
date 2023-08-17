package ru.practicum.evmservice.events.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.ReadOnlyProperty;
import ru.practicum.evmservice.categories.model.Category;
import ru.practicum.evmservice.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @ReadOnlyProperty
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT", length = 500)
    private String annotation;              // Краткое описание
    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "id")
    private Category category;           // Категория
    @Column(name = "confirmed_requests")
    private Long confirmedRequests = 0L;         // Количество одобренных заявок на участие в данном событии
    @Column(name = "created_on")
    private LocalDateTime createdOn;               // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "TEXT", length = 1000)
    private String description;             // Полное описание события
    @Column(name = "event_date")
    private LocalDateTime eventDate;               // Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    @ManyToOne
    @JoinColumn(name = "initiator", referencedColumnName = "id")
    private User initiator;              // Пользователь (краткая информация)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location", referencedColumnName = "id")
    private Location location;              // Широта и долгота места проведения события
    private boolean paid;                   // Нужно ли оплачивать участие
    @Column(name = "participant_limit", columnDefinition = "bigint default 0")
    private Long participantLimit;          // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    @Column(name = "published_on")
    private LocalDateTime publishedOn;      // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
    @Column(name = "request_moderation", columnDefinition = "boolean default true")
    private Boolean requestModeration;      // Нужна ли пре-модерация заявок на участие
    @Enumerated(EnumType.STRING)
    private EventState state;               // Список состояний жизненного цикла события
    private String title;                   // Заголовок
    private Long views;                     // Количество просмотрев события
}
