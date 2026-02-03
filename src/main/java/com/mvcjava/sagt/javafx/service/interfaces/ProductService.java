/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mvcjava.sagt.javafx.service.interfaces;

import java.util.Map;
import java.util.UUID;
import com.mvcjava.sagt.javafx.dao.model.Product;
/**
 *
 * @author lucas
 */
public interface ProductService { 
    void createProduct(Product product) throws Exception;
    Product getProduct(UUID id);
    void updateProduct(UUID id, Map<String, Object> updates) throws Exception;
    void deleteProduct(UUID id) throws Exception;
}
