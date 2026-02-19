/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.auth;

import java.util.UUID;

/**
 *
 * @author lucas
 */
public class SessionContext { //Creo que deberia devolver un objeto profile en lugar
    private static UUID currentUserId;
    private static String currentUserName;
    
    //temporal para desarrollo
    public static void setCurrentUser() {
        currentUserId = UUID.fromString("a6bbb40c-76a2-4a66-8805-a42a58392122");
        currentUserName = "Ana Administradora";
    }
    
    public static void setCurrentUser(UUID id, String name) {
        currentUserId = id;
        currentUserName = name;
    }
    
    public static UUID getCurrentUserId() {
        if (currentUserId == null) {
            throw new IllegalStateException("No hay usuario autenticado");
        }
        return currentUserId;
    }
    
    public static String getCurrentUserName() {
        return currentUserName != null ? currentUserName : "Desconocido";
    }
}