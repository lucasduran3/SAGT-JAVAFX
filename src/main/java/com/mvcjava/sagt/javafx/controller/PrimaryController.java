package com.mvcjava.sagt.javafx.controller;

import com.mvcjava.sagt.javafx.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
