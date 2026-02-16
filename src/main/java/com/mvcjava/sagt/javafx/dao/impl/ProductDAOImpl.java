/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dao.impl;

import com.mvcjava.sagt.javafx.config.DatabaseManager;
import com.mvcjava.sagt.javafx.dao.interfaces.ProductDAO;
import com.mvcjava.sagt.javafx.dao.model.Category;
import com.mvcjava.sagt.javafx.dao.model.Product;
import com.mvcjava.sagt.javafx.dto.ProductWithRelations;
import com.mvcjava.sagt.javafx.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author lucas
 */
public class ProductDAOImpl implements ProductDAO {
    
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
        } catch (SQLException ex) {
            throw new DataAccessException("Error al guardar el producto: " + product.getName(), ex);
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
                    product = mapResultSetToProduct(rs);
                }
            }

        } catch (SQLException ex) {
            throw new DataAccessException("Error al obtener el producto con id: " + id.toString(), ex);
        }
        return product; 
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<Product>();       
        String sql = "SELECT * FROM app.productos";
        
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
            
        } catch (SQLException ex) {
            throw new DataAccessException("Error obtener productos.", ex);
        }
        
        return products;
    }

    @Override
    public List<ProductWithRelations> findAllWithRelations() {
        List<ProductWithRelations> results = new ArrayList();
        String sql = "SELECT "
                + "p.id, p.nombre, p.marca, p.modelo, p.precio_compra, "
                + "p.precio_venta, p.stock, p.stock_minimo, p.id_proveedor, "
                + "p.cargado_por, p.fecha_ingreso, p.fecha_actualizacion, "
                + "u.nombre || ' ' || u.apellido AS u_nombre, "
                + "ARRAY_AGG(c.id ORDER BY c.nombre) AS categorias_id, "
                + "ARRAY_AGG(c.nombre ORDER BY c.nombre) AS categorias_nombre "
                + "FROM app.productos p "
                + "LEFT JOIN app.perfiles u ON p.cargado_por = u.id "
                + "LEFT JOIN app.productos_categorias pc ON p.id = pc.id_producto "
                + "LEFT JOIN app.categorias c ON pc.id_categoria = c.id "
                + "GROUP BY "
                + "p.id, p.nombre, p.marca, p.modelo, p.precio_compra, p.precio_venta, "
                + "p.stock, p.stock_minimo, p.id_proveedor, p.cargado_por, p.fecha_ingreso, "
                + "p.fecha_actualizacion, u.nombre, u.apellido "
                + "ORDER BY p.nombre";
        
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Product product = mapResultSetToProduct(rs);
                
                String loadedByName = rs.getString("u_nombre");
                
                List<Category> categories = extractCategoriesFromResultSet(rs);
                
                ProductWithRelations dto = new ProductWithRelations(product, loadedByName != null ? loadedByName : "Desconocido", categories);
                
                results.add(dto);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error al obtener productos con relaciones. ", ex);
        }
        
        return results;
    }

    @Override
    public void updateProduct(UUID id, Map<String, Object> updates) {
        if (updates == null || updates.isEmpty()) {
            return;
        }
        
        //Limpiar valores nulos del mapa (no se admiten nulos)
        updates.values().removeIf(t -> t == null);
        
        if (updates.isEmpty()) return;
        
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
                stmt.setObject(i++, p);
            }
            stmt.setObject(i, id);
            
            stmt.executeUpdate();
            
        } catch (SQLException ex) {
            throw new DataAccessException("Error al actualizar el producto con id: " + id.toString(), ex);
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
            throw new DataAccessException("Error al eliminar el producto con id: " + id.toString(), ex);
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
            throw new DataAccessException("Error al verificar existencia del producto: " + name + " " + model + " " + brand, ex);
        }
    }
    
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        
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
        product.setUpdateDate(rs.getTimestamp("fecha_actualizacion"));
        
        return product;
    }
    
    private List<Category> extractCategoriesFromResultSet(ResultSet rs) throws SQLException {
        List<Category> categories = new ArrayList<>();
        
        UUID[] ids = (UUID[]) rs.getArray("categorias_id").getArray();
        String[] names = (String[]) rs.getArray("categorias_nombre").getArray();
        
        if (ids == null || names == null) {
            return categories;
        }
        
        if (ids.length != names.length) {
            throw new SQLException("Inconsistencia en arrays de categorias.");
        }
        
        for (int i = 0; i < ids.length; i++) {
            Category category = new Category(ids[i], names[i]);
            categories.add(category);
        }
        
        return categories;
    }
}
