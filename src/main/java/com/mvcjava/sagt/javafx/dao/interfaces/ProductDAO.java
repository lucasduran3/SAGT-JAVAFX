/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mvcjava.sagt.javafx.dao.interfaces;

import com.mvcjava.sagt.javafx.dao.model.Product;
import java.util.Map;
import java.util.UUID;
/**
 *
 * @author lucas
 */
public interface ProductDAO {
    void addProduct(Product product);
    Product getProduct(UUID id);
    void updateProduct(UUID id, Map<String, Object> updates);
    void deleteProduct(UUID id);
    boolean alreadyExist(String name, String model, String brand);
}
