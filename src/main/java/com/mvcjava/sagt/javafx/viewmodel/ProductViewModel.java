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
import java.util.Objects;
import java.util.Set;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

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
    private final ObjectProperty<Timestamp> updateDate;
    
    private final ObservableSet<Category> categories;
    private final StringProperty categoriesDisplay;
    
    private final BooleanProperty selected;
    
    private boolean isNew;
    
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
        this.updateDate = new SimpleObjectProperty<>(product.getUpdateDate());
        
        this.categories = FXCollections.observableSet(dto.getCategories());
        this.categoriesDisplay = new SimpleStringProperty();
        
        this.selected = new SimpleBooleanProperty(false);
        
        updateCategoriesDisplay();
        
        this.isNew = false;
        
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
        updateDate.addListener((obs, old, newVal) -> product.setUpdateDate(newVal));
        
        supplier.addListener((o, cl, newVal) -> {
            if (newVal != null) {
                product.setIdSupplier(newVal.getId());
            }
        });
        
        //Actualiza el display cuando cambian las categorias
        this.categories.addListener((SetChangeListener<Category>) change -> {
            updateCategoriesDisplay();
        });
    }
    
    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
    public boolean getIsNew() {
        return this.isNew;
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
    public ObservableSet<Category> getCategories() {
        return categories;
    }
    public ObjectProperty<Timestamp> updateDateProperty() {
        return updateDate;
    }
    public BooleanProperty selectedProperty() {
        return selected;
    }
    
    public boolean isSelected() {
        return selected.get();
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

       
    //Getter modelo
    public Product getModel() {
        return this.product;
    }
    
    public void setCategories(Set<Category> categories) {
        this.categories.addAll(categories);
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProductViewModel other = (ProductViewModel) obj;
        
        return Objects.equals(product, other.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }
}