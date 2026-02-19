package com.mvcjava.sagt.javafx.controller;

import com.mvcjava.sagt.javafx.util.AlertUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

public class CheckBoxDialogController<T> {

    @FXML private VBox checkBoxContainer;
    @FXML private Label instructionLabel;

    private Map<T, CheckBox> checkBoxMap = new LinkedHashMap<>();

    public static <T> List<T> showDialog(Window owner, String windowTitle, String headerText, String instructionText, List<T> availableItems, List<T> currentItems) {
        try {
            FXMLLoader loader = new FXMLLoader(CheckBoxDialogController.class.getResource("/com/mvcjava/sagt/javafx/view/checkBoxDialog.fxml"));
            
            CheckBoxDialogController<T> controller = new CheckBoxDialogController<>();
            loader.setController(controller);

            DialogPane dialogPane = loader.load();
            Dialog<List<T>> dialog = new Dialog<>();
            dialog.setTitle(windowTitle);
            dialog.initOwner(owner);
            dialog.setDialogPane(dialogPane);
            dialog.setHeaderText(headerText);

            controller.setData(instructionText, availableItems, currentItems);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    return controller.getSelectedItems();
                }
                return null; // Si es Cancel o cerrar ventana
            });

            return dialog.showAndWait().orElse(null);

        } catch (IOException ex) {
            ex.printStackTrace();
            AlertUtils.showError("Error al abrir diálogo: " + ex.getMessage());
            return null;
        }
    }

    private void setData(String instructionText, List<T> availableItems, List<T> currentItems) {
        instructionLabel.setText(instructionText);
        checkBoxMap.clear();
        checkBoxContainer.getChildren().clear();

        for (T item : availableItems) {
            CheckBox checkbox = new CheckBox(item.toString());
            
            if (currentItems.contains(item)) {
                checkbox.setSelected(true);
            }
            
            checkBoxContainer.getChildren().add(checkbox);
            checkBoxMap.put(item, checkbox);
        }
    }
    
    private List<T> getSelectedItems() {
        List<T> selected = new ArrayList<>();
        checkBoxMap.forEach((item, checkbox) -> {
            if (checkbox.isSelected()) {
                selected.add(item);
            }
        });
        return selected;
    }
}