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
public class Category {
    private UUID id;
    private String name;
    private BasicStringValidator stringValidator;
    
    public Category(UUID id, String name) {
        stringValidator = new BasicStringValidator();
                
        setId(id);
        setName(name);
    }
    
    private void setId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("El id de categoria no puede ser null.");
        }
        this.id = id;
    }
    
    private void setName(String name) {
        stringValidator.validate(name, 3, 30, "nombre_categoria");
        this.name = name.trim().toLowerCase();
    }
    
    public UUID getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return id != null && id.equals(category.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return this.getName();
    }
    
}
