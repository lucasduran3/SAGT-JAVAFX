/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dao.impl;

import com.mvcjava.sagt.javafx.config.DatabaseManager;
import com.mvcjava.sagt.javafx.dao.interfaces.ProductDAO;
import com.mvcjava.sagt.javafx.dao.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author lucas
 */
public class ProductDAOImpl implements ProductDAO{
    
    @Override
        public void addProduct(Product product) {
        String sql = "INSERT INTO app.productos "
                + "(nombre,"
                + " marca,"
                + " modelo,"
                + " precio_compra,"
                + " precio_venta,"
                + " stock,"
                + " stock_minimo,"
                + " id_proveedor,"
                + " cargado_por) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getBrand());
            stmt.setString(3, product.getModel());
            stmt.setFloat(4, product.getPurchasePrice());
            stmt.setFloat(5, product.getSalePrice());
            stmt.setInt(6, product.getStock());
            stmt.setInt(7, product.getMinStock());
            stmt.setObject(8, product.getIdSupplier());
            stmt.setObject(9, product.getLoadedBy());
            stmt.executeUpdate();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Product getProduct(UUID id) {
        Product product = null;
        String sql = "SELECT * FROM app.productos WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection(); 
                PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    product = new Product();
                    product.setId((UUID)rs.getObject("id"));
                    product.setName(rs.getString("nombre"));
                    product.setBrand(rs.getString("marca"));
                    product.setModel(rs.getString("modelo"));
                    product.setPurchasePrice(rs.getFloat("precio_compra"));
                    product.setSalePrice(rs.getFloat("precio_venta"));
                    product.setStock(rs.getInt("stock"));
                    product.setMinStock(rs.getInt("stock_minimo"));
                    product.setIdSupplier((UUID)rs.getObject("id_proveedor"));
                    product.setLoadedBy((UUID)rs.getObject("cargado_por"));
                    product.setEntryDate(rs.getTimestamp("fecha_ingreso"));
                    product.setEntryDate(rs.getTimestamp("fecha_actualizacion"));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return product; 
    }

    @Override
    public void updateProduct(UUID id, Map<String, Object> updates) {
        if (updates == null || updates.isEmpty()) {
            return;
        }
        
        StringBuilder sql = new StringBuilder("UPDATE app.productos SET");
        
        int idx = 0;
        for (Map.Entry<String, Object> e : updates.entrySet()) {
            if (idx++ > 0) sql.append(", ");
            sql.append(e.getKey()).append(" = ?");
        }
        sql.append(" WHERE id = ?");
        
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql.toString()))
        {
            int i = 1;
            for (Object p : updates.values()) {
                if (p == null) {
                } else if (p instanceof String) {
                    stmt.setString(i++, (String) p);
                } else if (p instanceof Integer) {
                    stmt.setInt(i++, (Integer) p);
                } else if (p instanceof Float) {
                    stmt.setFloat(i++, (Float) p);
                } else if (p instanceof UUID) {
                    stmt.setObject(i++, (UUID) p);
                } else if (p instanceof Timestamp) {
                    stmt.setTimestamp(i++, (Timestamp) p);
                }
            }
            stmt.setObject(i, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(UUID id) {
        String sql = "DELETE FROM app.productos WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean alreadyExist(String name, String model, String brand) {
        String sql = "SELECT 1 FROM app.productos WHERE nombre = ? AND marca = ? AND modelo = ? LIMIT 1";
        
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
            stmt.setString(1, name);
            stmt.setString(2, brand);
            stmt.setString(3, model);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            throw new RuntimeException();
        }
    }
}
