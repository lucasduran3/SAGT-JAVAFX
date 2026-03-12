/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dto;

import com.mvcjava.sagt.javafx.dao.model.SaleHeader;

/**
 *
 * @author lucas
 */
public class HeaderSaleWithClient {
    private final SaleHeader header;
    private final String clientCompanyName;
    
    public HeaderSaleWithClient(SaleHeader header, String clientCompanyName) {
        this.header = header;
        this.clientCompanyName = clientCompanyName;
    }
    
    public SaleHeader getHeader() { return this.header; }
    public String getClientCompanyName() { return this.clientCompanyName; }
}
