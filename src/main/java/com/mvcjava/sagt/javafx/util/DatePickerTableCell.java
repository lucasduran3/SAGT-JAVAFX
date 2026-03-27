/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.util;

import java.time.LocalDate;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 *
 * @author lucas
 */
public class DatePickerTableCell<S> extends TableCell<S, LocalDate> {
    private DatePicker datePicker;
    
    public DatePickerTableCell() {
        this.datePicker = new DatePicker();
        
        datePicker.setOnAction(e -> {
            LocalDate selected = datePicker.getValue();
            if (isEditing() && selected != null) {
                commitEdit(selected);
            }
        });
        
        //Cancelar si el usuario cierra el popup sin seleccionar
        datePicker.getEditor().focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused() && isEditing()) cancelEdit();
        });
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (getItem() != null) {
            datePicker.setValue(getItem());
        }
        setGraphic(datePicker);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        datePicker.show();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem() == null ? "" : getItem().toString());
        setGraphic(null);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }
    
    @Override
    public void commitEdit(LocalDate date) {
        super.commitEdit(date);
        setGraphic(null);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    protected void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else if (isEditing()){
            datePicker.setValue(item);
            setText(null);
            setGraphic(datePicker);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        } else {
            setText(item == null ? "" : item.toString());
            setGraphic(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
    }
    
    public static <E> Callback<TableColumn<E, LocalDate>, TableCell<E, LocalDate>> forTableColumn() {
        return column -> new DatePickerTableCell<>();
    }
}
