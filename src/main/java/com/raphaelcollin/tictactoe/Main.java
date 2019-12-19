package com.raphaelcollin.tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Main extends Application {

    private static final String URL_CONFIG_FILE = "config/config.properties";
    private static final String URL_BUNDLE_LANGUAGE ="lang";
    private static final String URL_STYLE = "/style.css";
    private static final String URL_VIEW = "/view.fxml";
    private static final String URL_ICON = "file:img/icon.png";

    private static final String PROPERTY_LANGUAGE = "language";
    private static final String PROPERTY_VOLUME = "volume";

    private static final String COMMENT_CONFIG_FILE = "Last Configuration - Tic-Tac-Toe";
    private static final String BUNDLE_STRING_KEY_STAGE_TITLE = "stage_title";

    private Controller controller;

    @Override
    public void start(Stage stage) throws Exception{


        Properties properties = new Properties();
        String lastLanguageUsed;
        double volumeAudio ;

        try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream(URL_CONFIG_FILE))) {
            properties.load(reader);
            lastLanguageUsed = properties.getProperty(PROPERTY_LANGUAGE);
            volumeAudio = Double.parseDouble(properties.getProperty(PROPERTY_VOLUME).replace(",","."));
        }

        Locale locale = new Locale(lastLanguageUsed);
        ResourceBundle resources = ResourceBundle.getBundle(URL_BUNDLE_LANGUAGE,locale);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(URL_VIEW), resources);
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        controller.getSliderVolume().setValue(volumeAudio);


        int position = 0;
        for (int i = 0; i < controller.getLanguageIconComboBox().getItems().size(); i++) {
            if (controller.getLanguageIconComboBox().getItems().get(i).getLanguage().equals(lastLanguageUsed)) {
                position = i;
                break;
            }
        }

        controller.getLanguageIconComboBox().getSelectionModel().select(position);

        Rectangle2D screenSize = Screen.getPrimary().getBounds();
        double width = screenSize.getWidth() * 0.35;
        double height = screenSize.getWidth() * 0.35 * 0.74444;

        stage.setScene(new Scene(root,width,height));
        stage.setTitle(resources.getString(BUNDLE_STRING_KEY_STAGE_TITLE));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getScene().getStylesheets().add(getClass().getResource(URL_STYLE).toExternalForm());
        stage.getIcons().add(new Image(URL_ICON));
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        Properties properties = new Properties();
        try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(URL_CONFIG_FILE))) {
            if (controller != null) {
                properties.setProperty(PROPERTY_LANGUAGE, controller.getResources().getLocale().getLanguage());
                properties.setProperty(PROPERTY_VOLUME, String.format("%f", controller.getSliderVolume().getValue()));
                properties.store(writer, COMMENT_CONFIG_FILE);
            }

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
