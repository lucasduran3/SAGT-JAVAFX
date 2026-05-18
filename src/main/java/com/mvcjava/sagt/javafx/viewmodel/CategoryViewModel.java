/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.viewmodel;

import com.mvcjava.sagt.javafx.dao.model.Category;
import java.util.Objects;
import java.util.UUID;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author lucas
 */
public class CategoryViewModel {
    private final Category category;
    private final StringProperty name;
    private final BooleanProperty selected;
    
    private boolean isNew;
    
    public CategoryViewModel(Category category) {
        this.category = category;
        
        this.name = new SimpleStringProperty(category.getName());
        this.selected = new SimpleBooleanProperty(false);
        
        this.isNew = false;
        
        this.name.addListener((obs, oldVal, newVal) -> category.setName(newVal));
    }
    
    public StringProperty nameProperty() {
        return name;
    }
    public BooleanProperty selectedProperty() {
        return selected;
    }
    
    public boolean isSelected() {
        return selected.get();
    }
    
    public UUID getId() {
        return category.getId();
    }
    
    public Category getModel() {
        return category;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CategoryViewModel other = (CategoryViewModel) obj;
        return Objects.equals(category, other.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category);
    }
    
    public void setIsNew(boolean value) {
        this.isNew = value;
    }
    
    public boolean getIsNew() {
        return this.isNew;
    }
    
}
