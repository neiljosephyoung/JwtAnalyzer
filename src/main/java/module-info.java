module com.ny.jwtanalyzer {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires com.auth0.jwt;
    requires com.pixelduke.fxthemes;
    requires com.github.oshi;
    requires static lombok;
    requires org.slf4j;
    requires tools.jackson.databind;
    requires org.apache.commons.io;

    opens com.ny.jwtanalyzer to javafx.fxml;
    exports com.ny.jwtanalyzer;
}