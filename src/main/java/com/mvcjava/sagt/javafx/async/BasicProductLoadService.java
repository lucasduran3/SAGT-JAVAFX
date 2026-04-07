/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.async;

import com.mvcjava.sagt.javafx.dao.model.Product;
import com.mvcjava.sagt.javafx.service.impl.ProductServiceImpl;
import com.mvcjava.sagt.javafx.service.interfaces.ProductService;
import java.util.List;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author lucas
 */
public class BasicProductLoadService extends Service<List<Product>> {
    private ProductService productService;
    
    public BasicProductLoadService() {
        this.productService = new ProductServiceImpl();
    }

    @Override
    protected Task<List<Product>> createTask() {
        return new Task() {
            @Override
            protected List<Product> call() throws Exception {
                return productService.getAll();
            }
        };
    }
    
}
