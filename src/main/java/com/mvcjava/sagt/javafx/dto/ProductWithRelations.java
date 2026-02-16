/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dto;

import com.mvcjava.sagt.javafx.dao.model.Category;
import com.mvcjava.sagt.javafx.dao.model.Product;
import java.util.List;
/**
 *
 * @author lucas
 */
public class ProductWithRelations {
    private final Product product;
    private final String loadedByName;
    private final List<Category> categories;
    
    public ProductWithRelations(Product product, String loadedByName, List<Category> categories) {
        this.product = product;
        this.loadedByName = loadedByName;
        this.categories = categories;
    }
    
    public Product getProduct() { return product; }
    public String getLoadedByName() { return loadedByName; }
    public List<Category> getCategories() { return categories; }
}
