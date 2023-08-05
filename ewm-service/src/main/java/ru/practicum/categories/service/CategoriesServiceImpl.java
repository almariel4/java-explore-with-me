package ru.practicum.categories.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.mapper.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.exceptions.ApiError;
import ru.practicum.exceptions.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(
                CategoryMapper.INSTANCE.NewCategoryDtoToCategory(newCategoryDto));
        return CategoryMapper.INSTANCE.CategoryToCategoryDto(category);
    }

    @Override
    public void deleteCategory(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException(new ApiError()));
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDto changeCategory(Long catId, NewCategoryDto newCategoryDto) throws ConstraintViolationException {
        categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + catId + " was not found", LocalDateTime.now().toString())));
        Category category = categoryRepository.save(
                CategoryMapper.INSTANCE.NewCategoryDtoToCategory(newCategoryDto));
        return CategoryMapper.INSTANCE.CategoryToCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(CategoryMapper.INSTANCE::CategoryToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryInfo(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + catId + " was not found", LocalDateTime.now().toString())));
        return CategoryMapper.INSTANCE.CategoryToCategoryDto(category);
    }
}
