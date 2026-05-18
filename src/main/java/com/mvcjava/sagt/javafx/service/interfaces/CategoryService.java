/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mvcjava.sagt.javafx.service.interfaces;

import com.mvcjava.sagt.javafx.dao.model.Category;
import com.mvcjava.sagt.javafx.exception.BusinessException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author lucas
 */
public interface CategoryService {
    Set<Category> getAll();
    Category getById(UUID id);
    void createCategory(Category category) throws BusinessException;
    void updateCategory(Category category) throws BusinessException;
    void deleteCategory(UUID id) throws BusinessException;
    void saveChanges(Set<Category> newCategories, Set<Category> updates, Set<Category> categoriesToDelete) throws BusinessException;
}
