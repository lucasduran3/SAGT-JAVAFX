/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.controller;

import com.mvcjava.sagt.javafx.async.SupplierLoadService;
import com.mvcjava.sagt.javafx.async.SupplierSaveService;
import com.mvcjava.sagt.javafx.dao.model.Supplier;
import com.mvcjava.sagt.javafx.util.AlertUtils;
import com.mvcjava.sagt.javafx.viewmodel.SupplierViewModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author lucas
 */
public class SupplierController {
    @FXML
    private TableView<SupplierViewModel> supplierTable;
    
    private TableColumn<SupplierViewModel, Boolean> selectColumn;
    private TableColumn<SupplierViewModel, String> nameColumn;
    private TableColumn<SupplierViewModel, String> phoneColumn;
    private TableColumn<SupplierViewModel, String> emailColumn;
    private TableColumn<SupplierViewModel, String> addressColumn;
    private TableColumn<SupplierViewModel, String> webColumn;
    private TableColumn<SupplierViewModel, String> cityColumn;
    private TableColumn<SupplierViewModel, String> provinceColumn;
    
    private ObservableList<SupplierViewModel> supplierViewModels;
    private Map<UUID, Map<String, Object>> suppliersToUpdate;
    private List<SupplierViewModel> suppliersToDelete;
    
    //async
    private SupplierLoadService loadService;
    private SupplierSaveService saveService;
    
    @FXML
    public void initialize() {
        initializeDependencies();
        supplierViewModels = FXCollections.observableArrayList();
        
        setupTableColumns();
        supplierTable.setItems(supplierViewModels);
        
        loadData();
        
        suppliersToUpdate = new HashMap<>();
        suppliersToDelete = new ArrayList<>();
    }
    
    private void initializeDependencies() {
        this.loadService = new SupplierLoadService();
        this.saveService = new SupplierSaveService();
    }
    
    private void setupTableColumns() {
        
    }
    
    private void loadData() {
        if (!loadService.isRunning()) {
            loadService.reset();
            
            loadService.setOnSucceeded(e -> {
                List<Supplier> suppliers = loadService.getValue();
                supplierViewModels.setAll(suppliers.stream().map(SupplierViewModel::new).collect(Collectors.toList()));
            });
            
            loadService.setOnFailed(e -> {
                AlertUtils.showError(e.getSource().getException().getMessage());
            });
            
            loadService.start();
        }
    }
    
    private void saveData() {
        
    }
}
