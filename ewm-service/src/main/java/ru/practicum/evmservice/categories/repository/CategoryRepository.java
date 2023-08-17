package ru.practicum.evmservice.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.evmservice.categories.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category getCategoriesByName(String name);
}
