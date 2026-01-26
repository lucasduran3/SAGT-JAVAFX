module com.mvcjava.sagt.javafx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mvcjava.sagt.javafx to javafx.fxml;
    exports com.mvcjava.sagt.javafx;
}
