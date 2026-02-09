/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dto;

import com.mvcjava.sagt.javafx.dao.model.Product;
/**
 *
 * @author lucas
 */
public class ProductWithRelations {
    private final Product product;
    private final String loadedByName;
    
    public ProductWithRelations(Product product, String loadedByName) {
        this.product = product;
        this.loadedByName = loadedByName;
    }
    
    public Product getProduct() { return product; }
    public String getLoadedByName() { return loadedByName; }
}
