/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.controller;

import com.mvcjava.sagt.javafx.async.SaleDetailLoadService;
import com.mvcjava.sagt.javafx.async.SaleLoadService;
import com.mvcjava.sagt.javafx.dto.DetailSaleWithProduct;
import com.mvcjava.sagt.javafx.dto.HeaderSaleWithClient;
import com.mvcjava.sagt.javafx.util.AlertUtils;
import com.mvcjava.sagt.javafx.viewmodel.DetailSaleViewModel;
import com.mvcjava.sagt.javafx.viewmodel.SaleViewModel;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
    private TableColumn<SaleViewModel, Timestamp> colDate;
    private TableColumn<SaleViewModel, String> colClient;
    private TableColumn<SaleViewModel, Number> colTotal;
    private TableColumn<SaleViewModel, String> colPaymentMethod;
    
    //Columnas Detail
    private TableColumn<DetailSaleViewModel, String> colProduct;
    private TableColumn<DetailSaleViewModel, Number> colAmmount;
    private TableColumn<DetailSaleViewModel, Number> colUnitPrice;
    private TableColumn<DetailSaleViewModel, Number> colSubtotal;
    
    private ObservableList<SaleViewModel> saleViewModels;
    private ObservableList<DetailSaleViewModel> detailViewModels;
    
    private SaleLoadService saleLoadService;
    private SaleDetailLoadService detailLoadService;
    
    @FXML
    public void initialize() {
        saleViewModels = FXCollections.observableArrayList();
        detailViewModels = FXCollections.observableArrayList();
        
        saleLoadService = new SaleLoadService();
        detailLoadService = new SaleDetailLoadService();
        
        setupMasterColumns();
        setupDetailColumns();
        
        salesTable.setItems(saleViewModels);
        detailTable.setItems(detailViewModels);
        
        salesTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        loadDetail(newVal.getId(), newVal.getBillNumber());
                    } else {
                        detailViewModels.clear();
                        detailsLabel.setText("Selecciona una venta para ver su detalle");
                    }
                });
        
        loadHeaders();
    }
    
    private void setupMasterColumns() {
        colBillNumber = new TableColumn<>("Nº Factura");
        colBillNumber.setPrefWidth(130);
        colBillNumber.setCellValueFactory(
                data -> data.getValue().billNumberProperty());
 
        colDate = new TableColumn<>("Fecha");
        colDate.setPrefWidth(160);
        colDate.setCellValueFactory(
                data -> new SimpleObjectProperty<Timestamp>(data.getValue().getDate()));
 
        colClient = new TableColumn<>("Cliente");
        colClient.setPrefWidth(220);
        colClient.setCellValueFactory(
                data -> data.getValue().clientNameProperty());
 
        colTotal = new TableColumn<>("Total");
        colTotal.setPrefWidth(110);
        colTotal.setCellValueFactory(
                data -> new SimpleObjectProperty<>(data.getValue().getTotal()));
 
        colPaymentMethod = new TableColumn<>("Método de pago");
        colPaymentMethod.setPrefWidth(140);
        colPaymentMethod.setCellValueFactory(
                data -> data.getValue().paymentMethodProperty());
 
        salesTable.getColumns().addAll(
                colBillNumber, colDate, colClient, colTotal, colPaymentMethod);
    }
    
    private void setupDetailColumns() {
        colProduct = new TableColumn<>("Producto");
        colProduct.setPrefWidth(240);
        colProduct.setCellValueFactory(
                data -> data.getValue().productNameProperty());
 
        colAmmount = new TableColumn<>("Cantidad");
        colAmmount.setPrefWidth(90);
        colAmmount.setCellValueFactory(
                data -> new SimpleObjectProperty<>(data.getValue().getAmmount()));
 
        colUnitPrice = new TableColumn<>("Precio unitario");
        colUnitPrice.setPrefWidth(140);
        colUnitPrice.setCellValueFactory(
                data -> new SimpleObjectProperty<>(data.getValue().getUnitPrice()));
 
        colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setPrefWidth(120);
        colSubtotal.setCellValueFactory(
                data -> new SimpleObjectProperty<>(data.getValue().getSubtotal()));
 
        detailTable.getColumns().addAll(
                colProduct, colAmmount, colUnitPrice, colSubtotal);
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
        // Limpiar selección y detalle antes de recargar el master
        salesTable.getSelectionModel().clearSelection();
        detailViewModels.clear();
        detailsLabel.setText("Seleccioná una venta para ver su detalle");
        saleViewModels.clear();
        loadHeaders();
    }
}
