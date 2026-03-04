/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mvcjava.sagt.javafx.enums;

/**
 *
 * @author lucas
 */
public enum ClientType {
    EMPRESA,
    PARTICULAR;
    
    public static ClientType fromString(String text) {
        if (text == null || text.isBlank()) {
            return PARTICULAR;
        }
        
        try {
            return ClientType.valueOf(text.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El valor en la DB no coincide con el Enum en Java.");
        } 
    }
}
