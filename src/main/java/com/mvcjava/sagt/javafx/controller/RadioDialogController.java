/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.controller;

import com.mvcjava.sagt.javafx.util.AlertUtils;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

/**
 *
 * @author lucas
 */
public class RadioDialogController <T> {
    
    @FXML
    private TextField searchField;
    @FXML
    private VBox listContainer;
    @FXML
    private Label selectionLabel;
    
    
    private ToggleGroup toggleGroup;
    private List<T> allItems;
    private Function<T, String> nameExtractor;
    private Function<T, UUID> idExtractor;

    private RadioDialogController() {}
    
   public static <T> T showDialog(
            Window owner,
            String title,
            List<T> items,
            Function<T, String> nameExtractor,
            Function<T, UUID>   idExtractor,
            UUID currentId) {
 
        try {
            FXMLLoader loader = new FXMLLoader(
                RadioDialogController.class.getResource(
                    "/com/mvcjava/sagt/javafx/view/radioSelectDialog.fxml"));
 
            RadioDialogController<T> controller = new RadioDialogController<>();
            loader.setController(controller);
 
            DialogPane dialogPane = loader.load();
 
            Dialog<T> dialog = new Dialog<>();
            dialog.setTitle(title);
            dialog.initOwner(owner);
            dialog.setDialogPane(dialogPane);
 
            controller.nameExtractor = nameExtractor;
            controller.idExtractor   = idExtractor;
            controller.populate(items, currentId);
 
            Button btnOK = (Button) dialogPane.lookupButton(ButtonType.OK);
            btnOK.setDisable(controller.getSelected() == null);
            controller.toggleGroup.selectedToggleProperty().addListener(
                (obs, o, n) -> btnOK.setDisable(n == null));
 
            dialog.setResultConverter(btn ->
                btn == ButtonType.OK ? controller.getSelected() : null);
 
            return dialog.showAndWait().orElse(null);
 
        } catch (IOException ex) {
            ex.printStackTrace();
            AlertUtils.showError("Error al abrir el diálogo de selección: " + ex.getMessage());
            return null;
        }
    }
   
   @FXML
   public void initialize() {
       toggleGroup = new ToggleGroup();
       
       searchField.textProperty().addListener((obs, oldText, newText) -> {
           filterRows(newText == null ? "" : newText.trim().toLowerCase());
       });
   }
   
   private void populate(List<T> items, UUID currentItemId) {
       this.allItems = items;
       listContainer.getChildren().clear();
       
       for (T item: items) {
           HBox row = buildRow(item, currentItemId);
           listContainer.getChildren().add(row);
       }
       
       updateSelectionLabel();
   }
   
   private HBox buildRow(T item, UUID currentItemId) {
       String name = nameExtractor.apply(item);
       UUID id = idExtractor.apply(item);
       
       RadioButton radio = new RadioButton();
       radio.setToggleGroup(toggleGroup);
       radio.setUserData(item);
       radio.setMaxWidth(40);
       radio.setMinWidth(40);
       
       if (currentItemId != null && currentItemId.equals(id)) {
           radio.setSelected(true);
           updateSelectionLabel(name);
       }
       
       radio.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
           if (isSelected) updateSelectionLabel(name);
       });
       
       Label nameLabel = new Label(name);
       nameLabel.setPrefWidth(250);
       nameLabel.setWrapText(true);
       nameLabel.setStyle("-fx-text-fill: #2d3436;");
       
       Label idLabel = new Label(id != null ? id.toString() : "-");
       idLabel.setStyle("-fx-text-fill: #a0a0a0; -fx-font-size: 11px;");
       HBox.setHgrow(idLabel, Priority.ALWAYS);
       
       HBox row = new HBox(10, radio, nameLabel, idLabel);
       row.setPadding(new Insets(8, 10, 8, 10));
       row.setUserData(item);
       applyRowStyle(row, false);
       
       row.setOnMouseClicked(e -> radio.setSelected(true));
       row.setOnMouseEntered(e -> applyRowStyle(row, true));
       row.setOnMouseExited(e -> applyRowStyle(row, false));
       
       return row;
   }
   
   private void filterRows(String query) {
       listContainer.getChildren().forEach(node -> {
            if (!(node instanceof HBox)) return;

           HBox row = (HBox) node;
           
           Object data = row.getUserData();
           boolean matches = query.isEmpty() ||
                   nameExtractor.apply((T) data).toLowerCase().contains(query) ||
                   idExtractor.apply((T) data).toString().toLowerCase().contains(query);
           
           row.setVisible(matches);
           row.setManaged(matches);
       });
   }
   
   private void updateSelectionLabel() {
       T selected = getSelected();
       if (selected != null) {
           updateSelectionLabel(nameExtractor.apply(selected));
       }
   }
   
   private void updateSelectionLabel(String name) {
       selectionLabel.setText("Seleccionado: " + name);
       selectionLabel.setStyle(
            "-fx-text-fill: #1E88E5; -fx-font-style: normal; -fx-font-weight: bold;");
   }
   
   //Helpers
   
   @SuppressWarnings("unchecked")
    private T getSelected() {
        if (toggleGroup == null || toggleGroup.getSelectedToggle() == null) return null;
        return (T) toggleGroup.getSelectedToggle().getUserData();
    }
    
    private static void applyRowStyle(HBox row, boolean hover) {
        String base = "-fx-border-color: transparent transparent #E9ECEF transparent; "
                    + "-fx-border-width: 1;";
        row.setStyle(hover
            ? "-fx-background-color: #E3F2FD; " + base + " -fx-cursor: hand;"
            : base);
    }
}
