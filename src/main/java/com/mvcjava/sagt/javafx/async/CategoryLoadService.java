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
public class CategoryLoadService extends Service<Set<Category>>{
    private final CategoryService categoryService;
    
    public CategoryLoadService() {
        this.categoryService = new CategoryServiceImpl();
    }

    @Override
    protected Task<Set<Category>> createTask() {
        return new Task() {
            @Override
            protected Set<Category> call() throws Exception {
                return categoryService.getAll();
            }
        };
    }
}
