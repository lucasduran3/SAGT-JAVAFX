/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.controller;

import com.mvcjava.sagt.javafx.async.SaleDetailLoadService;
import com.mvcjava.sagt.javafx.async.SaleLoadService;
import com.mvcjava.sagt.javafx.async.SaleSaveService;
import com.mvcjava.sagt.javafx.dto.DetailSaleWithProduct;
import com.mvcjava.sagt.javafx.dto.HeaderSaleWithClient;
import com.mvcjava.sagt.javafx.enums.PaymentMethod;
import com.mvcjava.sagt.javafx.util.AlertUtils;
import com.mvcjava.sagt.javafx.util.DatePickerTableCell;
import com.mvcjava.sagt.javafx.util.EditableCellFactory;
import com.mvcjava.sagt.javafx.viewmodel.DetailSaleViewModel;
import com.mvcjava.sagt.javafx.viewmodel.SaleViewModel;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 *
 * @author lucas
 */
public class SalesController {
    @FXML
    private TableView<SaleViewModel> salesTable;
    
    @FXML
    private Label detailsLabel;
    
    @FXML
    private TableView<DetailSaleViewModel> detailTable;
    
    //Columnas Master
    private TableColumn<SaleViewModel, String> colBillNumber;
    private TableColumn<SaleViewModel, LocalDate> colDate;
    private TableColumn<SaleViewModel, String> colClient;
    private TableColumn<SaleViewModel, Number> colTotal;
    private TableColumn<SaleViewModel, PaymentMethod> colPaymentMethod;
    
    //Columnas Detail
    private TableColumn<DetailSaleViewModel, String> colProduct;
    private TableColumn<DetailSaleViewModel, Number> colAmmount;
    private TableColumn<DetailSaleViewModel, Number> colUnitPrice;
    private TableColumn<DetailSaleViewModel, Number> colSubtotal;
    
    private ObservableList<SaleViewModel> saleViewModels;
    private ObservableList<DetailSaleViewModel> detailViewModels;
    
    //cambios pendientes
    private Map<UUID, Map<String, Object>> headersToUpdate;
    private Map<UUID, Map<String, Object>> detailsToUpdate;
    
    //async
    private SaleLoadService saleLoadService;
    private SaleDetailLoadService detailLoadService;
    private SaleSaveService saleSaveService;
    
    private SaleViewModel currentSale;
    
    @FXML
    public void initialize() {
        currentSale = null;
        
        saleViewModels = FXCollections.observableArrayList();
        detailViewModels = FXCollections.observableArrayList();
        
        headersToUpdate = new HashMap<>();
        detailsToUpdate = new HashMap<>();
                
        saleLoadService = new SaleLoadService();
        detailLoadService = new SaleDetailLoadService();
        saleSaveService = new SaleSaveService();
        
        setupMasterColumns();
        setupDetailColumns();
        
        salesTable.setItems(saleViewModels);
        detailTable.setItems(detailViewModels);
        
        salesTable.setEditable(true);
        detailTable.setEditable(true);
        
        salesTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        currentSale = newVal;
                        loadDetail(newVal.getId(), newVal.getBillNumber());
                    } else {
                        currentSale = null;
                        detailViewModels.clear();
                        detailsLabel.setText("Selecciona una venta para ver su detalle");
                    }
                });
        
        loadHeaders();
    }
    
    private void setupMasterColumns() {
        colBillNumber = new TableColumn<>("Nº Factura");
        colBillNumber.setPrefWidth(150);
        colBillNumber.setEditable(true);
        colBillNumber.setUserData("numero_factura");
        colBillNumber.setCellValueFactory(data -> data.getValue().billNumberProperty());
        colBillNumber.setCellFactory(TextFieldTableCell.forTableColumn());
        colBillNumber.setOnEditCommit(e -> handleHeaderStringEdit(e, "numero_factura"));
 
        // Fecha – solo lectura por ahora
        colDate = new TableColumn<>("Fecha");
        colDate.setUserData("fecha");
        colDate.setPrefWidth(170);
        colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDate().toLocalDate()));
        colDate.setCellFactory(DatePickerTableCell.forTableColumn());
        colDate.setOnEditCommit(this::handleDateEdit);
        colDate.setEditable(true);
 
        // Cliente – solo lectura por ahora
        colClient = new TableColumn<>("Cliente");
        colDate.setUserData("id_cliente");
        colClient.setPrefWidth(220);
        colClient.setCellValueFactory(data -> data.getValue().clientNameProperty());
 
        // Total – solo lectura (se recalcula en el detalle)
        colTotal = new TableColumn<>("Total");
        colTotal.setUserData("total");
        colTotal.setPrefWidth(110);
        colTotal.setCellValueFactory(data -> data.getValue().totalProperty());
 
        // Método de pago – editable con ComboBox
        colPaymentMethod = new TableColumn<>("Método de pago");
        colPaymentMethod.setUserData("metodo_pago");
        colPaymentMethod.setPrefWidth(160);
        colPaymentMethod.setCellValueFactory(data -> data.getValue().paymentMethodProperty());
        colPaymentMethod.setCellFactory(ComboBoxTableCell.forTableColumn(PaymentMethod.values()));
        colPaymentMethod.setOnEditCommit(this::handlePaymentMethodEdit);
 
        salesTable.getColumns().addAll(
                colBillNumber, colDate, colClient, colTotal, colPaymentMethod);
    }
    
    private void setupDetailColumns() {
        // Producto – solo lectura por ahora
        colProduct = new TableColumn<>("Producto");
        colProduct.setUserData("id_producto");
        colProduct.setPrefWidth(240);
        colProduct.setCellValueFactory(data -> data.getValue().productNameProperty());
 
        colAmmount = new TableColumn<>("Cantidad");
        colAmmount.setUserData("cantidad");
        colAmmount.setPrefWidth(100);
        colAmmount.setCellValueFactory(data -> data.getValue().ammountProperty());
        colAmmount.setCellFactory(EditableCellFactory.forNumber(
                v -> v > 0, 
                "La cantidad debe ser mayor a 0.",
                (vm, value) -> {
                    vm.ammountProperty().set(value.intValue()); 
                    vm.recalculateSubtotal(); 
                    recalculateTotal(vm.getDetail().getSaleId());
                    registerDetailUpdate(vm.getId(), "subtotal", vm.getSubtotal());},
                false)
        );
        colAmmount.setOnEditCommit(this::handleDetailNumberEdit);

        colUnitPrice = new TableColumn<>("Precio unitario");
        colUnitPrice.setUserData("precio_unitario");
        colUnitPrice.setPrefWidth(150);
        colUnitPrice.setCellValueFactory(data -> data.getValue().unitPriceProperty());
        colUnitPrice.setCellFactory(EditableCellFactory.forNumber(
                v -> v > 0,
                "El precio unitario debe ser mayor a 0.",
                (vm, value) -> {
                    vm.unitPriceProperty().set(value.floatValue());
                    vm.recalculateSubtotal();
                    recalculateTotal(vm.getDetail().getSaleId());
                    registerDetailUpdate(vm.getId(), "subtotal", vm.getSubtotal());},
                true)
        );
        colUnitPrice.setOnEditCommit(this::handleDetailNumberEdit);
 
        // Subtotal – calculado, solo lectura
        colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setUserData("subtotal");
        colSubtotal.setPrefWidth(130);
        colSubtotal.setCellValueFactory(data -> data.getValue().subtotalProperty());
 
        detailTable.getColumns().addAll(
                colProduct, colAmmount, colUnitPrice, colSubtotal);
    }
    
    private void handleDateEdit(TableColumn.CellEditEvent<SaleViewModel, LocalDate> e) {
        LocalDate newValue = e.getNewValue();
        
        if (newValue == null) {
            salesTable.refresh();
            return;
        }
        
        if (newValue.isAfter(LocalDate.now())) {
            AlertUtils.showError("La fecha de la venta no puede ser posterior a la fecha actual");
            salesTable.refresh();
            return;
        }
        
        SaleViewModel vm = e.getRowValue();
        
        //Comparar contra el valor actual del viewModel por que getOldValue ya puede reflejar el nuevo valor
        LocalDate current = vm.getDate() != null ? vm.getDate().toLocalDate() : null;
        if (newValue.equals(current)) {
            return;
        }
        
        vm.dateProperty().set(Date.valueOf(newValue));
        registerHeaderUpdate(vm.getId(), "fecha", Date.valueOf(newValue));
    }
    
    private void handleHeaderStringEdit(TableColumn.CellEditEvent<SaleViewModel, String> e, String dbField) {
        String newValue = normalize(e.getNewValue());
        String oldValue = normalize(e.getOldValue());
        
        if (newValue.equals(oldValue)) return;
        
        if (newValue.isEmpty()) {
            AlertUtils.showError("El campo no puede estar vacío.");
            salesTable.refresh();
            return;
        }
        if (dbField.equals("numero_factura") && newValue.length() > 20) {
            AlertUtils.showError("El número de factura no puede superar los 20 carácteres.");
            salesTable.refresh();
            return;
        }
        
        SaleViewModel vm = e.getRowValue();
        vm.billNumberProperty().set(newValue);
        registerHeaderUpdate(vm.getId(), dbField, newValue);
    }
    
    private void handlePaymentMethodEdit(TableColumn.CellEditEvent<SaleViewModel, PaymentMethod> e) {
        PaymentMethod newValue = e.getNewValue();
        PaymentMethod oldValue = e.getOldValue();
        
        if (newValue == null || newValue == oldValue) return;
        
        SaleViewModel vm = e.getRowValue();
        vm.paymentMethodProperty().set(newValue);
        
        registerHeaderUpdate(vm.getId(), "metodo_pago", newValue.name());
    }
    
    private void handleDetailNumberEdit(TableColumn.CellEditEvent<DetailSaleViewModel, Number> e) {
        Number newValue = e.getNewValue();
        Number oldValue = e.getOldValue();
        
        if (newValue == null || newValue == oldValue) return;
        
        DetailSaleViewModel vm = e.getRowValue();
        String columnName = e.getTableColumn().getUserData().toString();
        
        registerDetailUpdate(vm.getId(), columnName, newValue);
    }
    
    private void registerHeaderUpdate(UUID id, String field, Object value) {
        headersToUpdate.computeIfAbsent(id, k -> new HashMap<>()).put(field, value);
    }
    
    private void registerDetailUpdate(UUID id, String field, Object value) {
        detailsToUpdate.computeIfAbsent(id, k -> new HashMap<>()).put(field, value);
    }
    
    private void recalculateTotal(UUID saleId) {
        if (saleId == null) return;

        float total = detailViewModels.stream()
            .filter(e -> saleId.equals(e.getDetail().getSaleId()))
            .map(DetailSaleViewModel::getSubtotal)
            .reduce(0.00f, Float::sum);

        System.out.println("Total calculado: " + total + " tamaño de lista " + detailViewModels.stream()
            .filter(e -> saleId.equals(e.getDetail().getSaleId())).collect(Collectors.toList()).size());
    
        currentSale.totalProperty().set(total);
        
        registerHeaderUpdate(saleId, "total", total);
    }
    
    private void loadHeaders() {
        if (saleLoadService.isRunning()) return;
 
        saleLoadService.reset();
 
        saleLoadService.setOnSucceeded(e -> {
            List<HeaderSaleWithClient> cabeceras = saleLoadService.getValue();
            List<SaleViewModel> vms = cabeceras.stream()
                    .map(SaleViewModel::new)
                    .collect(Collectors.toList());
            saleViewModels.setAll(vms);
        });
 
        saleLoadService.setOnFailed(e ->
            AlertUtils.showError(saleLoadService.getException().getMessage()));
 
        saleLoadService.start();
    }
    
    private void loadDetail(UUID saleId, String billNumber) {
        detailsLabel.setText("Cargando detalle…");
        detailViewModels.clear();
 
        detailLoadService.setSaleId(saleId);
        detailLoadService.reset();
 
        detailLoadService.setOnSucceeded(e -> {
            List<DetailSaleWithProduct> items = detailLoadService.getValue();
            List<DetailSaleViewModel> vms = items.stream()
                    .map(DetailSaleViewModel::new)
                    .collect(Collectors.toList());
            detailViewModels.setAll(vms);
            detailsLabel.setText("Detalle de venta  " + billNumber
                    + "  (" + vms.size() + " ítem" + (vms.size() != 1 ? "s" : "") + ")");
        });
 
        detailLoadService.setOnFailed(e -> {
            detailsLabel.setText("Error al cargar el detalle");
            AlertUtils.showError(detailLoadService.getException().getMessage());
        });
 
        detailLoadService.start();
    }
    
    @FXML
    public void handleReload() {
        if (!headersToUpdate.isEmpty() || !detailsToUpdate.isEmpty()) {
            Optional<ButtonType> confirm = AlertUtils.showConfirmAlert(
                    "Hay cambios sin guardar",
                    "Si recargás, perderás los cambios no guardados. ¿Continuar?");
            if (confirm.isEmpty() || confirm.get() != ButtonType.OK) return;
        }
 
        salesTable.getSelectionModel().clearSelection();
        detailViewModels.clear();
        detailsLabel.setText("Seleccioná una venta para ver su detalle");
        saleViewModels.clear();
        headersToUpdate.clear();
        detailsToUpdate.clear();
 
        loadHeaders();
    }
    
    @FXML
    public void handleSaveChanges() {
        if (headersToUpdate.isEmpty() && detailsToUpdate.isEmpty()) {
            AlertUtils.showError("No hay cambios pendientes para guardar.");
            return;
        }
 
        Optional<ButtonType> confirm = AlertUtils.showConfirmAlert(
                "Guardar cambios",
                "¿Desea guardar los cambios en la base de datos?");
 
        confirm.ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                saveData();
            }
        });
    }
    
    private void saveData() {
        if (saleSaveService.isRunning()) return;
 
        saleSaveService.reset();
        saleSaveService.setData(
                new HashMap<>(headersToUpdate),
                new HashMap<>(detailsToUpdate));
 
        int totalHeaders = headersToUpdate.size();
        int totalDetails = detailsToUpdate.size();
 
        saleSaveService.setOnSucceeded(e -> {
            headersToUpdate.clear();
            detailsToUpdate.clear();
 
            AlertUtils.showSuccess(
                    "Operación exitosa",
                    "Cambios guardados con éxito",
                    "Ventas (cabecera) actualizadas: " + totalHeaders
                    + "\nÍtems de detalle actualizados: " + totalDetails);
 
            loadHeaders();
        });
 
        saleSaveService.setOnFailed(e ->
                AlertUtils.showError(saleSaveService.getException().getMessage()));
 
        saleSaveService.start();
    }
    
    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
