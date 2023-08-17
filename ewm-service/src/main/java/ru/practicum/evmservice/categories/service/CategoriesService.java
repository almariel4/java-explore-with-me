package ru.practicum.evmservice.categories.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.evmservice.categories.dto.CategoryDto;
import ru.practicum.evmservice.categories.dto.NewCategoryDto;

import java.util.List;

public interface CategoriesService {
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);

    CategoryDto changeCategory(Long catId, NewCategoryDto newCategoryDto);

    List<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategoryInfo(Long catId);
}
