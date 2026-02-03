/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.service.impl;

import com.mvcjava.sagt.javafx.dao.interfaces.ProductDAO;
import com.mvcjava.sagt.javafx.dao.model.Product;
import com.mvcjava.sagt.javafx.service.interfaces.ProductService;
import java.util.Map;
import java.util.UUID;
/**
 *
 * @author lucas
 */
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;
    
    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public void createProduct(Product product) throws Exception{
        boolean exist = productDAO.alreadyExist(product.getName(), product.getModel(), product.getBrand());
        if (exist) {
            throw new Exception("Este producto ya existe en la base de datos");
        }
            
        if (product.getPurchasePrice() > product.getSalePrice()) {
            throw new Exception("El precio de venta debe ser mayor al precio de compra");
        }
       
        productDAO.addProduct(product);
    }

    @Override
    public Product getProduct(UUID id) {
        return productDAO.getProduct(id);
    }

    @Override
    public void updateProduct(UUID id, Map<String, Object> updates) throws Exception{
        Product currentProduct = productDAO.getProduct(id);
        if (currentProduct == null) {
            throw new Exception("El producto que intenta actualizar no existe.");
        }
        
        String newName = updates.containsKey("nombre") ? (String) updates.get("nombre")
                : currentProduct.getName();
        String newBrand = updates.containsKey("marca") ? (String) updates.get("marca")
                : currentProduct.getBrand();
        String newModel = updates.containsKey("modelo") ? (String) updates.get("modelo")
                : currentProduct.getModel();
        
        boolean dataChanged = updates.containsKey("nombre") ||
                updates.containsKey("modelo") ||
                updates.containsKey("marca");
        
        if (dataChanged) {
            boolean exists = productDAO.alreadyExist(newName, newModel, newBrand);          
            if (exists) {
                throw new Exception("Ya existe otro producto con el mismo nombre, marca y modelo.");
            }
        }
        
        if (updates.containsKey(("precio_compra")) || updates.containsKey("precio_venta")) {
            float newPurchasePrice = updates.containsKey("precio_compra") ? ((Float) updates.get("precio_compra")) : currentProduct.getPurchasePrice();
            float newSalePrice = updates.containsKey("precio_venta") ? ((Float) updates.get("precio_venta")) : currentProduct.getSalePrice();
            
            if (newPurchasePrice > newSalePrice) {
                throw new Exception("El precio de compra no puede ser mayor al precio de venta.");
            }
        }
        
        productDAO.updateProduct(id, updates);
    }

    @Override
    public void deleteProduct(UUID id) throws Exception{
        Product currentProduct = productDAO.getProduct(id);
        if (currentProduct == null) {
            throw new Exception("El producto que quieres eliminar no existe.");
        }
        productDAO.deleteProduct(id);
    }
    
}
