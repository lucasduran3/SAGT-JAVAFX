/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.viewmodel;

import com.mvcjava.sagt.javafx.dao.model.Product;
import com.mvcjava.sagt.javafx.dao.model.Supplier;
import com.mvcjava.sagt.javafx.dto.ProductWithRelations;
import java.sql.Timestamp;
import java.util.UUID;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author lucas
 */
public class ProductViewModel {
    private final Product product;
    private final StringProperty name;
    private final StringProperty brand;
    private final StringProperty model;
    private final FloatProperty purchasePrice;
    private final FloatProperty salePrice;
    private final IntegerProperty stock;
    private final IntegerProperty minStock;
    private final StringProperty loadedByName;
    private final ObjectProperty<Supplier> supplier;
    
    //Constructor recibe producto + nombres
    public ProductViewModel(ProductWithRelations dto, Supplier supplier) {
        this.product = dto.getProduct();

        this.name = new SimpleStringProperty(product.getName());
        this.brand = new SimpleStringProperty(product.getBrand());
        this.model = new SimpleStringProperty(product.getModel());
        this.purchasePrice = new SimpleFloatProperty(product.getPurchasePrice());
        this.salePrice = new SimpleFloatProperty(product.getSalePrice());
        this.stock = new SimpleIntegerProperty(product.getStock());
        this.minStock = new SimpleIntegerProperty(product.getMinStock());
        this.loadedByName = new SimpleStringProperty(dto.getLoadedByName());
        this.supplier = new SimpleObjectProperty<>(supplier);
        
        setupSync();
    }
    
    private void setupSync() {
        name.addListener((o, cl, newVal) -> product.setName(newVal));
        brand.addListener((o, cl, newVal) -> product.setBrand(newVal));
        model.addListener((o, cl, newVal) -> product.setModel(newVal));
        purchasePrice.addListener((o, cl, newVal) -> product.setPurchasePrice(newVal.floatValue()));
        salePrice.addListener((o, cl, newVal) -> product.setSalePrice(newVal.floatValue()));
        stock.addListener((o, cl, newVal) -> product.setStock(newVal.intValue()));
        minStock.addListener((o, cl, newVal) -> product.setMinStock(newVal.intValue()));
        
        supplier.addListener((o, cl, newVal) -> {
            if (newVal != null) {
                product.setIdSupplier(newVal.getId());
            }
        });
    }
    
    //GETTERS
    public StringProperty nameProperty() {
        return name;
    }
    public StringProperty brandProperty() {
        return brand;
    }
    public StringProperty modelProperty() {
        return model;
    }
    public FloatProperty purchasePriceProperty() {
        return purchasePrice;
    }
    public FloatProperty salePriceProperty() {
        return salePrice;
    }
    public IntegerProperty stockProperty() {
        return stock;
    }
    public IntegerProperty minStockProperty() {
        return minStock;
    }
    public StringProperty loadedByNameProperty() {
        return loadedByName;
    }
    public ObjectProperty<Supplier> supplierProperty() {
        return supplier;
    }
    
    
    //GETTERS CAMPOS NO EDITABLES
    public UUID getId() {
        return product.getId();
    }
    public UUID getIdSupplier() {
        return product.getIdSupplier();
    }
    public UUID getLoadedBy() {
        return product.getLoadedBy();
    }
    public String loadedByName() {
        return loadedByName.get();
    }
    public Timestamp getEntryDate() {
        return product.getEntryDate();
    }
    public Timestamp getUpdateDate() {
        return product.getUpdateDate();
    }
        
    public Product getModel() {
        return this.product;
    }
}