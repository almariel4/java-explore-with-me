package ru.practicum.evmservice.categories.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evmservice.categories.dto.CategoryDto;
import ru.practicum.evmservice.categories.service.CategoriesService;
import ru.practicum.evmservice.utils.PageRequestUtil;

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
    public CategoryDto getCategoryInfo(@PathVariable long catId) {
        return categoriesService.getCategoryInfo(catId);
    }
}
