/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dao.model;

import com.mvcjava.sagt.javafx.enums.PaymentMethod;
import com.mvcjava.sagt.javafx.util.BasicStringValidator;
import java.sql.Timestamp;
import java.util.UUID;

/**
 *
 * @author lucas
 */
public class SaleHeader {
    private UUID id;
    private String billNumber;
    private Timestamp date;
    private UUID clientId;
    private float total;
    private PaymentMethod paymentMethod;
    private UUID loadedBy;
    
    public SaleHeader() {}
    
    public SaleHeader(UUID id, String billNumber, Timestamp date, UUID clientId, float total, PaymentMethod paymentMethod, UUID loadedBy) {
        this.id = id;
        this.billNumber = billNumber;
        this.date = date;
        this.clientId = clientId;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.loadedBy = loadedBy;
    }
    
    //SETTERS
    
    public void setId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("El id de venta no puede ser null");
        }
        this.id = id;
    }
    
    public void setBillNumber(String billNumber) {
        billNumber = billNumber.trim().toLowerCase();
        BasicStringValidator.validate(billNumber, 1, 20, "numero de venta");
        this.billNumber = billNumber;
    }
    
    public void setDate(Timestamp date) {
        if (date == null) {
            throw new IllegalArgumentException("La fecha de la venta no puede ser null.");
        }
        this.date = date;
    }
    
    public void setClientId(UUID clientId) {
        if (clientId == null) {
            throw new IllegalArgumentException("El id del cliente no puede ser null.");
        }
        this.clientId = clientId;
    }
    
    public void setTotal(float total) {
        if (total < 0) {
            throw new IllegalArgumentException("El total de la venta no puede ser negativo.");
        }
        this.total = total;
    }
    
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public void setLoadedBy(UUID id) {
        this.loadedBy = id;
    }
    
    //GETTERS
    
    public UUID getId() {
        return this.id;
    }
    
    public String getBillNumber() {
        return this.billNumber;
    }
    
    public Timestamp getDate() {
        return this.date;
    }
    
    public UUID getClientId() {
        return this.clientId;
    }
    
    public float getTotal() {
        return this.total;
    }
    
    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }
    
    public UUID getLoadedBy() {
        return this.loadedBy;
    }
}
