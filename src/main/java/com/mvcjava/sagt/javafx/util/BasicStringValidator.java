/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.util;

import java.util.regex.Pattern;

/**
 *
 * @author lucas
 */
public class BasicStringValidator {
    
    //validaciones basicas para uso general
    public static void validate(String str, int minLength, int maxLength, String fieldName) {
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
    
    //Validaciones de formato especificas para capa service
    public static boolean isValidCuit(String cuit) {
        cuit = cuit.replaceAll("[^0-9]", "");
        
        if (cuit.length() != 11) return false;
        
        int[] multipliers = {5,4,3,2,7,6,5,4,3,2};
        int suma = 0;
        
        for (int i = 0; i < 10; i++) {
            suma += Character.getNumericValue(cuit.charAt(i)) * multipliers[i];
        }
        
        int rest = suma % 11;
        int calculatedDigit = 11 - rest;
        
        if (calculatedDigit == 11) {
            calculatedDigit = 0;
        } else if (calculatedDigit == 10) {
            calculatedDigit = 9;
        }
        
        int realDigit = Character.getNumericValue(cuit.charAt(10));
        
        return calculatedDigit == realDigit;
    }
    
    public static boolean isValidPhone(String phone) {
        Pattern phonePattern = Pattern.compile("^\\d{8,12}$");
        return phonePattern.matcher(phone).matches();
    }
    
    public static boolean isValidEmail(String email) {
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        return emailPattern.matcher(email).matches();
    }
}
