/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.controller;

import com.mvcjava.sagt.javafx.dao.model.Product;
import com.mvcjava.sagt.javafx.dto.SaleDetailFormData;
import com.mvcjava.sagt.javafx.util.AlertUtils;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Window;

/**
 *
 * @author lucas
 */
public class SaleDetailFormController {
    @FXML
    private ComboBox<Product> productComboBox;
    @FXML
    private TextField unitPriceField;
    @FXML
    private TextField ammountField;
    @FXML
    private TextField subtotalField;
    
    @FXML
    private Label errorProduct;
    @FXML
    private Label errorUnitPrice;
    @FXML
    private Label errorAmmount;
    
    public static SaleDetailFormData showForm(Window owner, List<Product> products, String billNumber) {
        try {
            FXMLLoader loader = new FXMLLoader(SaleDetailFormController.class.getResource("/com/mvcjava/sagt/javafx/view/saleDetailForm.fxml"));
            
            SaleDetailFormController controller = new SaleDetailFormController();
            loader.setController(controller);
            
            DialogPane dialogPane = loader.load();
            Dialog<SaleDetailFormData> dialog = new Dialog();
            dialog.setTitle("Agregar item - Venta " + billNumber);
            dialog.initOwner(owner);
            dialog.setDialogPane(dialogPane);
            
            controller.setData(products);
            
            Button btnOK = (Button) dialogPane.lookupButton(ButtonType.OK);
            btnOK.addEventFilter(ActionEvent.ACTION, e -> {
                if (!controller.isValid()) {
                    e.consume();
                }
            });
            
            dialog.setResultConverter(btn -> {
                if (btn == ButtonType.OK) {
                    return controller.buildResult();
                }
                return null;
            });
            
            return dialog.showAndWait().orElse(null);
        } catch (IOException ex) {
            ex.printStackTrace();
            AlertUtils.showError("Error al abrir el formulario de ítem: " + ex.getMessage());
            return null;
        }
    }
    
    @FXML
    public void initialize() {
        productComboBox.setCellFactory(lv -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " - " + item.getBrand() + " " + item.getModel());
                }
            }
        });
        productComboBox.setButtonCell(new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty); 
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " - " + item.getBrand() + " - " + item.getModel());
                }
            }
            
        });
        
        productComboBox.getSelectionModel().selectedItemProperty().addListener(
        (obs, oldVal, newVal) -> {
           if (newVal != null) {
               unitPriceField.setText(String.format("%.2f", newVal.getSalePrice()));
               recalcSubtotal();
           } else {
               unitPriceField.clear();
               subtotalField.clear();
           } 
        });
        
        ammountField.textProperty().addListener((obs, oldVal, newVal) -> recalcSubtotal());
        
        clearErrors();
    }
    
    private void setData(List<Product> products) {
        productComboBox.getItems().setAll(products);
    }
    
    private void recalcSubtotal() {
        try {
            float price = Float.parseFloat(unitPriceField.getText()
                    .replace(".", "")
                    .replace(",", ".")
                    .trim());
            int ammount = Integer.parseInt(ammountField.getText().trim());
            subtotalField.setText(String.format("%.2f", price * ammount));
        } catch (NumberFormatException ex ) {
            subtotalField.clear();
        }
    }
    
    private void clearErrors() {
        errorProduct.setText("");
        errorUnitPrice.setText("");
        errorAmmount.setText("");
    }
    
    private boolean isValid() {
        clearErrors();
        boolean valid = true;
        
        if (productComboBox.getValue() == null) {
            errorProduct.setText("Seleccione un producto.");
            valid = false;
        }
        
        float unitPrice = -1;
        try {
            String value = unitPriceField.getText().replace(".", "").replace(",", "");
            unitPrice = Float.parseFloat(value.trim());
            if (unitPrice <= 0) {
                errorUnitPrice.setText("El precio debe ser mayor a 0.");
                valid = false;
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            errorUnitPrice.setText("Precio inválido.");
            valid = false;
        }
        
        try {
            int ammount = Integer.parseInt(ammountField.getText().trim());
            if (ammount <= 0) {
                errorAmmount.setText("La cantidad debe ser mayor a 0.");
                valid = false;
            }
        } catch (NumberFormatException ex) {
            errorAmmount.setText("Ingrese un número entero válido.");
            valid = false;
        }
        
        return valid;
    }
    
    private SaleDetailFormData buildResult() {
        Product product = productComboBox.getValue();
        float price = Float.parseFloat(unitPriceField.getText().replace(".", "").replace(",", ".").trim());
        int ammount = Integer.parseInt(ammountField.getText().trim());
        float subtotal = price * ammount;
        
        return new SaleDetailFormData(product, price, ammount, subtotal);
    }
}
