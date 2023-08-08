package ru.practicum.evmservice.categories.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.evmservice.categories.dto.CategoryDto;
import ru.practicum.evmservice.categories.dto.NewCategoryDto;
import ru.practicum.evmservice.categories.mapper.CategoryMapper;
import ru.practicum.evmservice.categories.model.Category;
import ru.practicum.evmservice.categories.repository.CategoryRepository;
import ru.practicum.evmservice.events.model.Event;
import ru.practicum.evmservice.events.repository.EventRepository;
import ru.practicum.evmservice.exceptions.ApiError;
import ru.practicum.evmservice.exceptions.IncorrectRequestEcxeption;
import ru.practicum.evmservice.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.getCategoriesByName(newCategoryDto.getName());
        if (category != null) {
            throw new IncorrectRequestEcxeption(new ApiError(HttpStatus.FORBIDDEN.toString(),
                    "For the requested operation the conditions are not met.",
                    "Категория с таким именем уже существует",
                    LocalDateTime.now().format(formatter)));
        }
        return CategoryMapper.INSTANCE.categoryToCategoryDto((categoryRepository.save(
                CategoryMapper.INSTANCE.newCategoryDtoToCategory(newCategoryDto))));
    }

    @Override
    public void deleteCategory(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException(new ApiError()));
        List<Event> events = eventRepository.getEventByCategory(category);
        if (!events.isEmpty()) {
            throw new IncorrectRequestEcxeption(new ApiError(HttpStatus.CONFLICT.toString(),
                    "Категория привязана к событию",
                    "Категория привязана к событию", LocalDateTime.now().toString()));
        }
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDto changeCategory(Long catId, NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + catId + " was not found", LocalDateTime.now().toString())));
        category.setName(newCategoryDto.getName());
        Category testedCat = categoryRepository.getCategoriesByName(category.getName());
        if (testedCat != null) {
            if (category.getName().equals(testedCat.getName()) &&
                    !category.getId().equals(testedCat.getId())) {
                throw new IncorrectRequestEcxeption(new ApiError(HttpStatus.FORBIDDEN.toString(),
                        "For the requested operation the conditions are not met.",
                        "Категория с таким именем уже существует",
                        LocalDateTime.now().format(formatter)));
            }
        }
        return CategoryMapper.INSTANCE.categoryToCategoryDto(categoryRepository.save((category)));
    }

    @Override
    public List<CategoryDto> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(CategoryMapper.INSTANCE::categoryToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryInfo(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException(new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Event with id=" + catId + " was not found", LocalDateTime.now().toString())));
        return CategoryMapper.INSTANCE.categoryToCategoryDto(category);
    }
}
