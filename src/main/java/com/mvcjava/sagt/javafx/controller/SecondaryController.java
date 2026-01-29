package com.mvcjava.sagt.javafx.controller;

import com.mvcjava.sagt.javafx.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}