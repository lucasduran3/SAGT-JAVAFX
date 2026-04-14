/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dto;

import com.mvcjava.sagt.javafx.dao.model.Product;

/**
 *
 * @author lucas
 */
public class SaleDetailFormData {
    public final Product product;
    public final float unitPrice;
    public final int ammount;
    public final float subtotal;
    
    public SaleDetailFormData(Product product, float unitPrice, int ammount, float subtotal) {
        this.product = product;
        this.unitPrice = unitPrice;
        this.ammount = ammount;
        this.subtotal = subtotal;
    }
}
