/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.async;

import com.mvcjava.sagt.javafx.dto.HeaderSaleWithClient;
import com.mvcjava.sagt.javafx.service.impl.SaleServiceImpl;
import com.mvcjava.sagt.javafx.service.interfaces.SaleService;
import java.util.List;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author lucas
 */
public class SaleLoadService extends Service<List<HeaderSaleWithClient>>{
    private final SaleService saleService;
    
    public SaleLoadService() {
        this.saleService = new SaleServiceImpl();
    }

    @Override
    protected Task<List<HeaderSaleWithClient>> createTask() {
        return new Task<>() {
            @Override
            protected List<HeaderSaleWithClient> call() throws Exception {
                return saleService.getAllHeaders();
            }
        };
    }
}
