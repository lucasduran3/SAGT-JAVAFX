/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dto;

import com.mvcjava.sagt.javafx.dao.model.Client;
import com.mvcjava.sagt.javafx.enums.PaymentMethod;
import java.time.LocalDate;

/**
 *
 * @author lucas
 */
public class SaleHeaderFormData {
    public final String billNumber;
    public final LocalDate date;
    public final Client client;
    public final PaymentMethod paymentMethod;
    
    public SaleHeaderFormData(String billNumber, LocalDate date, Client client, PaymentMethod paymentMethod) {
        this.billNumber = billNumber;
        this.date = date;
        this.client = client;
        this.paymentMethod = paymentMethod;
    }
}
