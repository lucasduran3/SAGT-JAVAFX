/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.async;

import com.mvcjava.sagt.javafx.dao.model.Supplier;
import com.mvcjava.sagt.javafx.service.impl.SupplierServiceImpl;
import com.mvcjava.sagt.javafx.service.interfaces.SupplierService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author lucas
 */
public class SupplierSaveService extends Service<Void>{
    private final SupplierService supplierService;
    
    private List<Supplier> newSuppliers;
    private Map<UUID, Map<String, Object>> suppliersToUpdate;
    private List<Supplier> suppliersToDelete;
    
    public SupplierSaveService() {
        this.supplierService = new SupplierServiceImpl();
    }
    
    public void setData(List<Supplier> newSuppliers, Map<UUID, Map<String, Object>> suppliersToUpdate, List<Supplier> suppliersToDelte) {
        this.newSuppliers = newSuppliers;
        this.suppliersToUpdate = suppliersToUpdate;
        this.suppliersToDelete = suppliersToDelete;
    }
    
    @Override
    protected Task<Void> createTask() {
        return new Task() {
            @Override
            protected Void call() throws Exception {
                supplierService.saveChanges(newSuppliers, suppliersToUpdate, suppliersToDelete);
                return null;
            }
        };
    }
}
