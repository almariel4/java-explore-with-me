package ru.practicum.events.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.lang.Nullable;
import ru.practicum.categories.model.Category;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.users.model.User;

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
    private Category category;           // Категория
    private Long confirmedRequests;         // Количество одобренных заявок на участие в данном событии
    private LocalDateTime createdOn;               // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "TEXT", length = 1000)
    private String description;             // Полное описание события
    private LocalDateTime eventDate;               // Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    @ManyToOne
    private User initiator;              // Пользователь (краткая информация)
    @OneToOne(cascade = CascadeType.ALL)
    private Location location;              // Широта и долгота места проведения события
    private boolean paid;                   // Нужно ли оплачивать участие
    private Long participantLimit;          // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private LocalDateTime publishedOn;      // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
    private boolean requestModeration;      // Нужна ли пре-модерация заявок на участие
    private EventState state;               // Список состояний жизненного цикла события
    private String title;                   // Заголовок
    private Long views;                     // Количество просмотрев события
    @ManyToOne
    @JsonIgnore
    @Nullable
    private Compilation compilation;        // Подборка, к которой принадлежит событие
}
