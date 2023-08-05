package ru.practicum.categories.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.service.CategoriesService;

@RestController
@RequestMapping(path = "admin/categories")
@AllArgsConstructor
public class AdminCategoriesController {

    private final CategoriesService categoriesService;

    @PostMapping
    public CategoryDto addCategory(@RequestBody NewCategoryDto newCategoryDto) {
        return categoriesService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable long catId) {
        categoriesService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto changeCategory(@PathVariable long catId,
                                      @RequestBody NewCategoryDto newCategoryDto) {
        return categoriesService.changeCategory(catId, newCategoryDto);
    }


}
