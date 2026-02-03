module com.mvcjava.sagt.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;

    opens com.mvcjava.sagt.javafx to javafx.fxml;
    exports com.mvcjava.sagt.javafx;
}
