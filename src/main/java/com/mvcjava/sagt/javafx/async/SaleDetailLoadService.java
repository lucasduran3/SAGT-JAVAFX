/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.async;

import com.mvcjava.sagt.javafx.dto.DetailSaleWithProduct;
import com.mvcjava.sagt.javafx.service.impl.SaleServiceImpl;
import com.mvcjava.sagt.javafx.service.interfaces.SaleService;
import java.util.List;
import java.util.UUID;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author lucas
 */
public class SaleDetailLoadService extends Service<List<DetailSaleWithProduct>>{
    private final SaleService saleService;
    private UUID saleId;
    
    public SaleDetailLoadService() {
        this.saleService = new SaleServiceImpl();
    }
    
    public void setSaleId(UUID id) {
        this.saleId = id;
    }

    @Override
    protected Task<List<DetailSaleWithProduct>> createTask() {
        final UUID saleId = this.saleId;
        return new Task() {
            @Override
            protected List<DetailSaleWithProduct> call() throws Exception {
                return saleService.getDetailBySaleId(saleId);
            }
        };
    }
}
