/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.viewmodel;

import com.mvcjava.sagt.javafx.dao.model.Supplier;
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
public class SupplierViewModel {
    private final Supplier supplier;
    private final StringProperty name;
    private final StringProperty phone;
    private final StringProperty email;
    private final StringProperty address;
    private final StringProperty web;
    private final StringProperty city;
    private final StringProperty province;
    private final BooleanProperty selected;
    
    private boolean isNew;
    
    public SupplierViewModel(Supplier supplier) {
        this.supplier = supplier;
        
        this.name = new SimpleStringProperty(supplier.getName());
        this.phone = new SimpleStringProperty(supplier.getPhone());
        this.email = new SimpleStringProperty(supplier.getEmail());
        this.address = new SimpleStringProperty(supplier.getAddress());
        this.web = new SimpleStringProperty(supplier.getWeb());
        this.city = new SimpleStringProperty(supplier.getLocation());
        this.province = new SimpleStringProperty(supplier.getProvince());
        this.selected = new SimpleBooleanProperty(false);
        
        this.isNew = false;
        
        setupSync();
    }
    
    private void setupSync() {
        name.addListener((obs, oldVal, newVal) -> supplier.setName(newVal));
        phone.addListener((obs, oldVal, newVal) -> supplier.setPhone(newVal));
        email.addListener((obs, oldVal, newVal) -> supplier.setEmail(newVal));
        address.addListener((obs, oldVal, newVal) -> supplier.setAddress(newVal));
        web.addListener((obs, oldVal, newVal) -> supplier.setWeb(newVal));
        city.addListener((obs, oldVal, newVal) -> supplier.setLocation(newVal));
        province.addListener((obs, oldVal, newVal) -> supplier.setProvince(newVal));
    }
    
    public StringProperty nameProperty() {
        return name;
    }
    public StringProperty phoneProperty() {
        return phone;
    }
    public StringProperty emailProperty() {
        return email;
    }
    public StringProperty addressProperty() {
        return address;
    }
    public StringProperty webProperty() {
        return web;
    }
    public StringProperty cityProperty() {
        return city;
    }
    public StringProperty provinceProperty() {
        return province;
    }
    public BooleanProperty selectedProperty() {
        return selected;
    }
    
    public boolean isSelected() {
        return selected.get();
    }
    
    public UUID getId() {
        return supplier.getId();
    }
    
    public Supplier getModel() {
        return supplier;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SupplierViewModel other = (SupplierViewModel) obj;
        return Objects.equals(supplier, other.supplier);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(supplier);
    }
    
    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
    
    public boolean getIsNew() {
        return this.isNew;
    }
}
