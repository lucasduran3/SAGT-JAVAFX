/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.viewmodel;

import com.mvcjava.sagt.javafx.dao.model.Category;
import com.mvcjava.sagt.javafx.dao.model.Product;
import com.mvcjava.sagt.javafx.dao.model.Supplier;
import com.mvcjava.sagt.javafx.dto.ProductWithRelations;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

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
    
    private final ObservableList<Category> categories;
    private final StringProperty categoriesDisplay;
    
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
        
        this.categories = FXCollections.observableArrayList(dto.getCategories());
        this.categoriesDisplay = new SimpleStringProperty();
        updateCategoriesDisplay();
        
        setupSync();
    }
    
    //Sincronizar properties con el modelo
    private void setupSync() {
        name.addListener((obs, old, newVal) -> product.setName(newVal));
        brand.addListener((obs, old, newVal) -> product.setBrand(newVal));
        model.addListener((obs, old, newVal) -> product.setModel(newVal));
        purchasePrice.addListener((obs, old, newVal) -> product.setPurchasePrice(newVal.floatValue()));
        salePrice.addListener((obs, old, newVal) -> product.setSalePrice(newVal.floatValue()));
        stock.addListener((obs, old, newVal) -> product.setStock(newVal.intValue()));
        minStock.addListener((obs, old, newVal) -> product.setMinStock(newVal.intValue()));
        supplier.addListener((o, cl, newVal) -> {
            if (newVal != null) {
                product.setIdSupplier(newVal.getId());
            }
        });
        
        //Actualiza el display cuando cambian las categorias
        this.categories.addListener((ListChangeListener<Category>) change -> {
            updateCategoriesDisplay();
        });
    }
    
    //Getters de properties
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
    public StringProperty categoriesDisplayProperty() {
        return categoriesDisplay;
    }
    public ObservableList<Category> getCategories() {
        return categories;
    }
    
    //Getters de campos no editables
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
       
    //Getter modelo
    public Product getModel() {
        return this.product;
    }
    
    public void setCategories(List<Category> categories) {
        this.categories.setAll(categories);
    }
    
    private void updateCategoriesDisplay() {
        if (categories.isEmpty()) {
            categoriesDisplay.set("Sin categorias");
        } else {
            String display = categories.stream()
                    .map(Category::getName)
                    .collect(Collectors.joining(", "));
            categoriesDisplay.set(display);
        }
    }
}