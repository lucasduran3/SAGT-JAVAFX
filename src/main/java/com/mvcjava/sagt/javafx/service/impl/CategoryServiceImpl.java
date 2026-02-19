/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.service.impl;

import com.mvcjava.sagt.javafx.dao.impl.CategoryDAOImpl;
import com.mvcjava.sagt.javafx.dao.interfaces.CategoryDAO;
import com.mvcjava.sagt.javafx.dao.model.Category;
import com.mvcjava.sagt.javafx.service.interfaces.CategoryService;
import java.util.Set;

/**
 *
 * @author lucas
 */
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDAO categoryDAO;
    
    public CategoryServiceImpl() {
        this.categoryDAO = new CategoryDAOImpl();
    }

    @Override
    public Set<Category> getAll() {
        return this.categoryDAO.findAll();
    }
    
}
