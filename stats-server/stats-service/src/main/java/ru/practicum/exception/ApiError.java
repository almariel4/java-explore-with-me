package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private String[] errors;    // Список стектрейсов или описания ошибок
    private String message;     // Сообщение об ошибке
    private String reason;      // Общее описание причины ошибки
    private String status;      // Код статуса HTTP-ответа
    private String timestamp;   // Дата и время, когда произошла ошибка (в формате "yyyy-MM-dd HH:mm:ss")
    private Long id;

    public ApiError(String status, String reason, String message, String timestamp) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }

}
