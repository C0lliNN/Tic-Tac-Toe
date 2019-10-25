package com.raphaelcollin.jogodavelha;

import com.raphaelcollin.jogodavelha.subclasses.ImageListCell;
import com.raphaelcollin.jogodavelha.subclasses.LocatedImage;
import com.raphaelcollin.jogodavelha.subclasses.MatrizButton;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    // Controles
    @FXML
    private AnchorPane root;
    @FXML
    private Button jogarButton;
    @FXML
    private HBox hboxVelhaImageView;
    @FXML
    public ImageView velhaImageView;
    @FXML
    private ComboBox<LocatedImage> idiomaIconeComboBox;
    @FXML
    private Button audioIconeButton;
    @FXML
    private Slider sliderVolume;

    private Button reiniciarButton;
    private Button pausarOuRetomarButton;
    private Button cancelarButton;
    private ImageView indicadorVezImageView;
    private GridPane matrizGridPane;
    private Label tempoRestanteLabel;
    private Label tempoRestanteNumeroLabel;
    private Button playIconButton;

    // Constantes

    private static final String CLASSE_BUTTON_JOGO = "jogo-button";
    private static final String CLASSE_BUTTON_CAMPO = "campo-button";
    private static final String CLASSE_BUTTON_TOP = "top-button";

    private static final String ID_BUTTON_INICIAR = "iniciar-button";
    private static final String ID_BUTTON_AUDIO = "audio-button";
    private static final String ID_BUTTON_PLAY_ICON = "play-icon-button";
    private static final String ID_BUTTON_REINICIAR = "reiniciar-button";
    private static final String ID_BUTTON_PAUSAR_RETOMAR = "pausar-retomar-button";
    private static final String ID_BUTTON_CANCELAR = "cancelar-button";

    private static final String URL_IMAGEM_ICONE_X = "file:img/x-icon.png";
    private static final String URL_IMAGEM_VELHA = "file:img/grandmother-icon.png";
    private static final String URL_IMAGEM_ICONE_PLAY = "file:img/play-icon.png";
    private static final String URL_IMAGEM_ICONE_O = "file:img/o-icon.png";
    private static final String URL_IMAGEM_IDIOMA_BRASIL = "file:img/brasil-icon.png";
    private static final String URL_IMAGEM_IDIOMA_US = "file:img/us-icon.png";
    private static final String URL_IMAGEM_ICONE_AUDIO_MUTADO = "file:img/audio-icon-muted.png";
    private static final String URL_IMAGEM_ICONE_AUDIO_MINIMO = "file:img/audio-icon-low.png";
    private static final String URL_IMAGEM_ICONE_AUDIO_MEDIO = "file:img/audio-icon-medium.png";
    private static final String URL_IMAGEM_ICONE_AUDIO_MAXIMO = "file:img/audio-icon-full.png";
    private static final String URL_BUNDLE_IDIOMA = "lang";
    private static final String URL_MUSICA_FUNDO = "/music/background-music.mp3";
    private static final String URL_SOUND_EFFECT_BUTTON_CLICK = "/music/button-click.mp3";
    private static final String URL_SOUND_EFFECT_FIM_DE_JOGO = "/music/endofgame.mp3";
    private static final String URL_ERROR_SOUND_EFFECT = "/music/error.wav";

    private static final String IDIOMA_EN_LOCALE = "en";
    private static final String IDIOMA_PT_LOCALE = "pt";

    private static final String BUNDLE_STRING_KEY_STAGE_TITLE = "stage_title";
    private static final String BUNDLE_STRING_KEY_BUTTON_INICIAR = "iniciar_button";
    private static final String BUNDLE_STRING_KEY_ALERT_TITLE = "alert_title";
    private static final String BUNDLE_STRING_KEY_ALERT_TEMPOESGOTADO_HEADER_TEXT = "alert_tempoesgotado_headerText";
    private static final String BUNDLE_STRING_KEY_ALERT_EMPATE_HEADER_TEXT = "alert_empate_headerText";
    private static final String BUNDLE_STRING_KEY_ALERT_VENCEDOR_HEADER_TEXT = "alert_vencedor_headerText";
    private static final String BUNDLE_STRING_KEY_ALERT_CONTEXT_TEXT = "alert_contextText";
    private static final String BUNDLE_STRING_KEY_BUTTON_REINICIAR = "reiniciar_button";
    private static final String BUNDLE_STRING_KEY_BUTTON_PAUSAR = "pausar_button";
    private static final String BUNDLE_STRING_KEY_BUTTON_RETOMAR = "retomar_button";
    private static final String BUNDLE_STRING_KEY_BUTTON_CANCELAR = "cancelar_button";
    private static final String BUNDLE_STRING_KEY_LABEL_TEMPO_RESTANTE = "tempo_restante";

    private static final int TAMANHO_MATRIZ = 3;
    private static final int TEMPO_JOGO = 60;

    private static final String NOME_FONTE_BUTTONS = "Arial Bold";

    // Atributos de Controle

    private int contadorTempo = TEMPO_JOGO; // Tempo até o fim da partida

    private boolean vez; // false - X está com a vez; true - O está com a vez

    private int numeroDeJogadas = 0;

    private boolean jogoPausado = false;

    // Outros Atributos

    private Service contadorTempoService;

    private Rectangle2D tamanhoTela = Screen.getPrimary().getBounds();

    private MediaPlayer musicaFundoMediaPlayer;

    private AudioClip buttonClickAudioClip;

    private AudioClip fimDeJogoAudioClip;

    private ResourceBundle resources;

    private Timeline animacaoTelaInicialTimeline;

    private Timeline animacaoSliderVolumeTimeline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.resources = resources;

        // Background

        Stop[] stops = new Stop[] {
                new Stop(0, Color.rgb(240,154,7)),
                new Stop(1, Color.rgb(240,178,7)),
        };
        LinearGradient degrade = new LinearGradient(1, 0, 0, 0, true, CycleMethod.NO_CYCLE, stops);

        root.setBackground(new Background(new BackgroundFill(degrade,null,null)));

        // Botão Jogar

        jogarButton.getStyleClass().add(CLASSE_BUTTON_TOP);
        jogarButton.setId(ID_BUTTON_INICIAR);
        jogarButton.setFont(new Font(NOME_FONTE_BUTTONS,tamanhoTela.getWidth() * 0.0166666));
        jogarButton.setPrefWidth(tamanhoTela.getWidth() * 0.1562);
        jogarButton.setPrefHeight(tamanhoTela.getWidth() * 0.0312);

        // Image View Ícone da Velha

        velhaImageView.setImage(new Image(URL_IMAGEM_VELHA));
        velhaImageView.setFitWidth(tamanhoTela.getWidth() * 0.125);
        velhaImageView.setFitHeight(tamanhoTela.getWidth() * 0.125);
        AnchorPane.setTopAnchor(hboxVelhaImageView,tamanhoTela.getHeight() * 0.12);
        AnchorPane.setLeftAnchor(hboxVelhaImageView, 0.0);
        AnchorPane.setRightAnchor(hboxVelhaImageView, 0.0);

        DropShadow dropShadowImageView = new DropShadow(tamanhoTela.getWidth() * 0.00104166, Color.rgb(0,0,0,0.3));
        velhaImageView.setEffect(dropShadowImageView);

        // Combo Box Idioma

        ObservableList<LocatedImage> imagemURL = FXCollections.observableArrayList();
        imagemURL.add(new LocatedImage(URL_IMAGEM_IDIOMA_BRASIL, IDIOMA_PT_LOCALE));
        imagemURL.add(new LocatedImage(URL_IMAGEM_IDIOMA_US, IDIOMA_EN_LOCALE));

        idiomaIconeComboBox.setItems(imagemURL);
        idiomaIconeComboBox.setButtonCell(new ImageListCell());
        idiomaIconeComboBox.setCellFactory(listView -> new ImageListCell());
        idiomaIconeComboBox.getSelectionModel().selectFirst();
        idiomaIconeComboBox.setPrefWidth(tamanhoTela.getWidth() * 0.02864);

        idiomaIconeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String stringLocale = IDIOMA_PT_LOCALE; // Idioma Padrão caso aconteça erro nos condicionais abaixo
                if (newValue.getUrl().equals(URL_IMAGEM_IDIOMA_US)) {
                    stringLocale = IDIOMA_EN_LOCALE;
                } else if (newValue.getUrl().equals(URL_IMAGEM_IDIOMA_BRASIL)){
                    stringLocale = IDIOMA_PT_LOCALE;
                }
                this.resources = ResourceBundle.getBundle(URL_BUNDLE_IDIOMA,new Locale(stringLocale));
                colocarTextNosControles();
                try {
                    ((Stage) root.getScene().getWindow()).setTitle(this.resources.getString(BUNDLE_STRING_KEY_STAGE_TITLE));
                } catch (NullPointerException e) {
                    System.err.println("A Janela ainda não foi totalmente carregada");
                }

            }
        });

        AnchorPane.setBottomAnchor(idiomaIconeComboBox, tamanhoTela.getHeight() * 0.0185185);
        AnchorPane.setRightAnchor(idiomaIconeComboBox, tamanhoTela.getWidth() * 0.036458);

        // Botão Ícone áudio

        ImageView audioIconeImageView = new ImageView(new Image(URL_IMAGEM_ICONE_AUDIO_MUTADO));
        audioIconeImageView.setFitHeight(tamanhoTela.getWidth() * 0.015625);
        audioIconeImageView.setFitWidth(tamanhoTela.getWidth() * 0.015625);
        audioIconeButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        audioIconeButton.setGraphic(audioIconeImageView);
        audioIconeButton.setId(ID_BUTTON_AUDIO);
        AnchorPane.setBottomAnchor(audioIconeButton,tamanhoTela.getHeight() * 0.018518);
        AnchorPane.setRightAnchor(audioIconeButton, tamanhoTela.getWidth() * 0.015625);

        // Será tocado ao término da animação animacaoTelaInicialTimeline

        musicaFundoMediaPlayer = new MediaPlayer(new Media(getClass().getResource(URL_MUSICA_FUNDO).toExternalForm()));
        musicaFundoMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicaFundoMediaPlayer.volumeProperty().bind(sliderVolume.valueProperty());

        // Slider

        sliderVolume.setPrefHeight(tamanhoTela.getHeight() * 0.092592);
        sliderVolume.setLayoutY(tamanhoTela.getHeight() * 0.3);
        sliderVolume.setLayoutX(-tamanhoTela.getWidth() * 0.05208);

        sliderVolume.setPadding(new Insets(tamanhoTela.getHeight() * 0.0074074,tamanhoTela.getWidth() * 0.00625,
                tamanhoTela.getHeight() * 0.0074074, tamanhoTela.getWidth() * 0.00625));

        sliderVolume.valueProperty().addListener(((observable, oldValue, newValue) -> {

            // De acordo com o valor do sliderVolume, a imagem do ícone áudio será atualizada

            LocatedImage imagem;
            double volume = (Double) newValue;
            ImageView imageView = (ImageView) audioIconeButton.getGraphic();
            if (volume == 0.0){
                imagem = new LocatedImage(URL_IMAGEM_ICONE_AUDIO_MUTADO);
            } else if (volume > 0.0 && volume < 0.5) {
                imagem = new LocatedImage(URL_IMAGEM_ICONE_AUDIO_MINIMO);
            } else if (volume >= 0.5 && volume < 1.0) {
                imagem = new LocatedImage(URL_IMAGEM_ICONE_AUDIO_MEDIO);
            } else {
                imagem = new LocatedImage(URL_IMAGEM_ICONE_AUDIO_MAXIMO);
            }

            imageView.setImage(imagem);

        }));


        /* Animações */

        /* Animação - Botão Iniciar e ImageView da Velha */

        KeyValue animacaoTelaInicialValor1 = new KeyValue(jogarButton.layoutXProperty(),0.0);
        KeyFrame animacaoTelaInicialFrame1 = new KeyFrame(Duration.millis(0), animacaoTelaInicialValor1);
        KeyValue animacaoTelaInicialValor2 = new KeyValue(jogarButton.layoutXProperty(),
                tamanhoTela.getWidth() * 0.114583);
        KeyFrame animacaoTelaInicialFrame2 = new KeyFrame(Duration.millis(500),animacaoTelaInicialValor2);
        KeyValue animacaoTelaInicialValor3 = new KeyValue(jogarButton.layoutXProperty(),
                tamanhoTela.getWidth() * 0.0859375);
        KeyFrame animacaoTelaInicialFrame3 = new KeyFrame(Duration.millis(700),animacaoTelaInicialValor3);
        KeyValue animacaoTelaInicialValor4 = new KeyValue(jogarButton.layoutXProperty(),
                tamanhoTela.getWidth() * 0.099479);
        KeyFrame animacaoTelaInicialFrame4 = new KeyFrame(Duration.millis(900),animacaoTelaInicialValor4);
        KeyValue animacaoTelaInicialValor5 = new KeyValue(jogarButton.opacityProperty(),0.0);
        KeyFrame animacaoTelaInicialFrame5 = new KeyFrame(Duration.millis(0),animacaoTelaInicialValor5);
        KeyValue animacaoTelaInicialValor6 = new KeyValue(jogarButton.opacityProperty(),1.0);
        KeyFrame animacaoTelaInicialFrame6 = new KeyFrame(Duration.millis(900),animacaoTelaInicialValor6);
        KeyValue animacaoTelaInicialValor7 = new KeyValue(jogarButton.layoutYProperty(),0.0);
        KeyFrame animacaoTelaInicialFrame7 = new KeyFrame(Duration.millis(0), animacaoTelaInicialValor7);
        KeyValue animacaoTelaInicialValor8 = new KeyValue(jogarButton.layoutYProperty(),
                tamanhoTela.getHeight() * 0.043);
        KeyFrame animacaoTelaInicialFrame8 = new KeyFrame(Duration.millis(500), animacaoTelaInicialValor8);
        KeyValue animacaoTelaInicialValor9 = new KeyValue(jogarButton.layoutYProperty(),
                tamanhoTela.getHeight() * 0.038);
        KeyFrame animacaoTelaInicialFrame9 = new KeyFrame(Duration.millis(700), animacaoTelaInicialValor9);
        KeyValue animacaoTelaInicialValor10 = new KeyValue(jogarButton.layoutYProperty(),
                tamanhoTela.getHeight() * 0.04);
        KeyFrame animacaoTelaInicialFrame10 = new KeyFrame(Duration.millis(900), animacaoTelaInicialValor10);
        KeyValue animacaoTelaInicialValor11 = new KeyValue(jogarButton.disableProperty(), true);
        KeyFrame animacaoTelaInicialFrame11 = new KeyFrame(Duration.millis(0), animacaoTelaInicialValor11);
        KeyValue animacaoTelaInicialValor12 = new KeyValue(jogarButton.disableProperty(), false);
        KeyFrame animacaoTelaInicialFrame12 = new KeyFrame(Duration.millis(900), animacaoTelaInicialValor12);
        KeyValue animacaoTelaInicialValor13 = new KeyValue(velhaImageView.opacityProperty(), 0.0);
        KeyFrame animacaoTelaInicialFrame13 = new KeyFrame(Duration.millis(700), animacaoTelaInicialValor13);
        KeyValue animacaoTelaInicialValor14 = new KeyValue(velhaImageView.opacityProperty(), 1.0);
        KeyFrame animacaoTelaInicialFrame14 = new KeyFrame(Duration.millis(1500), animacaoTelaInicialValor14);

        animacaoTelaInicialTimeline = new Timeline(animacaoTelaInicialFrame1, animacaoTelaInicialFrame2,
                animacaoTelaInicialFrame3, animacaoTelaInicialFrame4, animacaoTelaInicialFrame5,
                animacaoTelaInicialFrame6, animacaoTelaInicialFrame7, animacaoTelaInicialFrame8, animacaoTelaInicialFrame9,
                animacaoTelaInicialFrame10, animacaoTelaInicialFrame11, animacaoTelaInicialFrame12,
                animacaoTelaInicialFrame13, animacaoTelaInicialFrame14);

        animacaoTelaInicialTimeline.setDelay(Duration.seconds(0.8));

        animacaoTelaInicialTimeline.setOnFinished(event -> musicaFundoMediaPlayer.play());

        animacaoTelaInicialTimeline.play();

        /* Animação - ComboBox Idioma e AudioButton*/

        KeyValue animacaoBarraConfigValor1 = new KeyValue(idiomaIconeComboBox.opacityProperty(),0.0);
        KeyFrame animacaoBarraConfigFrame1 = new KeyFrame(Duration.millis(0), animacaoBarraConfigValor1);
        KeyValue animacaoBarraConfigValor2 = new KeyValue(idiomaIconeComboBox.opacityProperty(),1.0);
        KeyFrame animacaoBarraConfigFrame2 = new KeyFrame(Duration.millis(1000), animacaoBarraConfigValor2);
        KeyValue animacaoBarraConfigValor3 = new KeyValue(audioIconeButton.opacityProperty(), 0.0);
        KeyFrame animacaoBarraConfigFrame3 = new KeyFrame(Duration.millis(0), animacaoBarraConfigValor3);
        KeyValue animacaoBarraConfigValor4 = new KeyValue(audioIconeButton.opacityProperty(), 1.0);
        KeyFrame animacaoBarraConfigFrame4 = new KeyFrame(Duration.millis(1000), animacaoBarraConfigValor4);

        Timeline animacaoBarraConfigTimeline = new Timeline(animacaoBarraConfigFrame1, animacaoBarraConfigFrame2,
                animacaoBarraConfigFrame3, animacaoBarraConfigFrame4);
        animacaoBarraConfigTimeline.setDelay(Duration.seconds(2));
        animacaoBarraConfigTimeline.play();

        animacaoBarraConfigTimeline.setOnFinished(event -> {
            idiomaIconeComboBox.setDisable(false);
            audioIconeButton.setDisable(false);
        });

        /* Animação Slider - Posição do Slider na janela */

        KeyValue animacaoSliderValor1 = new KeyValue(sliderVolume.layoutXProperty(),tamanhoTela.getWidth() *
                tamanhoTela.getWidth() * 0.3201);
        KeyFrame animacaoSliderFrame1 = new KeyFrame(Duration.millis(0), animacaoSliderValor1);
        KeyValue animacaoSliderValor2 = new KeyValue(sliderVolume.layoutXProperty(),-tamanhoTela.getWidth() * 0.05208);
        KeyFrame animacaoSliderFrame2 = new KeyFrame(Duration.millis(1), animacaoSliderValor2);
        animacaoSliderVolumeTimeline = new Timeline(animacaoSliderFrame1, animacaoSliderFrame2);
        animacaoSliderVolumeTimeline.setDelay(Duration.seconds(0.3));

        // Este service terá a função de exibir um contador em segundos na tela de acordo com o atributo contadorTempo

        contadorTempoService = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        while (contadorTempo >= 0) {
                            updateMessage(String.format("%d",contadorTempo));
                            Thread.sleep(1000);
                            contadorTempo--;
                        }

                        return null;
                    }
                };
            }
        };

        // Se a contagem chegar ao final (0 seg), será exibido um alert e o jogo será reiniciado

        contadorTempoService.setOnSucceeded(event -> {
            fimDeJogoAudioClip.play();
            exibirAlerta(this.resources.getString(BUNDLE_STRING_KEY_ALERT_TITLE),
                    this.resources.getString(BUNDLE_STRING_KEY_ALERT_TEMPOESGOTADO_HEADER_TEXT),
                    this.resources.getString(BUNDLE_STRING_KEY_ALERT_CONTEXT_TEXT));
            if (reiniciarButton != null) {
                reiniciarButton.fire();
            }

        });

        // Este AudioClip será executado sempre que o usuário acionar algum Button

        buttonClickAudioClip = new AudioClip(getClass().getResource(URL_SOUND_EFFECT_BUTTON_CLICK).toExternalForm());
        buttonClickAudioClip.volumeProperty().bind(sliderVolume.valueProperty());

        // Este AudioClip será executado quando o jogo chegar ao fim

        fimDeJogoAudioClip = new AudioClip(getClass().getResource(URL_SOUND_EFFECT_FIM_DE_JOGO).
                toExternalForm());
        fimDeJogoAudioClip.volumeProperty().bind(sliderVolume.valueProperty());

        colocarTextNosControles();

    }

    /* Este método colocará os textos dos controles de acordo com o idioma selecionado */
    private void colocarTextNosControles(){

        jogarButton.setText(resources.getString(BUNDLE_STRING_KEY_BUTTON_INICIAR).trim());
        if (reiniciarButton != null) {
            reiniciarButton.setText(resources.getString(BUNDLE_STRING_KEY_BUTTON_REINICIAR).trim());
        }
        if (pausarOuRetomarButton != null) {
            if (jogoPausado) {
               pausarOuRetomarButton.setText(resources.getString(BUNDLE_STRING_KEY_BUTTON_RETOMAR));
            } else {
                pausarOuRetomarButton.setText(resources.getString(BUNDLE_STRING_KEY_BUTTON_PAUSAR).trim());
            }

        }
        if (cancelarButton != null) {
            cancelarButton.setText(resources.getString(BUNDLE_STRING_KEY_BUTTON_CANCELAR).trim());
        }

        if (tempoRestanteLabel != null) {
            tempoRestanteLabel.setText(resources.getString(BUNDLE_STRING_KEY_LABEL_TEMPO_RESTANTE).trim());
        }

    }

    /* Este método será executado quando o jogarButton for acionado. Nesse caso, serão executadas animações a fim de
    *  configurar a aplicação para o início do jogo. */

    @FXML
    private void handleIniciarOnAction() {

        buttonClickAudioClip.play();

        double larguraButton = tamanhoTela.getWidth() * 0.07;
        double alturaButton = tamanhoTela.getHeight() * 0.046296;

        // Configuração dos Button reiniciarButton, pausarOuRetomarButton e cancelarButton

        reiniciarButton = new Button(resources.getString(BUNDLE_STRING_KEY_BUTTON_REINICIAR));
        reiniciarButton.getStyleClass().add(CLASSE_BUTTON_JOGO);
        reiniciarButton.setId(ID_BUTTON_REINICIAR);
        reiniciarButton.setPrefSize(larguraButton, alturaButton);
        reiniciarButton.setFont(new Font(NOME_FONTE_BUTTONS, tamanhoTela.getWidth() * 0.012));
        reiniciarButton.setLayoutY(tamanhoTela.getHeight() * 0.04);
        reiniciarButton.setLayoutX(-tamanhoTela.getWidth() * 0.10416);
        reiniciarButton.setOnAction(this::handleReiniciarButtonOnAction);

        pausarOuRetomarButton = new Button(resources.getString(BUNDLE_STRING_KEY_BUTTON_PAUSAR));
        pausarOuRetomarButton.getStyleClass().add(CLASSE_BUTTON_JOGO);
        pausarOuRetomarButton.setId(ID_BUTTON_PAUSAR_RETOMAR);
        pausarOuRetomarButton.setPrefSize(larguraButton, alturaButton);
        pausarOuRetomarButton.setFont(new Font(NOME_FONTE_BUTTONS, tamanhoTela.getWidth() * 0.012));
        pausarOuRetomarButton.setLayoutX(tamanhoTela.getWidth() * 0.145833);
        pausarOuRetomarButton.setLayoutY(-tamanhoTela.getHeight() * 0.09259);
        pausarOuRetomarButton.setOnAction(this::handlePausarButtonOnAction);

        cancelarButton = new Button(resources.getString(BUNDLE_STRING_KEY_BUTTON_CANCELAR));
        cancelarButton.getStyleClass().add(CLASSE_BUTTON_JOGO);
        cancelarButton.setId(ID_BUTTON_CANCELAR);
        cancelarButton.setPrefSize(larguraButton, alturaButton);
        cancelarButton.setFont(new Font(NOME_FONTE_BUTTONS, tamanhoTela.getWidth() * 0.012));
        cancelarButton.setLayoutY(tamanhoTela.getHeight() * 0.04);
        cancelarButton.setLayoutX(tamanhoTela.getWidth() * 0.41666);

        root.getChildren().addAll(reiniciarButton, pausarOuRetomarButton, cancelarButton);

        // Indicador de Vez - Imagem ao lado direito do cancelarButton

        indicadorVezImageView = new ImageView(new Image(URL_IMAGEM_ICONE_X));
        indicadorVezImageView.setFitHeight(tamanhoTela.getWidth() * 0.018229);
        indicadorVezImageView.setFitWidth(tamanhoTela.getWidth() * 0.018229);
        indicadorVezImageView.setEffect(new DropShadow(tamanhoTela.getWidth() * 0.00260416,Color.rgb(0,0,0,0.4)));
        indicadorVezImageView.setLayoutX(tamanhoTela.getWidth() * 0.30989);
        indicadorVezImageView.setLayoutY(-tamanhoTela.getHeight() * 0.092592);

        root.getChildren().add(indicadorVezImageView);

        double tamanhoMatrizButton = tamanhoTela.getWidth() * 0.041;

        // Configuração da Matriz de Button (campos disponíveis para as jogadas) - O Jogo propriamente dito

        matrizGridPane = new GridPane();
        matrizGridPane.setVgap(tamanhoTela.getWidth() * 0.0026041);
        matrizGridPane.setHgap(tamanhoTela.getWidth() * 0.0026041);


        int contadorNomeArquivo = 7;

        for (int i = 0; i < TAMANHO_MATRIZ; i++) {

            int contadorAuxiliar = contadorNomeArquivo;

            for (int j = 0; j < TAMANHO_MATRIZ; j++) {

                MatrizButton matrizButton = new MatrizButton(contadorAuxiliar);
                matrizButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                matrizButton.setOnMouseEntered(this::handleMatrizButtonMouseEntered);
                matrizButton.setOnMouseExited(event -> matrizButton.setIconeNumero());
                matrizButton.setOnAction(this::handleValidMatrizButtonOnAction);

                matrizButton.setAlignment(Pos.CENTER);
                matrizButton.getStyleClass().add(CLASSE_BUTTON_CAMPO);
                matrizButton.setPrefSize(tamanhoMatrizButton, tamanhoMatrizButton);
                matrizGridPane.add(matrizButton, j, i);

                contadorAuxiliar++;
            }

            contadorNomeArquivo = contadorNomeArquivo - 3;
        }


        matrizGridPane.setLayoutX(- tamanhoTela.getWidth() * 0.15625);
        matrizGridPane.setLayoutY(tamanhoTela.getHeight() * 0.12037);

        root.getChildren().add(matrizGridPane);

        Color rectangleColor = Color.rgb(100,100,100);

        double rectangleSize = tamanhoTela.getWidth() *  0.00208333;
        double rectangleSize2 = tamanhoTela.getHeight() * 0.2296296;

        // Configuração dos Rectangle que são as linhas da matriz

        Rectangle rectangle1 = new Rectangle(rectangleSize, rectangleSize2, rectangleColor);
        rectangle1.setLayoutX(- tamanhoTela.getWidth() * 0.15625);
        rectangle1.setLayoutY(tamanhoTela.getHeight() * 0.120370);

        Rectangle rectangle2 = new Rectangle(rectangleSize, rectangleSize2, rectangleColor);
        rectangle2.setLayoutX(-tamanhoTela.getWidth() * 0.052083);
        rectangle2.setLayoutY(tamanhoTela.getHeight() * 0.120370);

        Rectangle rectangle3 = new Rectangle(rectangleSize2, rectangleSize, rectangleColor);
        rectangle3.setLayoutX(tamanhoTela.getWidth() * 0.117);
        rectangle3.setLayoutY(-tamanhoTela.getHeight() * 0.092592);

        Rectangle rectangle4 = new Rectangle(rectangleSize2, rectangleSize, rectangleColor);
        rectangle4.setLayoutX(tamanhoTela.getWidth() * 0.117);
        rectangle4.setLayoutY(-tamanhoTela.getHeight() * 0.092592);

        root.getChildren().addAll(rectangle1, rectangle2, rectangle3, rectangle4);

        HBox tempoRestanteHbox = new HBox(tamanhoTela.getWidth() * 0.0052083);

        tempoRestanteLabel = new Label(resources.getString(BUNDLE_STRING_KEY_LABEL_TEMPO_RESTANTE));
        tempoRestanteLabel.setFont(new Font(tamanhoTela.getWidth() * 0.0119791));

        // Este Label será usado para fazer a contagem regressiva até o fim de jogo.
        // Seu valor será bindado com o contadorTempoService

        tempoRestanteNumeroLabel = new Label();
        tempoRestanteNumeroLabel.textProperty().bind(contadorTempoService.messageProperty());
        tempoRestanteNumeroLabel.setFont(new Font(tamanhoTela.getWidth() * 0.0119791));
        tempoRestanteNumeroLabel.setTextFill(Color.GREEN);

        tempoRestanteHbox.getChildren().addAll(tempoRestanteLabel,tempoRestanteNumeroLabel);
        tempoRestanteHbox.setLayoutX(tamanhoTela.getWidth() * 0.0104166);
        tempoRestanteHbox.setLayoutY(tamanhoTela.getHeight() * 0.5555555);

        root.getChildren().add(tempoRestanteHbox);

        /* Esta animação será executada quando o usuário acionar o botão Jogar. Nesse caso, será configurada
           a aplicação para o início do Jogo */

        KeyValue animacaoJogarValor1 = new KeyValue(jogarButton.layoutYProperty(),
                tamanhoTela.getHeight() * 0.04);
        KeyFrame animacaoJogarFrame1 = new KeyFrame(Duration.millis(0), animacaoJogarValor1);
        KeyValue animacaoJogarValor2 = new KeyValue(jogarButton.layoutYProperty(),
                -tamanhoTela.getHeight() * 0.0925925);
        KeyFrame animacaoJogarFrame2 = new KeyFrame(Duration.millis(300), animacaoJogarValor2);
        KeyValue animacaoJogarValor3 = new KeyValue(jogarButton.opacityProperty(),1.0);
        KeyFrame animacaoJogarFrame3 = new KeyFrame(Duration.millis(100), animacaoJogarValor3);
        KeyValue animacaoJogarValor4 = new KeyValue(jogarButton.opacityProperty(), 0.0);
        KeyFrame animacaoJogarFrame4 = new KeyFrame(Duration.millis(300), animacaoJogarValor4);
        KeyValue animacaoJogarValor5 = new KeyValue(velhaImageView.opacityProperty(), 1.0);
        KeyFrame animacaoJogarFrame5 = new KeyFrame(Duration.millis(0), animacaoJogarValor5);
        KeyValue animacaoJogarValor6 = new KeyValue(velhaImageView.opacityProperty(), 0.0);
        KeyFrame animacaoJogarFrame6 = new KeyFrame(Duration.millis(300), animacaoJogarValor6);
        KeyValue animacaoJogarValor7 = new KeyValue(reiniciarButton.layoutXProperty(),
                -tamanhoTela.getWidth() * 0.1041666);
        KeyFrame animacaoJogarFrame7 = new KeyFrame(Duration.millis(300), animacaoJogarValor7);
        KeyValue animacaoJogarValor8 = new KeyValue(reiniciarButton.layoutXProperty(),
                tamanhoTela.getWidth() * 0.072916);
        KeyFrame animacaoJogarFrame8 = new KeyFrame(Duration.millis(600), animacaoJogarValor8);
        KeyValue animacaoJogarValor9 = new KeyValue(pausarOuRetomarButton.layoutYProperty(),
                -tamanhoTela.getHeight() * 0.092592);
        KeyFrame animacaoJogarFrame9 = new KeyFrame(Duration.millis(300), animacaoJogarValor9);
        KeyValue animacaoJogarValor10 = new KeyValue(pausarOuRetomarButton.layoutYProperty(),
                tamanhoTela.getHeight() * 0.04);
        KeyFrame animacaoJogarFrame10 = new KeyFrame(Duration.millis(600), animacaoJogarValor10);
        KeyValue animacaoJogarValor11 = new KeyValue(cancelarButton.layoutXProperty(),
                tamanhoTela.getWidth() * 0.416666);
        KeyFrame animacaoJogarFrame11 = new KeyFrame(Duration.millis(300), animacaoJogarValor11);
        KeyValue animacaoJogarValor12 = new KeyValue(cancelarButton.layoutXProperty(),
                tamanhoTela.getWidth() * 0.21875);
        KeyFrame animacaoJogarFrame12 = new KeyFrame(Duration.millis(600), animacaoJogarValor12);
        KeyValue animacaoJogarValor13 = new KeyValue(indicadorVezImageView.layoutYProperty(),
                -tamanhoTela.getHeight() * 0.092592);
        KeyFrame animacaoJogarFrame13 = new KeyFrame(Duration.millis(300), animacaoJogarValor13);
        KeyValue animacaoJogarValor14 = new KeyValue(indicadorVezImageView.layoutYProperty(),
                tamanhoTela.getHeight() * 0.0481481);
        KeyFrame animacaoJogarFrame14 = new KeyFrame(Duration.millis(600), animacaoJogarValor14);
        KeyValue animacaoJogarValor15 = new KeyValue(matrizGridPane.layoutXProperty(),
                -tamanhoTela.getWidth() * 0.15625);
        KeyFrame animacaoJogarFrame15 = new KeyFrame(Duration.millis(300), animacaoJogarValor15);
        KeyValue animacaoJogarValor16 = new KeyValue(matrizGridPane.layoutXProperty(), tamanhoTela.getWidth() * 0.117);
        KeyFrame animacaoJogarFrame16 = new KeyFrame(Duration.millis(600), animacaoJogarValor16);
        KeyValue animacaoJogarValor17 = new KeyValue(rectangle1.layoutXProperty(),-tamanhoTela.getWidth() * 0.052083);
        KeyFrame animacaoJogarFrame17 = new KeyFrame(Duration.millis(300),animacaoJogarValor17);
        KeyValue animacaoJogarValor18 = new KeyValue(rectangle1.layoutXProperty(), tamanhoTela.getWidth() * 0.159);
        KeyFrame animacaoJogarFrame18 = new KeyFrame(Duration.millis(600),animacaoJogarValor18);
        KeyValue animacaoJogarValor19 = new KeyValue(rectangle2.layoutXProperty(),-tamanhoTela.getWidth() * 0.052083);
        KeyFrame animacaoJogarFrame19 = new KeyFrame(Duration.millis(300),animacaoJogarValor19);
        KeyValue animacaoJogarValor20 = new KeyValue(rectangle2.layoutXProperty(), tamanhoTela.getWidth() * 0.202);
        KeyFrame animacaoJogarFrame20 = new KeyFrame(Duration.millis(600),animacaoJogarValor20);
        KeyValue animacaoJogarValor21 = new KeyValue(rectangle3.layoutYProperty(), -tamanhoTela.getHeight() * 0.092592);
        KeyFrame animacaoJogarFrame21 = new KeyFrame(Duration.millis(300), animacaoJogarValor21);
        KeyValue animacaoJogarValor22 = new KeyValue(rectangle3.layoutYProperty(), tamanhoTela.getHeight() * 0.1944444);
        KeyFrame animacaoJogarFrame22 = new KeyFrame(Duration.millis(600), animacaoJogarValor22);
        KeyValue animacaoJogarValor23 = new KeyValue(rectangle4.layoutYProperty(), -tamanhoTela.getWidth() * 0.052083);
        KeyFrame animacaoJogarFrame23 = new KeyFrame(Duration.millis(300), animacaoJogarValor23);
        KeyValue animacaoJogarValor24 = new KeyValue(rectangle4.layoutYProperty(), tamanhoTela.getHeight() * 0.273148);
        KeyFrame animacaoJogarFrame24 = new KeyFrame(Duration.millis(600), animacaoJogarValor24);
        KeyValue animacaoJogarValor25 = new KeyValue(tempoRestanteHbox.layoutYProperty(),
                tamanhoTela.getHeight() * 0.555555);
        KeyFrame animacaoJogarFrame25 = new KeyFrame(Duration.millis(300), animacaoJogarValor25);
        KeyValue animacaoJogarValor26 = new KeyValue(tempoRestanteHbox.layoutYProperty(),
                tamanhoTela.getHeight() * 0.407407);
        KeyFrame animacaoJogarFrame26 = new KeyFrame(Duration.millis(600), animacaoJogarValor26);

        Animation animacaoJogarTimeline = new Timeline(animacaoJogarFrame1, animacaoJogarFrame2, animacaoJogarFrame3,
                animacaoJogarFrame4, animacaoJogarFrame5, animacaoJogarFrame6, animacaoJogarFrame7, animacaoJogarFrame8,
                animacaoJogarFrame9, animacaoJogarFrame10, animacaoJogarFrame11, animacaoJogarFrame12, animacaoJogarFrame13,
                animacaoJogarFrame14, animacaoJogarFrame15, animacaoJogarFrame16, animacaoJogarFrame17, animacaoJogarFrame18,
                animacaoJogarFrame19, animacaoJogarFrame20, animacaoJogarFrame21, animacaoJogarFrame22, animacaoJogarFrame23,
                animacaoJogarFrame24, animacaoJogarFrame25, animacaoJogarFrame26);
        animacaoJogarTimeline.play();

        animacaoJogarTimeline.setAutoReverse(false);

        // Quando o botão Cancelar for acionado, a aplicação voltará ao seu estado inicial. Para isso, animações serão
        // executadas e os atributos de controle serão atualizados.

        cancelarButton.setOnAction(event -> {

            buttonClickAudioClip.play();

            if (jogoPausado) {
                handleRetomarOnAction(null);
            }

            animacaoJogarTimeline.setRate(-1);
            animacaoJogarTimeline.jumpTo(Duration.millis(600));
            animacaoJogarTimeline.play();
            animacaoJogarTimeline.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.lessThanOrEqualTo(Duration.millis(300))){
                    animacaoJogarTimeline.stop();
                    animacaoTelaInicialTimeline.setDelay(Duration.seconds(0));
                    animacaoTelaInicialTimeline.play();
                }
            });

            this.vez = false;
            contadorTempo = TEMPO_JOGO;
            this.numeroDeJogadas = 0;
            contadorTempoService.cancel();

        });

        this.vez = false;
        contadorTempo = TEMPO_JOGO;
        this.numeroDeJogadas = 0;

        contadorTempoService.restart();

    }

    /* Este método será executado quando o botão reinicarButton for acionado. O jogo será reiniciado,
     * limpando os campos e atualizando os atributos de controle. */

    private void handleReiniciarButtonOnAction(ActionEvent event) {

        buttonClickAudioClip.play();

        if (jogoPausado) {
            handleRetomarOnAction(null);
        }

        matrizGridPane.getChildren().forEach(node -> {
            MatrizButton button = (MatrizButton) node;
            button.setText("");
            button.setOnMouseEntered(this::handleMatrizButtonMouseEntered);
            button.setOnMouseExited(e -> button.setIconeNumero());
            button.setOnAction(this::handleValidMatrizButtonOnAction);
            button.setIconeNumero();
        });

        indicadorVezImageView.setImage(new Image(URL_IMAGEM_ICONE_X));

        vez = false;
        numeroDeJogadas = 0;
        contadorTempo = TEMPO_JOGO;

        contadorTempoService.restart();

    }

    /* Esté metodo será executado quando o botão pausarOuRetomarButton for acionado. Será pausado o jogo, parando o contador,
     * desativando os buttons da matriz e será colocada na tela o playIconButton que contém a imagem de um ícone de play. */

    private void handlePausarButtonOnAction(ActionEvent event) {

        buttonClickAudioClip.play();

        matrizGridPane.getChildren().forEach(node -> node.setDisable(true));

        contadorTempoService.cancel();

        if (playIconButton == null) {
            playIconButton = new Button();
            ImageView imageView = new ImageView(new Image(URL_IMAGEM_ICONE_PLAY));
            imageView.setFitWidth(tamanhoTela.getWidth() * 0.09375);
            imageView.setFitHeight(tamanhoTela.getWidth() * 0.09375);
            imageView.setEffect(new DropShadow(10, Color.rgb(0,0,0,0.2)));
            playIconButton.setGraphic(imageView);
            playIconButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            playIconButton.setLayoutY(tamanhoTela.getHeight() * 0.15);
            playIconButton.setLayoutX(tamanhoTela.getWidth() * 0.13);
            playIconButton.setId(ID_BUTTON_PLAY_ICON);
            playIconButton.setOnAction(this::handleRetomarOnAction);
        }

        pausarOuRetomarButton.setOnAction(this::handleRetomarOnAction);
        pausarOuRetomarButton.setText(resources.getString(BUNDLE_STRING_KEY_BUTTON_RETOMAR));

        tempoRestanteNumeroLabel.setTextFill(Color.RED);

        root.getChildren().add(playIconButton);

        jogoPausado = true;
    }

    /* Este método será executado quando for acionado o botão playIconButton ou o pausarOuRetomarButton e o jogo estiver
     * pausado. Nesse caso, os buttons da matriz serão ativados, a contagem será retomada e o playIconButton será removido
     * da tela. */
    private void handleRetomarOnAction(ActionEvent event) {

        if (event != null) {
            buttonClickAudioClip.play();
        }

        matrizGridPane.getChildren().forEach(node -> node.setDisable(false));

        pausarOuRetomarButton.setOnAction(this::handlePausarButtonOnAction);
        pausarOuRetomarButton.setText(resources.getString(BUNDLE_STRING_KEY_BUTTON_PAUSAR));

        root.getChildren().remove(playIconButton);

        tempoRestanteNumeroLabel.setTextFill(Color.GREEN);

        jogoPausado = false;
        contadorTempoService.restart();
    }

    /* Este método será executado quando um button que esteja na matriz for acionado e ainda não esteja sido preenchido.
     * Será colocada a imagem de acordo com a vez, atualizados os métodos desse button, atualizados os atributos de controle
     * e executada uma animação e verificado se o jogo chegou ao fim. */

    private void handleValidMatrizButtonOnAction(ActionEvent event) {
        buttonClickAudioClip.play();
        MatrizButton button = (MatrizButton) event.getSource();
        ImageView imageView = (ImageView) button.getGraphic();
        imageView.setOpacity(1.0);
        if (vez) {
            button.setText("o");
            imageView.setImage(new Image(URL_IMAGEM_ICONE_O));
            indicadorVezImageView.setImage(new Image(URL_IMAGEM_ICONE_X));
        } else {
            button.setText("x");
            imageView.setImage(new Image(URL_IMAGEM_ICONE_X));
            indicadorVezImageView.setImage(new Image(URL_IMAGEM_ICONE_O));
        }

        button.setOnMouseEntered(null);
        button.setOnMouseExited(null);
        button.setOnAction(this::handleInvalidMatrizButtonOnAction);

        vez = !vez;
        numeroDeJogadas++;

        ScaleTransition transition = new ScaleTransition(Duration.millis(150), indicadorVezImageView);
        transition.setFromX(1);
        transition.setToX(1.3);
        transition.setFromY(1);
        transition.setToY(1.3);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.playFromStart();

        if (numeroDeJogadas > 4) {
            String vencedor = verificarResultado();
            if (vencedor != null) {
                transition.stop();
                fimDeJogoAudioClip.play();
                exibirAlerta(resources.getString(BUNDLE_STRING_KEY_ALERT_TITLE),
                        vencedor.toUpperCase() + " " + resources.getString(BUNDLE_STRING_KEY_ALERT_VENCEDOR_HEADER_TEXT)
                        , resources.getString(BUNDLE_STRING_KEY_ALERT_CONTEXT_TEXT));
                this.handleReiniciarButtonOnAction(null);
            } else {
                if (numeroDeJogadas == 9) {
                    fimDeJogoAudioClip.play();
                    exibirAlerta(resources.getString(BUNDLE_STRING_KEY_ALERT_TITLE)
                            ,resources.getString(BUNDLE_STRING_KEY_ALERT_EMPATE_HEADER_TEXT),
                            resources.getString(BUNDLE_STRING_KEY_ALERT_CONTEXT_TEXT));
                    this.handleReiniciarButtonOnAction(null);
                }
            }
        }

    }

    // Este método será executado quando um button pertencente à matriz for acionado. Nesse caso, será tocado um som de erro

    private void handleInvalidMatrizButtonOnAction(ActionEvent event) {
        AudioClip audioClipError = new AudioClip(getClass().getResource(URL_ERROR_SOUND_EFFECT).toExternalForm());
        audioClipError.play(sliderVolume.getValue());
    }

    /* Este método será executado quando o mouse passar por cima de button pertencente à matriz. Nesse caso, a imagem
     * contida nele será alterada de acordo com a vez. */

    private void handleMatrizButtonMouseEntered(MouseEvent event) {
        ImageView imageView = (ImageView) ((MatrizButton)event.getSource()).getGraphic();

        if (vez) {
            imageView.setImage(new Image(URL_IMAGEM_ICONE_O));
        } else {
            imageView.setImage(new Image(URL_IMAGEM_ICONE_X));
        }
    }

    /* Este método será chamado quando três valores ou mais estiverem sido colocados. Será verificado se os valores estão
       postos em posições que determinam o ganhador ou o fim do jogo. */

    private String verificarResultado() {

        String [][] jogo = new String[TAMANHO_MATRIZ][TAMANHO_MATRIZ];

        int c = 0;

        // Convertendo estado atual do jogo em uma matriz

        for (int i = 0; i < TAMANHO_MATRIZ; i++) {
            for (int j = 0; j < TAMANHO_MATRIZ; j++) {

                MatrizButton button = (((MatrizButton) matrizGridPane.getChildren().get(c++)));
                jogo[i][j] = button.getText();

            }
        }

        // Verificando se há alguma coluna com valores iguais

        for (int i = 0; i < TAMANHO_MATRIZ; i++) {
            if (!jogo[i][0].isEmpty() && !jogo[i][1].isEmpty() && !jogo[i][2].isEmpty()) {
                if (jogo[i][0].equals(jogo[i][1]) && (jogo[i][1].equals(jogo[i][2]))) {
                    return  jogo[i][0];

                }
            }

        }

        // Verificando se há alguma linha com valores iguais

        for (int i = 0; i < TAMANHO_MATRIZ; i++) {
            if (!jogo[0][i].isEmpty() && !jogo[1][i].isEmpty() && !jogo[2][i].isEmpty()) {
                if (jogo[0][i].equals(jogo[1][i]) && (jogo[1][i].equals(jogo[2][i]))) {
                    return jogo[0][i];
                }
            }
        }


        // Verficando se os valores da diagonal primária e a da secundária possuem o mesmo valor


        if (!jogo[0][0].isEmpty() && !jogo[1][1].isEmpty() && !jogo[2][2].isEmpty()) {
            if ((jogo[0][0].equals(jogo[1][1]) && jogo[1][1].equals(jogo[2][2]))) {
                return jogo[0][0];
            }
        }
        if (!jogo[0][2].isEmpty() && !jogo[1][1].isEmpty() && !jogo[2][0].isEmpty()) {
            if ((jogo[0][2].equals(jogo[1][1]) && (jogo[1][1].equals(jogo[2][0])))) {
                return jogo[1][1];
            }
        }


        return null;

    }

    // Este método será acionado quando o botão ícone - Canto inferior direito - for acionado.

    @FXML
    private void handleAudioButtonClick() {
        if (sliderVolume.getValue() != 0.0) {
            sliderVolume.setValue(0.0);
        } else {
            sliderVolume.setValue(0.5);
        }
    }

    // Quando o mouse estiver sobre o audioButton, o slider volume será posto loga acima do mesmo
    @FXML
    public void handleAudioButtonMouseEntered() {
        sliderVolume.setLayoutX(tamanhoTela.getWidth() * 0.3201);
    }

    // Quando o mouse sair do audioButton, a animação para esconder o slider será iniciada
    @FXML
    public void handleAudioButtonMouseExited() {

        animacaoSliderVolumeTimeline.play();
    }

    /* Se depois que o mouse for posto de cima do audioButton (nesse caso o slider aparecerá), o mouse for colocado sobre
       o sliderVolume, a animação que esconde o slider será cancelada e o slider continuará visível */
    @FXML
    public void handleSliderVolumeMouseEntered() {
        animacaoSliderVolumeTimeline.stop();
    }

    // Quando o mouse sair do slider volume, este será escondido
    @FXML
    private void handleSliderVolumeMouseExited() {
        sliderVolume.setLayoutX(-100.0);
    }

    /* Este método será utilizado para permitir que o usuário realize jogadas utilizando o Teclado. Quando algum número
     * de 1 à 9 pertencente ao NUMPAD do teclado for acionado, o método onAction do respectivo button será acionado */

    @FXML
    private void handleRootKeyPressed(KeyEvent keyEvent) {
        int posicao = 0;

        if (keyEvent.getCode().equals(KeyCode.NUMPAD1)){
            posicao = 1;
        }
        if (keyEvent.getCode().equals(KeyCode.NUMPAD2)){
            posicao = 2;
        }
        if (keyEvent.getCode().equals(KeyCode.NUMPAD3)){
            posicao = 3;
        }
        if (keyEvent.getCode().equals(KeyCode.NUMPAD4)){
            posicao = 4;
        }
        if (keyEvent.getCode().equals(KeyCode.NUMPAD5)){
            posicao = 5;
        }
        if (keyEvent.getCode().equals(KeyCode.NUMPAD6)){
            posicao = 6;
        }
        if (keyEvent.getCode().equals(KeyCode.NUMPAD7)){
            posicao = 7;
        }
        if (keyEvent.getCode().equals(KeyCode.NUMPAD8)){
            posicao = 8;
        }
        if (keyEvent.getCode().equals(KeyCode.NUMPAD9)){
            posicao = 9;
        }

        if (posicao != 0) {

            MatrizButton buttonSelected = null;
            for (Node node : matrizGridPane.getChildren()) {
                MatrizButton button = (MatrizButton) node;
                if (button.getPosicao() ==  posicao) {
                    buttonSelected = button;
                    break;
                }
            }

            if (buttonSelected != null) {
                buttonSelected.fire();
            }

        }


    }

    // Este método foi criado para evitar repetição de Código
    private void exibirAlerta(String title, String headerText, String contextText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(root.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.showAndWait();
    }

    // Getters

    ComboBox<LocatedImage> getIdiomaIconeComboBox() {
        return idiomaIconeComboBox;
    }

    Slider getSliderVolume() {
        return sliderVolume;
    }

    ResourceBundle getResources() {
        return resources;
    }
}
