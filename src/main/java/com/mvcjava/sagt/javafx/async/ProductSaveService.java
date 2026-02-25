/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.async;

import com.mvcjava.sagt.javafx.dao.model.Product;
import com.mvcjava.sagt.javafx.service.interfaces.ProductService;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author lucas
 */
public class ProductSaveService extends Service<Void> {
    private final ProductService productService;
    
    private Map<Product, Set<UUID>> newProducts;
    private Map<UUID, Map<String, Object>> productsToUpdate;
    private Map<UUID, Set<UUID>> categoriesToUpdate;
    private Set<UUID> productsToDelete;

    public ProductSaveService(ProductService productService) {
        this.productService = productService;
    }
    
    @Override
    protected Task<Void> createTask() {
        return new Task() {
            @Override
            protected Void call() throws Exception {
                productService.saveChanges(newProducts, productsToUpdate, categoriesToUpdate, productsToDelete);
                return null;
            }
        };
    }
    
    public void setDataToSave(
            Map<Product,Set<UUID>> newProducts,
            Map<UUID, Map<String, Object>> productsToUpdate,
            Map<UUID, Set<UUID>> categoriesToUpdate,
            Set<UUID> productsToDelete) {
        
        this.newProducts = newProducts;
        this.productsToUpdate = productsToUpdate;
        this.categoriesToUpdate = categoriesToUpdate;
        this.productsToDelete = productsToDelete;
    }
}
