/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.controller;

import com.mvcjava.sagt.javafx.dao.model.Client;
import com.mvcjava.sagt.javafx.dto.SaleHeaderFormData;
import com.mvcjava.sagt.javafx.enums.PaymentMethod;
import com.mvcjava.sagt.javafx.util.AlertUtils;
import com.mvcjava.sagt.javafx.util.BasicStringValidator;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
public class SaleHeaderFormController {
    @FXML
    private TextField billNumberField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Client> clientComboBox;
    @FXML
    private ComboBox<PaymentMethod> paymentMethodComboBox;
    
    @FXML
    private Label errorBillNumber;
    @FXML
    private Label errorDate;
    @FXML
    private Label errorClient;
    @FXML
    private Label errorPaymentMethod;
    
    public static SaleHeaderFormData showForm(Window owner, List<Client> clients) {
        try {
            FXMLLoader loader = new FXMLLoader (
                    SaleHeaderFormController.class.getResource("/com/mvcjava/sagt/javafx/view/saleHeaderForm.fxml")
            );
            
            SaleHeaderFormController controller = new SaleHeaderFormController();
            loader.setController(controller);
            
            DialogPane dialogPane = loader.load();
            Dialog<SaleHeaderFormData> dialog = new Dialog<>();
            dialog.setTitle("Nueva venta");
            dialog.initOwner(owner);
            dialog.setDialogPane(dialogPane);
            
            controller.setData(clients);
            
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
            AlertUtils.showError("Error al abrir el formulario de venta: " + ex.getMessage());
            return null;
        }
    }
    
    @FXML
    public void initialize() {
        clientComboBox.setCellFactory(lv -> new ListCell<Client>() {
            @Override
            protected void updateItem(Client item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getCompanyName());
            }
        });
        clientComboBox.setButtonCell(new ListCell<Client>() {
            @Override
            protected void updateItem(Client item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getCompanyName());
            }
        });
        
        paymentMethodComboBox.getItems().setAll(PaymentMethod.values());
        
        datePicker.setValue(LocalDate.now());
        
        clearErrors();
    }
    
    private void setData(List<Client> clients) {
        clientComboBox.getItems().setAll(clients);
    }
    
    private void clearErrors() {
        errorBillNumber.setText("");
        errorDate.setText("");
        errorClient.setText("");
        errorPaymentMethod.setText("");
    }
    
    private boolean isValid() {
        clearErrors();
        boolean valid = true;
        
        try {
            BasicStringValidator.validate(billNumberField.getText().trim(), 1, 20, "Número de factura");
        } catch (IllegalArgumentException ex) {
            errorBillNumber.setText(ex.getMessage());
            valid = false;
        }
        
        if (datePicker.getValue() == null) {
            errorDate.setText("La fecha es obligatoria.");
            valid = false;
        } else if (datePicker.getValue().isAfter(LocalDate.now())) {
            errorDate.setText("La fecha no puede ser futura.");
            valid = false;
        }
        
        if (clientComboBox.getValue() == null) {
            errorClient.setText("Seleccione un cliente");
            valid = false;
        }
        
        if (paymentMethodComboBox.getValue() == null) {
            errorPaymentMethod.setText("Seleccione un método de pago.");
            valid = false;
        }
        
        return valid;
    }
    
    private SaleHeaderFormData buildResult() {
        return new SaleHeaderFormData(
                billNumberField.getText().trim(),
                datePicker.getValue(),
                clientComboBox.getValue(),
                paymentMethodComboBox.getValue()
        );
    }
}
