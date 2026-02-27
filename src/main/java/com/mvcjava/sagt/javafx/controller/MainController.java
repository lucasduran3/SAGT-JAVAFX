/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.controller;

import com.mvcjava.sagt.javafx.util.AlertUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;

/**
 *
 * @author lucas
 */
public class MainController {
    private Map<String, String> routes;
    
    @FXML
    private ToggleGroup menuGroup;
    
    @FXML
    private StackPane dynamicContentContainer;
    
    @FXML
    public void initialize() {
        initializeRoutes();
        setupSideMenuListener();
    }
    
    private void initializeRoutes() {
        routes = new HashMap<>();
        routes.put("productos", "/com/mvcjava/sagt/javafx/view/productsView.fxml");
    }
    
    private void setupSideMenuListener() {
        menuGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                //forzar seleccion anterior al deseleccionar
                menuGroup.selectToggle(oldToggle);
            } else {
                String routeKey = ((ToggleButton)newToggle).getText().toLowerCase();
                String route = routes.get(routeKey);
                
                try {
                    loadView(route);
                } catch (IOException ex) {
                    AlertUtils.showError("Error al cargar vista " + routeKey);
                    menuGroup.selectToggle(oldToggle);
                }
            }
        });
    }
    
    private void loadView(String route) throws IOException{
        if (route != null && !route.trim().isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(route));
            Node view = loader.load();
            dynamicContentContainer.getChildren().setAll(view);    
        }
    }
}
