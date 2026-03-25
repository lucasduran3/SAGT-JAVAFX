/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.viewmodel;

import com.mvcjava.sagt.javafx.dao.model.SaleDetail;
import com.mvcjava.sagt.javafx.dto.DetailSaleWithProduct;
import java.util.UUID;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author lucas
 */
public class DetailSaleViewModel {
    private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
    private final StringProperty productName = new SimpleStringProperty();
    private final IntegerProperty ammount = new SimpleIntegerProperty();
    private final FloatProperty unitPrice = new SimpleFloatProperty();
    private final FloatProperty subtotal = new SimpleFloatProperty();
    
    private final DetailSaleWithProduct source;
    private final SaleDetail detail;
    
    public DetailSaleViewModel(DetailSaleWithProduct dto) {
        this.source = dto;
        this.detail = dto.getDetail();
        this.id.set(dto.getDetail().getId());
        this.productName.set(dto.getProductName());
        this.ammount.set(dto.getDetail().getAmmount());
        this.unitPrice.set(dto.getDetail().getUnitPrice());
        this.subtotal.set(dto.getDetail().getSubtotal());
        
        this.setupSync();
    }
    
    private void setupSync() {
        this.ammount.addListener((obs, old, val) -> this.detail.setAmmount(val.intValue()));
        this.unitPrice.addListener((obs, old, val) -> this.detail.setUnitPrice(val.floatValue()));
        this.subtotal.addListener((obs, old, val) -> this.detail.setSubtotal(val.floatValue()));
    }
    
    public ObjectProperty<UUID> idProperty() { return this.id; }
    public StringProperty productNameProperty() { return this.productName; }
    public IntegerProperty ammountProperty() { return this.ammount; }
    public FloatProperty unitPriceProperty() { return this.unitPrice; }
    public FloatProperty subtotalProperty() { return this.subtotal; }
    
    public UUID getId() { return this.id.get(); }
    public String getProductName() { return this.productName.get(); }
    public int getAmmount() { return this.ammount.get(); }
    public float getUnitPrice() { return this.unitPrice.get(); }
    public float getSubtotal() { return this.subtotal.get(); }
    
    public SaleDetail getDetail() { return this.detail; }
    
    /*Recalcula y actualiza el subtotal a partir del precio unitario y la cantidad
    Debe llamarse cada vez que se actualiza uno de los dos*/
    public void recalculateSubtotal() {
        System.out.println("Entra en calcular subtotal");
        subtotal.set(unitPrice.get() * ammount.get());
        System.out.println("Resultado final: " + subtotal.get());
    }
}
