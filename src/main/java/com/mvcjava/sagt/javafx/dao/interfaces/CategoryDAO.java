/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mvcjava.sagt.javafx.dao.interfaces;

import com.mvcjava.sagt.javafx.dao.model.Category;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author lucas
 */
public interface CategoryDAO {
    Category findById(UUID id);
    Set<Category> findAll();
    void addCategory(Category category);
    void updateCategory(UUID id, Map<String, Object> updates);
    void deleteCategory(UUID id);
    boolean alreadyExists(UUID id, String name);
}
