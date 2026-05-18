/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.controller;

import com.mvcjava.sagt.javafx.async.CategoryLoadService;
import com.mvcjava.sagt.javafx.async.CategorySaveService;
import com.mvcjava.sagt.javafx.dao.model.Category;
import com.mvcjava.sagt.javafx.viewmodel.CategoryViewModel;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;

/**
 *
 * @author lucas
 */
public class CategoryController {
    @FXML
    private TableView<CategoryViewModel> categoriesTable;
    
    private TableColumn<CategoryViewModel, Boolean> selectColumn;
    private TableColumn<CategoryViewModel, UUID> idColumn;
    private TableColumn<CategoryViewModel, String> nameColumn;
    
    private ObservableList<CategoryViewModel> categoryViewModels;
    
    private Set<Category> categoriesToUpdate;
    private Set<Category> categoriesToDelete;
    
    private CategoryLoadService loadService;
    private CategorySaveService saveService;
    
    public CategoryController() {}
    
    @FXML
    public void initialize() {
        this.loadService = new CategoryLoadService();
        this.saveService = new CategorySaveService();
        
        categoryViewModels = FXCollections.observableArrayList();
        
        setupTableColumns();
        categoriesTable.setItems(categoryViewModels);
        
        loadData();
        
        categoriesToUpdate = new HashSet<>();
        categoriesToDelete = new HashSet<>();
    }
    
    private void setupTableColumns() {
        selectColumn = new TableColumn<>("");
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(col -> new CheckBoxTableCell<>());
        selectColumn.setEditable(true);
        selectColumn.setResizable(false);
        selectColumn.setMaxWidth(50);
        selectColumn.setMinWidth(50);    
    }
    
    private void loadData() {
        
    }
}
