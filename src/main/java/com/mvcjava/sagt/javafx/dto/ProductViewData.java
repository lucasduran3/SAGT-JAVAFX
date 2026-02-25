/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dto;

import com.mvcjava.sagt.javafx.dao.model.Category;
import com.mvcjava.sagt.javafx.dao.model.Product;
import com.mvcjava.sagt.javafx.dao.model.Supplier;
import java.util.List;
import java.util.Set;

/**
 *
 * @author lucas
 */
public class ProductViewData {
    private final Set<Category> categories;
    private final List<Supplier> suppliers;
    private final List<ProductWithRelations> products;
    
    public ProductViewData(Set<Category> categories, List<Supplier> suppliers, List<ProductWithRelations> products) {
        this.categories = categories;
        this.suppliers = suppliers;
        this.products = products;
    }
    
    public Set<Category> getCategories() {
        return categories;
    }
    
    public List<Supplier> getSuppliers() {
        return suppliers;
    }
    
    public List<ProductWithRelations> getProducts() {
        return products;
    }
}
