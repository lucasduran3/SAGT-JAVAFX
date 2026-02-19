/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mvcjava.sagt.javafx.service.interfaces;

import java.util.Map;
import java.util.UUID;
import com.mvcjava.sagt.javafx.dao.model.Product;
import com.mvcjava.sagt.javafx.dto.ProductWithRelations;
import com.mvcjava.sagt.javafx.exception.BusinessException;
import java.util.List;
import java.util.Set;
/**
 *
 * @author lucas
 */
public interface ProductService { 
    void createProduct(Product product) throws BusinessException;
    void createProductWithCategories(Product product, Set<UUID> categoryIds) throws BusinessException;
    Product getProduct(UUID id);
    List<Product> getAll();
    List<ProductWithRelations> getAllWithRelations();
    void updateProduct(UUID id, Map<String, Object> updates) throws BusinessException;
    void updateProductCategories(Map<UUID, Set<UUID>> updates) throws BusinessException;    
    void deleteProduct(UUID id) throws BusinessException;
}
