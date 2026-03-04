/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.controller;

import com.mvcjava.sagt.javafx.dao.model.Client;
import com.mvcjava.sagt.javafx.enums.ClientType;
import com.mvcjava.sagt.javafx.service.impl.ClientServiceImpl;
import com.mvcjava.sagt.javafx.service.interfaces.ClientService;
import com.mvcjava.sagt.javafx.util.AlertUtils;
import com.mvcjava.sagt.javafx.util.BasicStringValidator;
import com.mvcjava.sagt.javafx.viewmodel.ClientViewModel;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;

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
    private TableColumn<ClientViewModel, ClientType> clientTypeColumn;
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

        loadData();

        clientsToUpdate = new HashMap<>();
        clientsToDelete = new HashSet<>();
    }

    private void initializeDependencies() {
        this.clientService = new ClientServiceImpl();
    }

    private void setupTableColumns() {
        selectColumn = new TableColumn<>("");
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(col -> new CheckBoxTableCell<>());
        selectColumn.setEditable(true);
        selectColumn.setResizable(false);
        selectColumn.setMaxWidth(50);
        selectColumn.setMinWidth(50);

        idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setEditable(false);

        cuitColumn = createStringColumn("CUIT/CUIL", "cuit_cuil", ClientViewModel::cuitCuilProperty, 11, 11, true, false, false);
        companyNameColumn = createStringColumn("Razón social", "razon_social", ClientViewModel::companyNameProperty, 1, 100, false, false, false);
        phoneColumn = createStringColumn("Teléfono", "telefono", ClientViewModel::phoneProperty, 8, 20, false, true, false);
        emailColumn = createStringColumn("Email", "email", ClientViewModel::emailProperty, 4, 255, false, false, true);
        addressColumn = createStringColumn("Dirección", "direccion", ClientViewModel::addressProperty, 3, 100, false, false, false);
        locationColumn = createStringColumn("Localidad", "localidad", ClientViewModel::locationProperty, 3, 50, false, false, false);
        provinceColumn = createStringColumn("Provincia", "provincia", ClientViewModel::provinceProperty, 3, 50, false, false, false);

        clientTypeColumn = new TableColumn<>("Tipo");
        clientTypeColumn.setUserData("tipo");
        clientTypeColumn.setCellValueFactory(cellData -> cellData.getValue().clientTypeProperty());
        clientTypeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(ClientType.values()));
        clientTypeColumn.setOnEditCommit(this::handleClientTypeEdit);
        clientTypeColumn.setEditable(true);

        entryDateColumn = new TableColumn<>("Fecha de alta");
        entryDateColumn.setCellValueFactory(cellData -> cellData.getValue().entryDateProperty());
        entryDateColumn.setEditable(false);

        clientsTable.getColumns().addAll(
                selectColumn,
                idColumn,
                cuitColumn,
                companyNameColumn,
                clientTypeColumn,
                phoneColumn,
                emailColumn,
                addressColumn,
                locationColumn,
                provinceColumn,
                entryDateColumn
        );

        clientsTable.setEditable(true);
        clientsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<ClientViewModel, String> createStringColumn(
            String title,
            String dbField,
            javafx.util.Callback<ClientViewModel, javafx.beans.property.StringProperty> propertyGetter,
            int minLength,
            int maxLength,
            boolean validateCuit,
            boolean validatePhone,
            boolean validateEmail
    ) {
        TableColumn<ClientViewModel, String> column = new TableColumn<>(title);
        column.setUserData(dbField);
        column.setCellValueFactory(cellData -> propertyGetter.call(cellData.getValue()));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setEditable(true);

        column.setOnEditCommit(e -> {
            String newValue = normalize(e.getNewValue());
            String oldValue = normalize(e.getOldValue());

            if (newValue.equals(oldValue)) {
                return;
            }

            try {
                BasicStringValidator.validate(newValue, minLength, maxLength, title.toLowerCase());
                if (validateCuit && !BasicStringValidator.isValidCuit(newValue)) {
                    throw new IllegalArgumentException("Número de cuit/cuil inválido.");
                }
                if (validatePhone && !BasicStringValidator.isValidPhone(newValue)) {
                    throw new IllegalArgumentException("Número de teléfono inválido.");
                }
                if (validateEmail && !BasicStringValidator.isValidEmail(newValue)) {
                    throw new IllegalArgumentException("Dirección de email inválida.");
                }

                ClientViewModel vm = e.getRowValue();
                propertyGetter.call(vm).set(newValue);
                registerUpdate(vm.getId(), dbField, newValue);
            } catch (IllegalArgumentException ex) {
                AlertUtils.showError(ex.getMessage());
                clientsTable.refresh();
            }
        });

        return column;
    }

    private void handleClientTypeEdit(TableColumn.CellEditEvent<ClientViewModel, ClientType> e) {
        ClientType newValue = e.getNewValue();
        ClientType oldValue = e.getOldValue();

        if (newValue == null || newValue == oldValue) {
            return;
        }

        ClientViewModel vm = e.getRowValue();
        vm.clientTypeProperty().set(newValue);
        registerUpdate(vm.getId(), "tipo", newValue.name());
    }

    private void registerUpdate(UUID clientId, String fieldName, Object value) {
        clientsToUpdate.computeIfAbsent(clientId, k -> new HashMap<>()).put(fieldName, value);
    }

    private void loadData() {
        Set<Client> clients = clientService.getAll();
        clientViewModels.setAll(
                clients.stream()
                        .map(ClientViewModel::new)
                        .collect(Collectors.toList())
        );
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
