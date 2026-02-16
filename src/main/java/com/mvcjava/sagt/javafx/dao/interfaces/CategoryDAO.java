/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mvcjava.sagt.javafx.dao.interfaces;

import com.mvcjava.sagt.javafx.dao.model.Category;
import java.util.List;

/**
 *
 * @author lucas
 */
public interface CategoryDAO {
    List<Category> findAll();
}
