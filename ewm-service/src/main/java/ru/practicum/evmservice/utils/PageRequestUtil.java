package ru.practicum.evmservice.utils;

import org.springframework.data.domain.PageRequest;

public class PageRequestUtil {

    public static PageRequest createPageRequest(Integer from, Integer size) {
        PageRequest pageRequest;
        if (from < 0 || size < 0) {
            throw new IllegalArgumentException("Индекс первого элемента и количество элементов не могут быть отрицательными");
        }
        if (from == 0 && size == 0) {
            throw new IllegalArgumentException("Нечего возвращать");
        }
        int pageNumber = from / size;
        pageRequest = PageRequest.of(pageNumber, Math.toIntExact(size));
        return pageRequest;
    }
}
