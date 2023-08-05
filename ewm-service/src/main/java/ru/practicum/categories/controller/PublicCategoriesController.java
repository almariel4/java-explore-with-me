package ru.practicum.categories.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.CategoriesService;
import ru.practicum.utils.PageRequestUtil;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@AllArgsConstructor
public class PublicCategoriesController {

    private final CategoriesService categoriesService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(value = "from", defaultValue = "0") Integer from,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequestUtil.createPageRequest(from, size);
        return categoriesService.getCategories(pageable);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryInfo(@RequestParam long catId) {
        return categoriesService.getCategoryInfo(catId);
    }
}
