/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.async;

import com.mvcjava.sagt.javafx.dao.model.Category;
import com.mvcjava.sagt.javafx.dao.model.Supplier;
import com.mvcjava.sagt.javafx.dto.ProductViewData;
import com.mvcjava.sagt.javafx.dto.ProductWithRelations;
import com.mvcjava.sagt.javafx.service.interfaces.CategoryService;
import com.mvcjava.sagt.javafx.service.interfaces.ProductService;
import com.mvcjava.sagt.javafx.service.interfaces.SupplierService;
import java.util.List;
import java.util.Set;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author lucas
 */
public class ProductLoadService extends Service<ProductViewData>{
    private final ProductService productService;
    private final SupplierService supplierService;
    private final CategoryService categoryService;
    
    public ProductLoadService(ProductService productService, SupplierService supplierService, CategoryService categoryService) {
        this.productService = productService;
        this.supplierService = supplierService;
        this.categoryService = categoryService;
    }

    @Override
    protected Task<ProductViewData> createTask() {
        return new Task() {
            @Override
            protected ProductViewData call() throws Exception{
                Set<Category> categories = categoryService.getAll();
                List<Supplier> suppliers = supplierService.getAll();
                List<ProductWithRelations> products = productService.getAllWithRelations();
                
                return new ProductViewData(categories, suppliers, products);
            }
        };
    }
}
