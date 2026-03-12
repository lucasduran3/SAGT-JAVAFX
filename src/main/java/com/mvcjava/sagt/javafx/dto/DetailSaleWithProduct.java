/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dto;

import com.mvcjava.sagt.javafx.dao.model.SaleDetail;

/**
 *
 * @author lucas
 */
public class DetailSaleWithProduct {
    private final SaleDetail detail;
    private final String productName;
    
    public DetailSaleWithProduct(SaleDetail detail, String productName) {
        this.detail = detail;
        this.productName = productName;
    }
    
    public SaleDetail getDetail() { return this.detail; }
    public String getProductName() { return this.productName; }
}
