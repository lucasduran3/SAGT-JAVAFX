/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.service.impl;

import com.mvcjava.sagt.javafx.dao.impl.SaleDAOImpl;
import com.mvcjava.sagt.javafx.dao.interfaces.SaleDAO;
import com.mvcjava.sagt.javafx.dto.DetailSaleWithProduct;
import com.mvcjava.sagt.javafx.dto.HeaderSaleWithClient;
import com.mvcjava.sagt.javafx.service.interfaces.SaleService;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author lucas
 */
public class SaleServiceImpl implements SaleService {
    
    private final SaleDAO saleDAO;
    
    public SaleServiceImpl() {
        this.saleDAO = new SaleDAOImpl();
    }

    @Override
    public List<HeaderSaleWithClient> getAllHeaders() {
        return saleDAO.findAllHeaders();
    }

    @Override
    public List<DetailSaleWithProduct> getDetailBySaleId(UUID saleId) {
        return saleDAO.findDetailBySaleId(saleId);
    }
}
