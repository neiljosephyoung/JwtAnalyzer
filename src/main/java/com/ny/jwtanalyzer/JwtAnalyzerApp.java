package com.ny.jwtanalyzer;

import atlantafx.base.theme.PrimerDark;
import com.pixelduke.window.ThemeWindowManagerFactory;
import com.pixelduke.window.Win11ThemeWindowManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class JwtAnalyzerApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(JwtAnalyzerApp.class.getResource("main-view.fxml"));
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        Scene scene = new Scene(fxmlLoader.load(), 1080, 800);
        stage.setTitle("Jwt Analyzer");
        stage.setScene(scene);
        stage.initStyle(StageStyle.DECORATED); // Customizing window style


        Image applicationIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/lock.png")));
        stage.getIcons().add(applicationIcon);
        stage.show();

        // set dark-mode to local window
        Win11ThemeWindowManager manager = (Win11ThemeWindowManager) ThemeWindowManagerFactory.create();
        manager.setDarkModeForWindowFrame(stage, true);

    }

    public static void main(String[] args) {
        launch();
    }
}