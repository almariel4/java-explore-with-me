package ru.practicum.evmservice.categories.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.practicum.evmservice.categories.dto.CategoryDto;
import ru.practicum.evmservice.categories.dto.NewCategoryDto;
import ru.practicum.evmservice.categories.model.Category;

@Component
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto categoryToCategoryDto(Category category);

    Category newCategoryDtoToCategory(NewCategoryDto newCategoryDto);
}
