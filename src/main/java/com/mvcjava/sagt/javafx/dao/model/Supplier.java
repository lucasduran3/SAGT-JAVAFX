/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.dao.model;

import com.mvcjava.sagt.javafx.util.BasicStringValidator;
import java.util.UUID;

/**
 *
 * @author lucas
 */
public class Supplier {
    private UUID id;
    private String name;
    private String phone;
    private String email;
    private String direction;
    private String web;
    private String location;
    private String province;
    
    private BasicStringValidator stringValidator;
    
    public Supplier() {
        this.stringValidator = new BasicStringValidator();
    }
    
    //SETTERS
    public void setId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser null.");
        }    
        this.id = id;
    }

    public void setName(String name) {
        stringValidator.validate(name, 3, 30, "nombre");
        this.name = name.trim().toLowerCase();
    }
    
    public void setPhone(String phone) {
        stringValidator.validate(phone, 8, 20, "telefono");
        this.phone = phone;
    }
    
    public void setEmail(String email) {
        stringValidator.validate(email, 4, 255, "email");
        this.email = email.trim().toLowerCase();
    }
    
    public void setDirection(String direction) {
        stringValidator.validate(direction, 3, 100, "direccion");
        this.direction = direction.trim().toLowerCase();
    }
    
    public void setWeb(String web) {
        stringValidator.validate(web, 4, 255, "web");
        this.web = web.trim().toLowerCase();
    }
    
    public void setLocation(String location) {
        stringValidator.validate(direction, 3, 50, "localidad");
        this.location = location.trim().toLowerCase();
    }
    
    public void setProvince(String province) {
        stringValidator.validate(province, 3, 50, "provincia");
        this.province = province.trim().toLowerCase();
    }
    
    //GETTERS
    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
    
    public String getPhone() {
        return this.phone;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public String getDirection() {
        return this.direction;
    }
    
    public String getWeb() {
        return this.web;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public String getProvince() {
        return this.province;
    }

    @Override
    public String toString() { //ComboBox usa toString para mostrar cada item al hacer items.setALl()
        return name;
    }
}
