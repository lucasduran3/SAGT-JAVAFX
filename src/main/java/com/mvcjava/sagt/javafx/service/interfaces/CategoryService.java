/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mvcjava.sagt.javafx.service.interfaces;

import com.mvcjava.sagt.javafx.dao.model.Category;
import java.util.Set;

/**
 *
 * @author lucas
 */
public interface CategoryService {
    Set<Category> getAll();
}
