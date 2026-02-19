/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dao.impl;

import com.mvcjava.sagt.javafx.config.DatabaseManager;
import com.mvcjava.sagt.javafx.dao.interfaces.CategoryDAO;
import com.mvcjava.sagt.javafx.dao.model.Category;
import com.mvcjava.sagt.javafx.exception.DataAccessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author lucas
 */
public class CategoryDAOImpl implements CategoryDAO{

    @Override
    public Set<Category> findAll() {
        Set<Category> categories = new HashSet<>();
        String sql = "SELECT * FROM app.categorias";
        
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Category category = new Category((UUID)rs.getObject("id"), rs.getString("nombre"));
                categories.add(category);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error al obtener todas las categorias.", ex);
        }
        
        return categories;
    }
    
}
