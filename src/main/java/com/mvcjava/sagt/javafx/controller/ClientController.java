/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.controller;

import com.mvcjava.sagt.javafx.service.impl.ClientServiceImpl;
import com.mvcjava.sagt.javafx.service.interfaces.ClientService;
import com.mvcjava.sagt.javafx.viewmodel.ClientViewModel;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author lucas
 */
public class ClientController {
    private ClientService clientService;
    
    @FXML
    private TableView<ClientViewModel> clientsTable;
    
    private TableColumn<ClientViewModel, Boolean> selectColumn;
    private TableColumn<ClientViewModel, UUID> idColumn;
    private TableColumn<ClientViewModel, String> cuitColumn;
    private TableColumn<ClientViewModel, String> companyNameColumn;
    private TableColumn<ClientViewModel, String> clientTypeColumn;
    private TableColumn<ClientViewModel, String> phoneColumn;
    private TableColumn<ClientViewModel, String> emailColumn;
    private TableColumn<ClientViewModel, String> addressColumn;
    private TableColumn<ClientViewModel, String> locationColumn;
    private TableColumn<ClientViewModel, String> provinceColumn;
    private TableColumn<ClientViewModel, Timestamp> entryDateColumn;
    
    private ObservableList<ClientViewModel> clientViewModels;
    
    private Map<UUID, Map<String, Object>> clientsToUpdate;
    private Set<ClientViewModel> clientsToDelete;
    
    public ClientController() {}
    
    @FXML
    public void initialize() {
        initializeDependencies();
        clientViewModels = FXCollections.observableArrayList();
        
        setupTableColumns();
        clientsTable.setItems(clientViewModels);
        
        //cargar datos
        
        clientsToUpdate = new HashMap<>();
        clientsToDelete = new HashSet<>();
    }
    
    private void initializeDependencies() {
        this.clientService = new ClientServiceImpl();
    }
    
    private void setupTableColumns() {
        
    }
}
