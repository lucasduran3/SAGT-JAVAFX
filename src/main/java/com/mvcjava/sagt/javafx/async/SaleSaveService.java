/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.async;

import com.mvcjava.sagt.javafx.dao.model.SaleDetail;
import com.mvcjava.sagt.javafx.dao.model.SaleHeader;
import com.mvcjava.sagt.javafx.service.impl.SaleServiceImpl;
import com.mvcjava.sagt.javafx.service.interfaces.SaleService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author lucas
 */
public class SaleSaveService extends Service<Void> {
    
    private final SaleService saleService;
    
    private Map<UUID, Map<String, Object>> headersToUpdate;
    private Map<UUID, Map<String, Object>> detailsToUpdate;
    private List<SaleHeader> headersToCreate;
    private List<SaleDetail> detailsToCreate;
    private List<UUID> headersToDelete;
    private List<UUID> detailsToDelete;
    
    public SaleSaveService() {
        this.saleService = new SaleServiceImpl();
    }
    
    public void setData(
            List<SaleHeader> headersToCreate,
            List<SaleDetail> detailsToCreate,
            Map<UUID, Map<String, Object>> headersToUpdate,
            Map<UUID, Map<String, Object>> detailsToUpdate,
            List<UUID> headersToDelete,
            List<UUID> detailsToDelete
            ) {
        this.headersToCreate = headersToCreate;
        this.detailsToCreate = detailsToCreate;
        this.headersToUpdate = headersToUpdate;
        this.detailsToUpdate = detailsToUpdate;
        this.headersToDelete = headersToDelete;
        this.detailsToDelete = detailsToDelete;
    }

    @Override
    protected Task<Void> createTask() {        
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                saleService.saveChanges(headersToCreate, detailsToCreate, headersToUpdate, detailsToUpdate, headersToDelete, detailsToDelete);
                return null;
            }
        };
    }
}
