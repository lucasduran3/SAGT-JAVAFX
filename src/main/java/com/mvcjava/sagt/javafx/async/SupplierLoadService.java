/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.async;

import com.mvcjava.sagt.javafx.dao.model.Supplier;
import com.mvcjava.sagt.javafx.service.impl.SupplierServiceImpl;
import com.mvcjava.sagt.javafx.service.interfaces.SupplierService;
import java.util.List;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author lucas
 */
public class SupplierLoadService extends Service<List<Supplier>> {
    private final SupplierService supplierService;
    
    public SupplierLoadService() {
        this.supplierService = new SupplierServiceImpl();
    }
    
    @Override
    protected Task<List<Supplier>> createTask() {
        return new Task() {
            @Override
            protected List<Supplier> call() throws Exception {
                return supplierService.getAll();
            }
        };
    }    
}
