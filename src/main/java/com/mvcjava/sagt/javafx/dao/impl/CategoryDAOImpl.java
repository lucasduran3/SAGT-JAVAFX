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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author lucas
 */
public class CategoryDAOImpl implements CategoryDAO {

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

    @Override
    public Category findById(UUID id) {
        Category category = new Category();
        String sql = "SELECT * FROM app.categorias WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    category.setId((UUID)rs.getObject("id"));
                    category.setName(rs.getString("nombre"));
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error al obtener categoría con id: " + id.toString(), ex);
        }
        
        return category;
    }

    @Override
    public void updateCategory(Category category) {
        if (category == null) return;        
        
        String sql = "UPDATE app.categorias SET nombre = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            stmt.setObject(1, category.getName());
            stmt.setObject(2, category.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error al actualizar categoría: " + category.getId(), ex);
        }
    }

    @Override
    public void addCategory(Category category) {
        String sql = "INSERT INTO app.categorias (nombre) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, category.getName());
        } catch (SQLException ex) {
            throw new DataAccessException("Error al añadir categoria: " + category.getName(), ex);
        }
    }

    @Override
    public void deleteCategory(UUID id) {
        String sql = "DELETE FROM app.categorias WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error al eliminar categoría con id: " + id, ex);
        }
    }

    @Override
    public boolean alreadyExists(UUID id, String name) {
        String sql = "SELECT 1 FROM app.categorias WHERE nombre = ? AND id <> ? LIMIT 1";
        
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setObject(2, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error al verificar existencia de categoría: " + id, ex);
        }
    }
    
    
    
}
