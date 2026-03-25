/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.async;

import com.mvcjava.sagt.javafx.service.impl.SaleServiceImpl;
import com.mvcjava.sagt.javafx.service.interfaces.SaleService;
import java.util.Map;
import java.util.UUID;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author lucas
 */
public class SaleSaveService extends Service<Void>{
    
    private final SaleService saleService;
    
    private Map<UUID, Map<String, Object>> headersToUpdate;
    private Map<UUID, Map<String, Object>> detailsToUpdate;
    
    public SaleSaveService() {
        this.saleService = new SaleServiceImpl();
    }
    
    public void setData(
            Map<UUID, Map<String, Object>> headersToUpdate,
            Map<UUID, Map<String, Object>> detailsToUpdate
            ) {
        this.headersToUpdate = headersToUpdate;
        this.detailsToUpdate = detailsToUpdate;
    }

    @Override
    protected Task<Void> createTask() {
        final Map<UUID, Map<String, Object>> headers = this.headersToUpdate;
        final Map<UUID, Map<String, Object>> details = this.detailsToUpdate;
        
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                saleService.saveChanges(headersToUpdate, detailsToUpdate);
                return null;
            }
        };
    }
}
