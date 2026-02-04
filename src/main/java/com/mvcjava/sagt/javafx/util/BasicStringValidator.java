/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.util;

/**
 *
 * @author lucas
 */
public class BasicStringValidator {
    
    public void validate(String str, int minLength, int maxLength, String fieldName) {
        //Error maxLength menor que minLength y valores negativos
        if (minLength < 0 || maxLength < 0) {
            throw new IllegalArgumentException("minLength o maxLength no pueden ser negativos.");
        } else if (minLength > maxLength) {
            throw new IllegalArgumentException("minLength no puede ser mayor que maxLength");
        }
        
        if (str == null) {
            throw new IllegalArgumentException("El campo '" + fieldName + "' no puede ser null.");
        } else if (str.isBlank() || str.isEmpty()) {
            throw new IllegalArgumentException("El campo '" + fieldName + "' no puede estar vacío.");
        } else if (str.length() < minLength) {
            throw new IllegalArgumentException("El campo '" + fieldName + "' es demasiado corto. Debe tener mas de " + minLength + " carácteres.");
        } else if (str.length() > maxLength) {
            throw new IllegalArgumentException("El campo '" + fieldName + "' es demasiado largo. Debe tener menos de " + maxLength + "carácteres");
        }
    }
   
}
