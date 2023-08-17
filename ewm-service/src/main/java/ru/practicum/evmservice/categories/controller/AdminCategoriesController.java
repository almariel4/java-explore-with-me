package ru.practicum.evmservice.categories.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evmservice.categories.dto.CategoryDto;
import ru.practicum.evmservice.categories.dto.NewCategoryDto;
import ru.practicum.evmservice.categories.service.CategoriesService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "admin/categories")
@AllArgsConstructor
public class AdminCategoriesController {

    private final CategoriesService categoriesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoriesService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        categoriesService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto changeCategory(@PathVariable long catId,
                                      @Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoriesService.changeCategory(catId, newCategoryDto);
    }


}
