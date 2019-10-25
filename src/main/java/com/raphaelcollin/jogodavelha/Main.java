package com.raphaelcollin.jogodavelha;

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

    // Constantes
    private static final String URL_ARQUIVO_CONFIGURACOES = "config/config.properties";
    private static final String URL_BUNDLE_IDIOMA ="lang";
    private static final String URL_ESTILO_CSS = "/estilo.css";
    private static final String URL_JANELA_PRINCIPAL = "/janela_principal.fxml";
    private static final String URL_ICONE = "file:img/icon.png";

    private static final String PROPRIEDADE_IDIOMA = "idioma";
    private static final String PROPRIEDADE_VOLUME = "volume";

    private static final String COMENTARIO_ARQUIVO_CONFIGURACOES = "Últimas Configurações - Jogo da Velha";
    private static final String BUNDLE_STRING_KEY_STAGE_TITLE = "stage_title";

    // Controller
    private Controller controller;

    @Override
    public void start(Stage stage) throws Exception{

        // Será lido o arquivo config.properties que contém as últimas configurações utilizadas pelo usuário

        Properties properties = new Properties();
        String ultimoIdiomaUsado; // Variável que conterá o último idioma utilizado
        double volumeAudio ; // Variávle que conterá o último volume utlizado pelo usuário

        try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream(URL_ARQUIVO_CONFIGURACOES))) {
            properties.load(reader);
            ultimoIdiomaUsado = properties.getProperty(PROPRIEDADE_IDIOMA);
            volumeAudio = Double.parseDouble(properties.getProperty(PROPRIEDADE_VOLUME).replace(",","."));
        }

        // ResourceBundle - Será usado para carregar os textos dos controles
        Locale locale = new Locale(ultimoIdiomaUsado);
        ResourceBundle resources = ResourceBundle.getBundle(URL_BUNDLE_IDIOMA,locale);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(URL_JANELA_PRINCIPAL), resources);
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        controller.getSliderVolume().setValue(volumeAudio);

        // Obtendo item do Combo Box Correspondente ao idioma

        int posicao = 0;
        for (int i = 0; i < controller.getIdiomaIconeComboBox().getItems().size(); i++) {
            if (controller.getIdiomaIconeComboBox().getItems().get(i).getIdioma().equals(ultimoIdiomaUsado)) {
                posicao = i;
                break;
            }
        }

        controller.getIdiomaIconeComboBox().getSelectionModel().select(posicao);

        // Mantendo a proporção da tela

        Rectangle2D tamanhoTela = Screen.getPrimary().getBounds();
        double width = tamanhoTela.getWidth() * 0.35;
        double height = tamanhoTela.getWidth() * 0.35 * 0.70714;

        stage.setScene(new Scene(root,width,height));
        stage.initStyle(StageStyle.UNIFIED); // Bordas Brancas - Windows 10
        stage.setTitle(resources.getString(BUNDLE_STRING_KEY_STAGE_TITLE));
        stage.getScene().getStylesheets().add(getClass().getResource(URL_ESTILO_CSS).toExternalForm());
        stage.getIcons().add(new Image(URL_ICONE));
        stage.setResizable(false);
        stage.show();
    }

    /* Este método será executado quando a aplicação for finalizada. Nesse caso, serão salvos no arquivo config.properties
    * o valor do volume e o idioma. Assim, quando o usuário executar novamente, a aplicação estará com as últimas configurações utilizadas. */

    @Override
    public void stop() throws Exception {
        super.stop();

        Properties properties = new Properties();
        try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(URL_ARQUIVO_CONFIGURACOES))) {
            if (controller != null) {
                properties.setProperty(PROPRIEDADE_IDIOMA, controller.getResources().getLocale().getLanguage());
                properties.setProperty(PROPRIEDADE_VOLUME, String.format("%f", controller.getSliderVolume().getValue()));
                properties.store(writer, COMENTARIO_ARQUIVO_CONFIGURACOES);
            }

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
