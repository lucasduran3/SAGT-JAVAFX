/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.async;

import com.mvcjava.sagt.javafx.dao.model.Category;
import com.mvcjava.sagt.javafx.service.impl.CategoryServiceImpl;
import com.mvcjava.sagt.javafx.service.interfaces.CategoryService;
import java.util.Set;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author lucas
 */
public class CategorySaveService extends Service<Void> {
    private final CategoryService categoryService;
    
    private Set<Category> newCategories;
    private Set<Category> updates;
    private Set<Category> categoriesToDelete;
    
    public CategorySaveService() {
        this.categoryService = new CategoryServiceImpl();
    }
    
    public void setData(Set<Category> newCategories, Set<Category> updates, Set<Category> categoriesToDelete) {
        this.newCategories = newCategories;
        this.updates = updates;
        this.categoriesToDelete = categoriesToDelete;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task() {
            @Override
            protected Void call() throws Exception {
                categoryService.saveChanges(newCategories, updates, categoriesToDelete);
                return null;
            }
        };
    }
}
