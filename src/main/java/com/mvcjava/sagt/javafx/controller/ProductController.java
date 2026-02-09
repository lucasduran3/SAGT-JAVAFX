/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.controller;

import com.mvcjava.sagt.javafx.service.interfaces.ProductService;
import com.mvcjava.sagt.javafx.service.interfaces.SupplierService;
import com.mvcjava.sagt.javafx.dao.model.Product;
import com.mvcjava.sagt.javafx.dao.model.Supplier;
import com.mvcjava.sagt.javafx.exception.DataAccessException;
import com.mvcjava.sagt.javafx.service.impl.ProductServiceImpl;
import com.mvcjava.sagt.javafx.service.impl.SupplierServiceImpl;
import com.mvcjava.sagt.javafx.viewmodel.ProductViewModel;
import com.mvcjava.sagt.javafx.dto.ProductWithRelations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 *
 * @author lucas
 */
public class ProductController {
    private ProductService productService;
    private SupplierService supplierService;
    
    @FXML 
    private TableView<ProductViewModel> productsTable;
    
    private TableColumn<ProductViewModel, UUID> idColumn;
    private TableColumn<ProductViewModel, String> nameColumn;
    private TableColumn<ProductViewModel, String> brandColumn;
    private TableColumn<ProductViewModel, String> modelColumn;
    private TableColumn<ProductViewModel, Float> purchasePriceColumn;
    private TableColumn<ProductViewModel, Float> salePriceColumn;
    private TableColumn<ProductViewModel, Integer> stockColumn;
    private TableColumn<ProductViewModel, Integer> minStockColumn;
    private TableColumn<ProductViewModel, Supplier> supplierColumn;
    private TableColumn<ProductViewModel, String> loadedByNameColumn;
    private TableColumn<ProductViewModel, Timestamp> entryDateColumn;
    private TableColumn<ProductViewModel, Timestamp> updateDateColumn;
    
    private ObservableList<ProductViewModel> productViewModels; //notifica a la tabla cuando cambia
    private ObservableList<Supplier> avaibleSuppliers;
   
    public ProductController() {}
    
    @FXML
    public void initialize() {
        initializeDependencies();
        productViewModels = FXCollections.observableArrayList();
        avaibleSuppliers = FXCollections.observableArrayList();
        
        setupTableColumns();
        productsTable.setItems(productViewModels);
        
        loadSuppliers();
        loadProducts();
    }
    
    private void initializeDependencies() {
        this.productService = new ProductServiceImpl();
        this.supplierService = new SupplierServiceImpl();
    }
    
    private void setupTableColumns() {
        idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getId())
        );
        idColumn.setEditable(false);
        
        nameColumn = new TableColumn<>("Nombre");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setEditable(true);
        
        brandColumn = new TableColumn<>("Marca");
        brandColumn.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        brandColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        brandColumn.setEditable(true);
        
        modelColumn = new TableColumn<>("Modelo");
        modelColumn.setCellValueFactory(cellData -> cellData.getValue().modelProperty());
        modelColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        modelColumn.setEditable(true);
        
        purchasePriceColumn = new TableColumn<>("Precio de compra");
        purchasePriceColumn.setCellValueFactory(cellData -> cellData.getValue().purchasePriceProperty().asObject());
        purchasePriceColumn.setEditable(false);
        
        salePriceColumn = new TableColumn<>("Precio de venta");
        salePriceColumn.setCellValueFactory(cellData -> cellData.getValue().salePriceProperty().asObject());
        salePriceColumn.setEditable(false);
        
        stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        stockColumn.setEditable(false);
        
        minStockColumn = new TableColumn<>("Stock minimo");
        minStockColumn.setCellValueFactory(cellData -> cellData.getValue().minStockProperty().asObject());
        minStockColumn.setEditable(false);
        
        supplierColumn = new TableColumn<>("Proveedor");
        supplierColumn.setCellValueFactory(cellData -> 
            cellData.getValue().supplierProperty()
        );
        
        //configurar comboBox
        supplierColumn.setCellFactory(col -> {
            ComboBoxTableCell<ProductViewModel, Supplier> cell = 
                    new ComboBoxTableCell<ProductViewModel, Supplier>() {
                    
                        @Override
                        public void updateItem(Supplier item, boolean empty) {
                            super.updateItem(item, empty);
                            
                            if (empty || item == null) {
                                setText(null);
                            } else {
                                setText(item.getName());
                            }
                        }
                    };
            cell.getItems().setAll(avaibleSuppliers);
            
            return cell;
        });
        supplierColumn.setEditable(true);
        
        loadedByNameColumn = new TableColumn<>("Cargado por");
        loadedByNameColumn.setCellValueFactory(cellData -> 
                cellData.getValue().loadedByNameProperty()
        );
        loadedByNameColumn.setEditable(false);
        
        entryDateColumn = new TableColumn<>("Fecha de carga");
        entryDateColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getEntryDate())
        );
        entryDateColumn.setEditable(false);
        
        updateDateColumn = new TableColumn<>("Fecha de actualizacion");
        updateDateColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getUpdateDate())
        );
        updateDateColumn.setEditable(false);
        
        productsTable.getColumns().addAll(
                idColumn,
                nameColumn,
                brandColumn,
                modelColumn,
                purchasePriceColumn,
                salePriceColumn,
                stockColumn,
                minStockColumn,
                supplierColumn,
                loadedByNameColumn,
                entryDateColumn,
                updateDateColumn
        );
        
        productsTable.setEditable(true);
        productsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void loadSuppliers() {
        try {
            List<Supplier> suppliers = supplierService.getAll();
            avaibleSuppliers.setAll(suppliers);
        } catch (DataAccessException ex) {
            showError(ex.getMessage());
        }
    }
    
    private void loadProducts() {
       try {
           List<ProductWithRelations> products = productService.getAllWithRelations();
           
           List<ProductViewModel> viewModels = new ArrayList<>();
           for (ProductWithRelations dto : products) {
               UUID supplierId = dto.getProduct().getIdSupplier();
               Supplier supplier = findSupplierById(supplierId);
               
               ProductViewModel productVm = new ProductViewModel(dto, supplier);
               viewModels.add(productVm);
           }
           
           productViewModels.setAll(viewModels);
           
       } catch (DataAccessException ex) {
           showError("Error al cargar productos: " + ex.getMessage());
       }
    }
    
    //buscar proveedor por id desde controller para no realizar varias consultas
    private Supplier findSupplierById(UUID id) {
        if (id == null) {
            return null;
        }
        
        return avaibleSuppliers.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
