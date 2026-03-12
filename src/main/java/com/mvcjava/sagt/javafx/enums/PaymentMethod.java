/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.enums;

/**
 *
 * @author lucas
 */
public enum PaymentMethod {
    EFECTIVO,
    TRANSFERENCIA, 
    DEBITO,
    CREDITO;
    
    public static PaymentMethod fromString(String text) {
        if (text == null || text.isBlank()) {
            return EFECTIVO;
        }
        
        try {
            return PaymentMethod.valueOf(text.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("El valor en la DB no coincide con el Enum en Java.");
        }
    }
}
