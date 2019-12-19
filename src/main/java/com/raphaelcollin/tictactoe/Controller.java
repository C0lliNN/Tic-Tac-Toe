package com.raphaelcollin.tictactoe;

import com.raphaelcollin.tictactoe.subclasses.ImageListCell;
import com.raphaelcollin.tictactoe.subclasses.LocatedImage;
import com.raphaelcollin.tictactoe.subclasses.MatrixButton;
import javafx.animation.*;
import javafx.application.Platform;
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

    @FXML
    private AnchorPane root;
    @FXML
    private HBox titleBarHBox;
    @FXML
    private Label titleLabel;
    @FXML
    private Button closeButton;
    @FXML
    private Button playButton;
    @FXML
    private HBox hBoxAppIconImageView;
    @FXML
    public ImageView appIconImageView;
    @FXML
    private ComboBox<LocatedImage> languageIconComboBox;
    @FXML
    private Button audioIconButton;
    @FXML
    private Slider sliderVolume;

    private Button restartButton;
    private Button pauseOrResumeButton;
    private Button cancelButton;
    private ImageView indicatorVezImageView;
    private GridPane matrixGridPane;
    private Label timeRemainingLabel;
    private Label timeRemainingNumberLabel;
    private Button playIconButton;

    private static final String CLASS_BUTTON_GAME_CONTROL = "game-button-control";
    private static final String CLASS_BUTTON_FIELD = "button-field";

    private static final String ID_TITLE_BAR = "title-bar";
    private static final String ID_BUTTON_CLOSE = "button-close";
    private static final String ID_BUTTON_PLAY = "button-play";
    private static final String ID_BUTTON_AUDIO = "button-audio";
    private static final String ID_BUTTON_PLAY_ICON = "button-play-icon";
    private static final String ID_BUTTON_RESTART = "button-restart";
    private static final String ID_BUTTON_PAUSE_RESUME = "button-pause-resume";
    private static final String ID_BUTTON_CANCEL = "button-cancel";

    private static final String URL_IMAGE_ICON_X = "file:img/x-icon.png";
    private static final String URL_IMAGE_APP_ICON = "file:img/grandmother-icon.png";
    private static final String URL_IMAGE_ICON_PLAY = "file:img/play-icon.png";
    private static final String URL_IMAGE_ICON_O = "file:img/o-icon.png";
    private static final String URL_IMAGE_LANGUAGE_BRAZIL = "file:img/brazil-icon.png";
    private static final String URL_IMAGE_LANGUAGE_US = "file:img/us-icon.png";
    private static final String URL_IMAGE_ICON_AUDIO_MUTED = "file:img/audio-icon-muted.png";
    private static final String URL_IMAGE_ICON_AUDIO_MIN = "file:img/audio-icon-low.png";
    private static final String URL_IMAGE_ICON_AUDIO_MEDIUM = "file:img/audio-icon-medium.png";
    private static final String URL_IMAGE_ICON_AUDIO_MAX = "file:img/audio-icon-full.png";
    private static final String URL_BUNDLE_LANGUAGE = "lang";
    private static final String URL_MUSIC_BACKGROUND = "/music/background-music.mp3";
    private static final String URL_SOUND_EFFECT_BUTTON_CLICK = "/music/button-click.mp3";
    private static final String URL_SOUND_EFFECT_GAME_OVER = "/music/endofgame.mp3";
    private static final String URL_ERROR_SOUND_EFFECT = "/music/error.wav";

    private static final String LANGUAGE_EN_LOCALE = "en";
    private static final String LANGUAGE_PT_LOCALE = "pt";

    private static final String BUNDLE_STRING_KEY_STAGE_TITLE = "stage_title";
    private static final String BUNDLE_STRING_KEY_BUTTON_PLAY = "play_button";
    private static final String BUNDLE_STRING_KEY_ALERT_TITLE = "alert_title";
    private static final String BUNDLE_STRING_KEY_ALERT_TIME_REMAINING_HEADER_TEXT = "alert_times_up_headerText";
    private static final String BUNDLE_STRING_KEY_ALERT_DRAW_HEADER_TEXT = "alert_draw_headerText";
    private static final String BUNDLE_STRING_KEY_ALERT_WINNER_HEADER_TEXT = "alert_winner_headerText";
    private static final String BUNDLE_STRING_KEY_ALERT_CONTEXT_TEXT = "alert_contextText";
    private static final String BUNDLE_STRING_KEY_BUTTON_RESTART = "restart_button";
    private static final String BUNDLE_STRING_KEY_BUTTON_PAUSE = "pause_button";
    private static final String BUNDLE_STRING_KEY_BUTTON_RESUME = "resume_button";
    private static final String BUNDLE_STRING_KEY_BUTTON_CANCEL = "cancel_button";
    private static final String BUNDLE_STRING_KEY_LABEL_TIME_REMAINING = "time_remaining";

    private static final int MATRIX_SIZE = 3;
    private static final int GAME_MAX_TIME = 60;

    private static final String NAME_FONT_BUTTONS = "Arial Bold";

    private int countTime = GAME_MAX_TIME; // Time until the end of the game

    private boolean turn; // false - X has his turn; true - O has his turn

    private int numOfMatrixButtonFilled = 0;

    private boolean gamePaused = false;
    
    private Service<Void> countTempoService;

    private Rectangle2D screenSize = Screen.getPrimary().getBounds();

    private MediaPlayer backgroundMusicMediaPlayer;

    private AudioClip buttonClickAudioClip;

    private AudioClip endOfGameAudioClip;

    private ResourceBundle resources;

    private Timeline animationInitialViewTimeline;

    private Timeline animationSliderVolumeTimeline;

    private double sceneX; // Window movement
    private double sceneY; // Window movement

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.resources = resources;

        Stop[] stops = new Stop[] {
                new Stop(0, Color.rgb(240,154,7)),
                new Stop(1, Color.rgb(240,178,7)),
        };
        LinearGradient linearGradient = new LinearGradient(1, 0, 0, 0, true, CycleMethod.NO_CYCLE, stops);

        root.setBackground(new Background(new BackgroundFill(linearGradient,null,null)));

        double titleBarHeight = screenSize.getHeight() * 0.0324074;

        titleBarHBox.setId(ID_TITLE_BAR);
        titleBarHBox.setMinHeight(titleBarHeight);
        titleBarHBox.setPrefHeight(titleBarHeight);
        titleBarHBox.setMaxHeight(titleBarHeight);

        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setFont(new Font("Arial Bold", screenSize.getWidth() * 0.009375));
        HBox.setMargin(titleLabel, new Insets(0,0,0,screenSize.getWidth() * 0.01041666));

        double closeButtonSize = screenSize.getWidth() * 0.0177083;

        closeButton.setId(ID_BUTTON_CLOSE);
        closeButton.setPrefSize(closeButtonSize,closeButtonSize);
        closeButton.setMinSize(closeButtonSize,closeButtonSize);
        closeButton.setMaxSize(closeButtonSize,closeButtonSize);
        closeButton.setFont(new Font("Verdana",screenSize.getWidth() * 0.01145833));

        playButton.setId(ID_BUTTON_PLAY);
        playButton.setFont(new Font(NAME_FONT_BUTTONS, screenSize.getWidth() * 0.0166666));
        playButton.setPrefWidth(screenSize.getWidth() * 0.1562);
        playButton.setPrefHeight(screenSize.getWidth() * 0.0312);

        appIconImageView.setImage(new Image(URL_IMAGE_APP_ICON));
        appIconImageView.setFitWidth(screenSize.getWidth() * 0.125);
        appIconImageView.setFitHeight(screenSize.getWidth() * 0.125);
        AnchorPane.setTopAnchor(hBoxAppIconImageView, screenSize.getHeight() * 0.15);
        AnchorPane.setLeftAnchor(hBoxAppIconImageView, 0.0);
        AnchorPane.setRightAnchor(hBoxAppIconImageView, 0.0);

        DropShadow dropShadowImageView = new DropShadow(screenSize.getWidth() * 0.00104166, Color.rgb(0,0,0,0.3));
        appIconImageView.setEffect(dropShadowImageView);

        ObservableList<LocatedImage> imageURL = FXCollections.observableArrayList();
        imageURL.add(new LocatedImage(URL_IMAGE_LANGUAGE_BRAZIL, LANGUAGE_PT_LOCALE));
        imageURL.add(new LocatedImage(URL_IMAGE_LANGUAGE_US, LANGUAGE_EN_LOCALE));

        languageIconComboBox.setItems(imageURL);
        languageIconComboBox.setButtonCell(new ImageListCell());
        languageIconComboBox.setCellFactory(listView -> new ImageListCell());
        languageIconComboBox.getSelectionModel().selectFirst();
        languageIconComboBox.setPrefWidth(screenSize.getWidth() * 0.02864);

        languageIconComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String stringLocale = LANGUAGE_EN_LOCALE; // DEFAULT LANGUAGE
                if (newValue.getImageUrl().equals(URL_IMAGE_LANGUAGE_US)) {
                    stringLocale = LANGUAGE_EN_LOCALE;
                } else if (newValue.getImageUrl().equals(URL_IMAGE_LANGUAGE_BRAZIL)){
                    stringLocale = LANGUAGE_PT_LOCALE;
                }
                this.resources = ResourceBundle.getBundle(URL_BUNDLE_LANGUAGE,new Locale(stringLocale));
                setTextInControls();
                try {
                    ((Stage) root.getScene().getWindow()).setTitle(this.resources.getString(BUNDLE_STRING_KEY_STAGE_TITLE));
                } catch (NullPointerException e) {
                    System.err.println("A View haven't load fully yet");
                }

            }
        });

        AnchorPane.setBottomAnchor(languageIconComboBox, screenSize.getHeight() * 0.0185185);
        AnchorPane.setRightAnchor(languageIconComboBox, screenSize.getWidth() * 0.036458);

        ImageView audioIconImageView = new ImageView(new Image(URL_IMAGE_ICON_AUDIO_MUTED));
        audioIconImageView.setFitHeight(screenSize.getWidth() * 0.015625);
        audioIconImageView.setFitWidth(screenSize.getWidth() * 0.015625);
        audioIconButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        audioIconButton.setGraphic(audioIconImageView);
        audioIconButton.setId(ID_BUTTON_AUDIO);
        AnchorPane.setBottomAnchor(audioIconButton, screenSize.getHeight() * 0.018518);
        AnchorPane.setRightAnchor(audioIconButton, screenSize.getWidth() * 0.015625);

        backgroundMusicMediaPlayer = new MediaPlayer(new Media(getClass().getResource(URL_MUSIC_BACKGROUND).toExternalForm()));
        backgroundMusicMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundMusicMediaPlayer.volumeProperty().bind(sliderVolume.valueProperty());

        sliderVolume.setPrefHeight(screenSize.getHeight() * 0.092592);
        sliderVolume.setLayoutY(screenSize.getHeight() * 0.3);
        sliderVolume.setLayoutX(-screenSize.getWidth() * 0.05208);

        sliderVolume.setPadding(new Insets(screenSize.getHeight() * 0.0074074, screenSize.getWidth() * 0.00625,
                screenSize.getHeight() * 0.0074074, screenSize.getWidth() * 0.00625));

        sliderVolume.valueProperty().addListener(((observable, oldValue, newValue) -> {

            LocatedImage image;
            double volume = (Double) newValue;
            ImageView imageView = (ImageView) audioIconButton.getGraphic();
            if (volume == 0.0){
                image = new LocatedImage(URL_IMAGE_ICON_AUDIO_MUTED);
            } else if (volume > 0.0 && volume < 0.5) {
                image = new LocatedImage(URL_IMAGE_ICON_AUDIO_MIN);
            } else if (volume >= 0.5 && volume < 1.0) {
                image = new LocatedImage(URL_IMAGE_ICON_AUDIO_MEDIUM);
            } else {
                image = new LocatedImage(URL_IMAGE_ICON_AUDIO_MAX);
            }

            imageView.setImage(image);

        }));


        /* Animations */

        

        KeyValue animationInitialViewValue1 = new KeyValue(playButton.layoutXProperty(),0.0);
        KeyFrame animationInitialViewFrame1 = new KeyFrame(Duration.millis(0), animationInitialViewValue1);
        KeyValue animationInitialViewValue2 = new KeyValue(playButton.layoutXProperty(),
                screenSize.getWidth() * 0.114583);
        KeyFrame animationInitialViewFrame2 = new KeyFrame(Duration.millis(500),animationInitialViewValue2);
        KeyValue animationInitialViewValue3 = new KeyValue(playButton.layoutXProperty(),
                screenSize.getWidth() * 0.0859375);
        KeyFrame animationInitialViewFrame3 = new KeyFrame(Duration.millis(700),animationInitialViewValue3);
        KeyValue animationInitialViewValue4 = new KeyValue(playButton.layoutXProperty(),
                screenSize.getWidth() * 0.095479);
        KeyFrame animationInitialViewFrame4 = new KeyFrame(Duration.millis(900),animationInitialViewValue4);
        KeyValue animationInitialViewValue5 = new KeyValue(playButton.opacityProperty(),0.0);
        KeyFrame animationInitialViewFrame5 = new KeyFrame(Duration.millis(0),animationInitialViewValue5);
        KeyValue animationInitialViewValue6 = new KeyValue(playButton.opacityProperty(),1.0);
        KeyFrame animationInitialViewFrame6 = new KeyFrame(Duration.millis(900),animationInitialViewValue6);
        KeyValue animationInitialViewValue7 = new KeyValue(playButton.layoutYProperty(),0.0);
        KeyFrame animationInitialViewFrame7 = new KeyFrame(Duration.millis(0), animationInitialViewValue7);
        KeyValue animationInitialViewValue8 = new KeyValue(playButton.layoutYProperty(),
                screenSize.getHeight() * 0.043);
        KeyFrame animationInitialViewFrame8 = new KeyFrame(Duration.millis(500), animationInitialViewValue8);
        KeyValue animationInitialViewValue9 = new KeyValue(playButton.layoutYProperty(),
                screenSize.getHeight() * 0.038);
        KeyFrame animationInitialViewFrame9 = new KeyFrame(Duration.millis(700), animationInitialViewValue9);
        KeyValue animationInitialViewValue10 = new KeyValue(playButton.layoutYProperty(),
                screenSize.getHeight() * 0.07);
        KeyFrame animationInitialViewFrame10 = new KeyFrame(Duration.millis(900), animationInitialViewValue10);
        KeyValue animationInitialViewValue11 = new KeyValue(playButton.disableProperty(), true);
        KeyFrame animationInitialViewFrame11 = new KeyFrame(Duration.millis(0), animationInitialViewValue11);
        KeyValue animationInitialViewValue12 = new KeyValue(playButton.disableProperty(), false);
        KeyFrame animationInitialViewFrame12 = new KeyFrame(Duration.millis(900), animationInitialViewValue12);
        KeyValue animationInitialViewValue13 = new KeyValue(appIconImageView.opacityProperty(), 0.0);
        KeyFrame animationInitialViewFrame13 = new KeyFrame(Duration.millis(700), animationInitialViewValue13);
        KeyValue animationInitialViewValue14 = new KeyValue(appIconImageView.opacityProperty(), 1.0);
        KeyFrame animationInitialViewFrame14 = new KeyFrame(Duration.millis(1500), animationInitialViewValue14);

        animationInitialViewTimeline = new Timeline(animationInitialViewFrame1, animationInitialViewFrame2,
                animationInitialViewFrame3, animationInitialViewFrame4, animationInitialViewFrame5,
                animationInitialViewFrame6, animationInitialViewFrame7, animationInitialViewFrame8, animationInitialViewFrame9,
                animationInitialViewFrame10, animationInitialViewFrame11, animationInitialViewFrame12,
                animationInitialViewFrame13, animationInitialViewFrame14);

        animationInitialViewTimeline.setDelay(Duration.seconds(0.8));

        animationInitialViewTimeline.setOnFinished(event -> backgroundMusicMediaPlayer.play());

        animationInitialViewTimeline.play();

        /* Animação - ComboBox Idioma e AudioButton*/

        KeyValue animationConfigAreaValue1 = new KeyValue(languageIconComboBox.opacityProperty(),0.0);
        KeyFrame animationConfigAreaFrame1 = new KeyFrame(Duration.millis(0), animationConfigAreaValue1);
        KeyValue animationConfigAreaValue2 = new KeyValue(languageIconComboBox.opacityProperty(),1.0);
        KeyFrame animationConfigAreaFrame2 = new KeyFrame(Duration.millis(1000), animationConfigAreaValue2);
        KeyValue animationConfigAreaValue3 = new KeyValue(audioIconButton.opacityProperty(), 0.0);
        KeyFrame animationConfigAreaFrame3 = new KeyFrame(Duration.millis(0), animationConfigAreaValue3);
        KeyValue animationConfigAreaValue4 = new KeyValue(audioIconButton.opacityProperty(), 1.0);
        KeyFrame animationConfigAreaFrame4 = new KeyFrame(Duration.millis(1000), animationConfigAreaValue4);

        Timeline animationConfigAreaTimeline = new Timeline(animationConfigAreaFrame1, animationConfigAreaFrame2,
                animationConfigAreaFrame3, animationConfigAreaFrame4);
        animationConfigAreaTimeline.setDelay(Duration.seconds(2));
        animationConfigAreaTimeline.play();

        animationConfigAreaTimeline.setOnFinished(event -> {
            languageIconComboBox.setDisable(false);
            audioIconButton.setDisable(false);
        });
        

        KeyValue animationSliderValue1 = new KeyValue(sliderVolume.layoutXProperty(), screenSize.getWidth() *
                screenSize.getWidth() * 0.3201);
        KeyFrame animationSliderFrame1 = new KeyFrame(Duration.millis(0), animationSliderValue1);
        KeyValue animationSliderValue2 = new KeyValue(sliderVolume.layoutXProperty(),-screenSize.getWidth() * 0.05208);
        KeyFrame animationSliderFrame2 = new KeyFrame(Duration.millis(1), animationSliderValue2);
        animationSliderVolumeTimeline = new Timeline(animationSliderFrame1, animationSliderFrame2);
        animationSliderVolumeTimeline.setDelay(Duration.seconds(0.3));

        countTempoService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        while (countTime >= 0) {
                            updateMessage(String.format("%d", countTime));
                            Thread.sleep(1000);
                            countTime--;
                        }

                        return null;
                    }
                };
            }
        };

        // If the count hit (0 seg), an alert should be shown and the game should restart

        countTempoService.setOnSucceeded(event -> {
            endOfGameAudioClip.play();
            showAlert(this.resources.getString(BUNDLE_STRING_KEY_ALERT_TITLE),
                    this.resources.getString(BUNDLE_STRING_KEY_ALERT_TIME_REMAINING_HEADER_TEXT),
                    this.resources.getString(BUNDLE_STRING_KEY_ALERT_CONTEXT_TEXT));
            if (restartButton != null) {
                restartButton.fire();
            }

        });

        buttonClickAudioClip = new AudioClip(getClass().getResource(URL_SOUND_EFFECT_BUTTON_CLICK).toExternalForm());
        buttonClickAudioClip.volumeProperty().bind(sliderVolume.valueProperty());

        endOfGameAudioClip = new AudioClip(getClass().getResource(URL_SOUND_EFFECT_GAME_OVER).
                toExternalForm());
        endOfGameAudioClip.volumeProperty().bind(sliderVolume.valueProperty());

        setTextInControls();

    }

   
    private void setTextInControls(){

        playButton.setText(resources.getString(BUNDLE_STRING_KEY_BUTTON_PLAY).trim());
        if (restartButton != null) {
            restartButton.setText(resources.getString(BUNDLE_STRING_KEY_BUTTON_RESTART).trim());
        }
        if (pauseOrResumeButton != null) {
            if (gamePaused) {
               pauseOrResumeButton.setText(resources.getString(BUNDLE_STRING_KEY_BUTTON_RESUME));
            } else {
                pauseOrResumeButton.setText(resources.getString(BUNDLE_STRING_KEY_BUTTON_PAUSE).trim());
            }

        }
        if (cancelButton != null) {
            cancelButton.setText(resources.getString(BUNDLE_STRING_KEY_BUTTON_CANCEL).trim());
        }

        if (timeRemainingLabel != null) {
            timeRemainingLabel.setText(resources.getString(BUNDLE_STRING_KEY_LABEL_TIME_REMAINING).trim());
        }

    }
    

    @FXML
    private void handlePlayOnAction() {

        buttonClickAudioClip.play();

        double buttonWidth = screenSize.getWidth() * 0.07;
        double buttonHeight = screenSize.getHeight() * 0.046296;

        restartButton = new Button(resources.getString(BUNDLE_STRING_KEY_BUTTON_RESTART));
        restartButton.getStyleClass().add(CLASS_BUTTON_GAME_CONTROL);
        restartButton.setId(ID_BUTTON_RESTART);
        restartButton.setPrefSize(buttonWidth, buttonHeight);
        restartButton.setFont(new Font(NAME_FONT_BUTTONS, screenSize.getWidth() * 0.012));
        restartButton.setLayoutY(screenSize.getHeight() * 0.06);
        restartButton.setLayoutX(-screenSize.getWidth() * 0.10416);
        restartButton.setOnAction(this::handleRestartButtonOnAction);

        pauseOrResumeButton = new Button(resources.getString(BUNDLE_STRING_KEY_BUTTON_PAUSE));
        pauseOrResumeButton.getStyleClass().add(CLASS_BUTTON_GAME_CONTROL);
        pauseOrResumeButton.setId(ID_BUTTON_PAUSE_RESUME);
        pauseOrResumeButton.setPrefSize(buttonWidth, buttonHeight);
        pauseOrResumeButton.setFont(new Font(NAME_FONT_BUTTONS, screenSize.getWidth() * 0.012));
        pauseOrResumeButton.setLayoutX(screenSize.getWidth() * 0.145833);
        pauseOrResumeButton.setLayoutY(-screenSize.getHeight() * 0.09259);
        pauseOrResumeButton.setOnAction(this::handlePauseButtonOnAction);

        cancelButton = new Button(resources.getString(BUNDLE_STRING_KEY_BUTTON_CANCEL));
        cancelButton.getStyleClass().add(CLASS_BUTTON_GAME_CONTROL);
        cancelButton.setId(ID_BUTTON_CANCEL);
        cancelButton.setPrefSize(buttonWidth, buttonHeight);
        cancelButton.setFont(new Font(NAME_FONT_BUTTONS, screenSize.getWidth() * 0.012));
        cancelButton.setLayoutY(screenSize.getHeight() * 0.06);
        cancelButton.setLayoutX(screenSize.getWidth() * 0.41666);

        root.getChildren().addAll(restartButton, pauseOrResumeButton, cancelButton);

        indicatorVezImageView = new ImageView(new Image(URL_IMAGE_ICON_X));
        indicatorVezImageView.setFitHeight(screenSize.getWidth() * 0.018229);
        indicatorVezImageView.setFitWidth(screenSize.getWidth() * 0.018229);
        indicatorVezImageView.setEffect(new DropShadow(screenSize.getWidth() * 0.00260416,Color.rgb(0,0,0,0.4)));
        indicatorVezImageView.setLayoutX(screenSize.getWidth() * 0.30989);
        indicatorVezImageView.setLayoutY(-screenSize.getHeight() * 0.092592);

        root.getChildren().add(indicatorVezImageView);

        double matrixSizeButton = screenSize.getWidth() * 0.041;

        matrixGridPane = new GridPane();
        matrixGridPane.setVgap(screenSize.getWidth() * 0.0026041);
        matrixGridPane.setHgap(screenSize.getWidth() * 0.0026041);


        int countForFileName = 7;

        for (int i = 0; i < MATRIX_SIZE; i++) {

            int countAux = countForFileName;

            for (int j = 0; j < MATRIX_SIZE; j++) {

                MatrixButton matrixButton = new MatrixButton(countAux);
                matrixButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                matrixButton.setOnMouseEntered(this::handleMatrixButtonMouseEntered);
                matrixButton.setOnMouseExited(event -> matrixButton.setNumberIcon());
                matrixButton.setOnAction(this::handleValidMatrixButtonOnAction);

                matrixButton.setAlignment(Pos.CENTER);
                matrixButton.getStyleClass().add(CLASS_BUTTON_FIELD);
                matrixButton.setPrefSize(matrixSizeButton, matrixSizeButton);
                matrixGridPane.add(matrixButton, j, i);

                countAux++;
            }

            countForFileName = countForFileName - 3;
        }


        matrixGridPane.setLayoutX(- screenSize.getWidth() * 0.15625);
        matrixGridPane.setLayoutY(screenSize.getHeight() * 0.14037);

        root.getChildren().add(matrixGridPane);

        Color rectangleColor = Color.rgb(100,100,100);

        double rectangleSize = screenSize.getWidth() *  0.00208333;
        double rectangleSize2 = screenSize.getHeight() * 0.2296296;

        Rectangle rectangle1 = new Rectangle(rectangleSize, rectangleSize2, rectangleColor);
        rectangle1.setLayoutX(- screenSize.getWidth() * 0.15625);
        rectangle1.setLayoutY(screenSize.getHeight() * 0.140370);

        Rectangle rectangle2 = new Rectangle(rectangleSize, rectangleSize2, rectangleColor);
        rectangle2.setLayoutX(-screenSize.getWidth() * 0.052083);
        rectangle2.setLayoutY(screenSize.getHeight() * 0.140370);

        Rectangle rectangle3 = new Rectangle(rectangleSize2, rectangleSize, rectangleColor);
        rectangle3.setLayoutX(screenSize.getWidth() * 0.117);
        rectangle3.setLayoutY(-screenSize.getHeight() * 0.092592);

        Rectangle rectangle4 = new Rectangle(rectangleSize2, rectangleSize, rectangleColor);
        rectangle4.setLayoutX(screenSize.getWidth() * 0.117);
        rectangle4.setLayoutY(-screenSize.getHeight() * 0.092592);

        root.getChildren().addAll(rectangle1, rectangle2, rectangle3, rectangle4);

        HBox timeRemainingHBox = new HBox(screenSize.getWidth() * 0.0052083);

        timeRemainingLabel = new Label(resources.getString(BUNDLE_STRING_KEY_LABEL_TIME_REMAINING));
        timeRemainingLabel.setFont(new Font(screenSize.getWidth() * 0.0119791));

        timeRemainingNumberLabel = new Label();
        timeRemainingNumberLabel.textProperty().bind(countTempoService.messageProperty());
        timeRemainingNumberLabel.setFont(new Font(screenSize.getWidth() * 0.0119791));
        timeRemainingNumberLabel.setTextFill(Color.GREEN);

        timeRemainingHBox.getChildren().addAll(timeRemainingLabel, timeRemainingNumberLabel);
        timeRemainingHBox.setLayoutX(screenSize.getWidth() * 0.0104166);
        timeRemainingHBox.setLayoutY(screenSize.getHeight() * 0.5555555);

        root.getChildren().add(timeRemainingHBox);

        

        KeyValue animationPlayValue1 = new KeyValue(playButton.layoutYProperty(),
                screenSize.getHeight() * 0.04);
        KeyFrame animationPlayFrame1 = new KeyFrame(Duration.millis(0), animationPlayValue1);
        KeyValue animationPlayValue2 = new KeyValue(playButton.layoutYProperty(),
                -screenSize.getHeight() * 0.0925925);
        KeyFrame animationPlayFrame2 = new KeyFrame(Duration.millis(300), animationPlayValue2);
        KeyValue animationPlayValue3 = new KeyValue(playButton.opacityProperty(),1.0);
        KeyFrame animationPlayFrame3 = new KeyFrame(Duration.millis(100), animationPlayValue3);
        KeyValue animationPlayValue4 = new KeyValue(playButton.opacityProperty(), 0.0);
        KeyFrame animationPlayFrame4 = new KeyFrame(Duration.millis(300), animationPlayValue4);
        KeyValue animationPlayValue5 = new KeyValue(appIconImageView.opacityProperty(), 1.0);
        KeyFrame animationPlayFrame5 = new KeyFrame(Duration.millis(0), animationPlayValue5);
        KeyValue animationPlayValue6 = new KeyValue(appIconImageView.opacityProperty(), 0.0);
        KeyFrame animationPlayFrame6 = new KeyFrame(Duration.millis(300), animationPlayValue6);
        KeyValue animationPlayValue7 = new KeyValue(restartButton.layoutXProperty(),
                -screenSize.getWidth() * 0.1041666);
        KeyFrame animationPlayFrame7 = new KeyFrame(Duration.millis(300), animationPlayValue7);
        KeyValue animationPlayValue8 = new KeyValue(restartButton.layoutXProperty(),
                screenSize.getWidth() * 0.072916);
        KeyFrame animationPlayFrame8 = new KeyFrame(Duration.millis(600), animationPlayValue8);
        KeyValue animationPlayValue9 = new KeyValue(pauseOrResumeButton.layoutYProperty(),
                -screenSize.getHeight() * 0.092592);
        KeyFrame animationPlayFrame9 = new KeyFrame(Duration.millis(300), animationPlayValue9);
        KeyValue animationPlayValue10 = new KeyValue(pauseOrResumeButton.layoutYProperty(),
                screenSize.getHeight() * 0.06);
        KeyFrame animationPlayFrame10 = new KeyFrame(Duration.millis(600), animationPlayValue10);
        KeyValue animationPlayValue11 = new KeyValue(cancelButton.layoutXProperty(),
                screenSize.getWidth() * 0.416666);
        KeyFrame animationPlayFrame11 = new KeyFrame(Duration.millis(300), animationPlayValue11);
        KeyValue animationPlayValue12 = new KeyValue(cancelButton.layoutXProperty(),
                screenSize.getWidth() * 0.21875);
        KeyFrame animationPlayFrame12 = new KeyFrame(Duration.millis(600), animationPlayValue12);
        KeyValue animationPlayValue13 = new KeyValue(indicatorVezImageView.layoutYProperty(),
                -screenSize.getHeight() * 0.092592);
        KeyFrame animationPlayFrame13 = new KeyFrame(Duration.millis(300), animationPlayValue13);
        KeyValue animationPlayValue14 = new KeyValue(indicatorVezImageView.layoutYProperty(),
                screenSize.getHeight() * 0.0681481);
        KeyFrame animationPlayFrame14 = new KeyFrame(Duration.millis(600), animationPlayValue14);
        KeyValue animationPlayValue15 = new KeyValue(matrixGridPane.layoutXProperty(),
                -screenSize.getWidth() * 0.15625);
        KeyFrame animationPlayFrame15 = new KeyFrame(Duration.millis(300), animationPlayValue15);
        KeyValue animationPlayValue16 = new KeyValue(matrixGridPane.layoutXProperty(), screenSize.getWidth() * 0.117);
        KeyFrame animationPlayFrame16 = new KeyFrame(Duration.millis(600), animationPlayValue16);
        KeyValue animationPlayValue17 = new KeyValue(rectangle1.layoutXProperty(),-screenSize.getWidth() * 0.052083);
        KeyFrame animationPlayFrame17 = new KeyFrame(Duration.millis(300),animationPlayValue17);
        KeyValue animationPlayValue18 = new KeyValue(rectangle1.layoutXProperty(), screenSize.getWidth() * 0.159);
        KeyFrame animationPlayFrame18 = new KeyFrame(Duration.millis(600),animationPlayValue18);
        KeyValue animationPlayValue19 = new KeyValue(rectangle2.layoutXProperty(),-screenSize.getWidth() * 0.052083);
        KeyFrame animationPlayFrame19 = new KeyFrame(Duration.millis(300),animationPlayValue19);
        KeyValue animationPlayValue20 = new KeyValue(rectangle2.layoutXProperty(), screenSize.getWidth() * 0.202);
        KeyFrame animationPlayFrame20 = new KeyFrame(Duration.millis(600),animationPlayValue20);
        KeyValue animationPlayValue21 = new KeyValue(rectangle3.layoutYProperty(), -screenSize.getHeight() * 0.092592);
        KeyFrame animationPlayFrame21 = new KeyFrame(Duration.millis(300), animationPlayValue21);
        KeyValue animationPlayValue22 = new KeyValue(rectangle3.layoutYProperty(), screenSize.getHeight() * 0.2144444);
        KeyFrame animationPlayFrame22 = new KeyFrame(Duration.millis(600), animationPlayValue22);
        KeyValue animationPlayValue23 = new KeyValue(rectangle4.layoutYProperty(), -screenSize.getWidth() * 0.052083);
        KeyFrame animationPlayFrame23 = new KeyFrame(Duration.millis(300), animationPlayValue23);
        KeyValue animationPlayValue24 = new KeyValue(rectangle4.layoutYProperty(), screenSize.getHeight() * 0.293148);
        KeyFrame animationPlayFrame24 = new KeyFrame(Duration.millis(600), animationPlayValue24);
        KeyValue animationPlayValue25 = new KeyValue(timeRemainingHBox.layoutYProperty(),
                screenSize.getHeight() * 0.555555);
        KeyFrame animationPlayFrame25 = new KeyFrame(Duration.millis(300), animationPlayValue25);
        KeyValue animationPlayValue26 = new KeyValue(timeRemainingHBox.layoutYProperty(),
                screenSize.getHeight() * 0.407407);
        KeyFrame animationPlayFrame26 = new KeyFrame(Duration.millis(600), animationPlayValue26);

        Animation animationPlayTimeline = new Timeline(animationPlayFrame1, animationPlayFrame2, animationPlayFrame3,
                animationPlayFrame4, animationPlayFrame5, animationPlayFrame6, animationPlayFrame7, animationPlayFrame8,
                animationPlayFrame9, animationPlayFrame10, animationPlayFrame11, animationPlayFrame12, animationPlayFrame13,
                animationPlayFrame14, animationPlayFrame15, animationPlayFrame16, animationPlayFrame17, animationPlayFrame18,
                animationPlayFrame19, animationPlayFrame20, animationPlayFrame21, animationPlayFrame22, animationPlayFrame23,
                animationPlayFrame24, animationPlayFrame25, animationPlayFrame26);
        animationPlayTimeline.play();

        animationPlayTimeline.setAutoReverse(false);

        cancelButton.setOnAction(event -> {

            buttonClickAudioClip.play();

            if (gamePaused) {
                handleResumeOnAction(null);
            }

            animationPlayTimeline.setRate(-1);
            animationPlayTimeline.jumpTo(Duration.millis(600));
            animationPlayTimeline.play();
            animationPlayTimeline.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.lessThanOrEqualTo(Duration.millis(300))){
                    animationPlayTimeline.stop();
                    animationInitialViewTimeline.setDelay(Duration.seconds(0));
                    animationInitialViewTimeline.play();
                }
            });

            this.turn = false;
            countTime = GAME_MAX_TIME;
            this.numOfMatrixButtonFilled = 0;
            countTempoService.cancel();

        });

        this.turn = false;
        countTime = GAME_MAX_TIME;
        this.numOfMatrixButtonFilled = 0;

        countTempoService.restart();

    }


    private void handleRestartButtonOnAction(ActionEvent event) {

        buttonClickAudioClip.play();

        if (gamePaused) {
            handleResumeOnAction(null);
        }

        matrixGridPane.getChildren().forEach(node -> {
            MatrixButton button = (MatrixButton) node;
            button.setText("");
            button.setOnMouseEntered(this::handleMatrixButtonMouseEntered);
            button.setOnMouseExited(e -> button.setNumberIcon());
            button.setOnAction(this::handleValidMatrixButtonOnAction);
            button.setNumberIcon();
        });

        indicatorVezImageView.setImage(new Image(URL_IMAGE_ICON_X));

        turn = false;
        numOfMatrixButtonFilled = 0;
        countTime = GAME_MAX_TIME;

        countTempoService.restart();

    }


    private void handlePauseButtonOnAction(ActionEvent event) {

        buttonClickAudioClip.play();

        matrixGridPane.getChildren().forEach(node -> node.setDisable(true));

        countTempoService.cancel();

        if (playIconButton == null) {
            playIconButton = new Button();
            ImageView imageView = new ImageView(new Image(URL_IMAGE_ICON_PLAY));
            imageView.setFitWidth(screenSize.getWidth() * 0.09375);
            imageView.setFitHeight(screenSize.getWidth() * 0.09375);
            imageView.setEffect(new DropShadow(10, Color.rgb(0,0,0,0.2)));
            playIconButton.setGraphic(imageView);
            playIconButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            playIconButton.setLayoutY(screenSize.getHeight() * 0.15);
            playIconButton.setLayoutX(screenSize.getWidth() * 0.13);
            playIconButton.setId(ID_BUTTON_PLAY_ICON);
            playIconButton.setOnAction(this::handleResumeOnAction);
        }

        pauseOrResumeButton.setOnAction(this::handleResumeOnAction);
        pauseOrResumeButton.setText(resources.getString(BUNDLE_STRING_KEY_BUTTON_RESUME));

        timeRemainingNumberLabel.setTextFill(Color.RED);

        root.getChildren().add(playIconButton);

        gamePaused = true;
    }


    private void handleResumeOnAction(ActionEvent event) {

        if (event != null) {
            buttonClickAudioClip.play();
        }

        matrixGridPane.getChildren().forEach(node -> node.setDisable(false));

        pauseOrResumeButton.setOnAction(this::handlePauseButtonOnAction);
        pauseOrResumeButton.setText(resources.getString(BUNDLE_STRING_KEY_BUTTON_PAUSE));

        root.getChildren().remove(playIconButton);

        timeRemainingNumberLabel.setTextFill(Color.GREEN);

        gamePaused = false;
        countTempoService.restart();
    }

    private void handleValidMatrixButtonOnAction(ActionEvent event) {
        buttonClickAudioClip.play();
        MatrixButton button = (MatrixButton) event.getSource();
        ImageView imageView = (ImageView) button.getGraphic();
        imageView.setOpacity(1.0);
        if (turn) {
            button.setText("o");
            imageView.setImage(new Image(URL_IMAGE_ICON_O));
            indicatorVezImageView.setImage(new Image(URL_IMAGE_ICON_X));
        } else {
            button.setText("x");
            imageView.setImage(new Image(URL_IMAGE_ICON_X));
            indicatorVezImageView.setImage(new Image(URL_IMAGE_ICON_O));
        }

        button.setOnMouseEntered(null);
        button.setOnMouseExited(null);
        button.setOnAction(this::handleInvalidMatrixButtonOnAction);

        turn = !turn;
        numOfMatrixButtonFilled++;

        ScaleTransition transition = new ScaleTransition(Duration.millis(150), indicatorVezImageView);
        transition.setFromX(1);
        transition.setToX(1.3);
        transition.setFromY(1);
        transition.setToY(1.3);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.playFromStart();

        if (numOfMatrixButtonFilled > 4) {
            String vencedor = checkResult();
            if (vencedor != null) {
                transition.stop();
                endOfGameAudioClip.play();
                showAlert(resources.getString(BUNDLE_STRING_KEY_ALERT_TITLE),
                        vencedor.toUpperCase() + " " + resources.getString(BUNDLE_STRING_KEY_ALERT_WINNER_HEADER_TEXT)
                        , resources.getString(BUNDLE_STRING_KEY_ALERT_CONTEXT_TEXT));
                this.handleRestartButtonOnAction(null);
            } else {
                if (numOfMatrixButtonFilled == 9) {
                    endOfGameAudioClip.play();
                    showAlert(resources.getString(BUNDLE_STRING_KEY_ALERT_TITLE)
                            ,resources.getString(BUNDLE_STRING_KEY_ALERT_DRAW_HEADER_TEXT),
                            resources.getString(BUNDLE_STRING_KEY_ALERT_CONTEXT_TEXT));
                    this.handleRestartButtonOnAction(null);
                }
            }
        }

    }

    private void handleInvalidMatrixButtonOnAction(ActionEvent event) {
        AudioClip audioClipError = new AudioClip(getClass().getResource(URL_ERROR_SOUND_EFFECT).toExternalForm());
        audioClipError.play(sliderVolume.getValue());
    }

    private void handleMatrixButtonMouseEntered(MouseEvent event) {
        ImageView imageView = (ImageView) ((MatrixButton)event.getSource()).getGraphic();

        if (turn) {
            imageView.setImage(new Image(URL_IMAGE_ICON_O));
        } else {
            imageView.setImage(new Image(URL_IMAGE_ICON_X));
        }
    }

    private String checkResult() {

        String [][] game = new String[MATRIX_SIZE][MATRIX_SIZE];

        int c = 0;

        // Converting current state of the game in a Matrix

        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {

                MatrixButton button = (((MatrixButton) matrixGridPane.getChildren().get(c++)));
                game[i][j] = button.getText();

            }
        }

        // Checking if there is some column with equal values

        for (int i = 0; i < MATRIX_SIZE; i++) {
            if (!game[i][0].isEmpty() && !game[i][1].isEmpty() && !game[i][2].isEmpty()) {
                if (game[i][0].equals(game[i][1]) && (game[i][1].equals(game[i][2]))) {
                    return  game[i][0];

                }
            }

        }

        // Checking if there is some row with equal values

        for (int i = 0; i < MATRIX_SIZE; i++) {
            if (!game[0][i].isEmpty() && !game[1][i].isEmpty() && !game[2][i].isEmpty()) {
                if (game[0][i].equals(game[1][i]) && (game[1][i].equals(game[2][i]))) {
                    return game[0][i];
                }
            }
        }


        // Checking if there is some diagonal with equal values


        if (!game[0][0].isEmpty() && !game[1][1].isEmpty() && !game[2][2].isEmpty()) {
            if ((game[0][0].equals(game[1][1]) && game[1][1].equals(game[2][2]))) {
                return game[0][0];
            }
        }
        if (!game[0][2].isEmpty() && !game[1][1].isEmpty() && !game[2][0].isEmpty()) {
            if ((game[0][2].equals(game[1][1]) && (game[1][1].equals(game[2][0])))) {
                return game[1][1];
            }
        }


        return null;

    }

    @FXML
    private void handleAudioButtonClick() {
        if (sliderVolume.getValue() != 0.0) {
            sliderVolume.setValue(0.0);
        } else {
            sliderVolume.setValue(0.5);
        }
    }

    @FXML
    public void handleAudioButtonMouseEntered() {
        sliderVolume.setLayoutX(screenSize.getWidth() * 0.3135);
    }

    @FXML
    public void handleAudioButtonMouseExited() {

        animationSliderVolumeTimeline.play();
    }

    @FXML
    public void handleSliderVolumeMouseEntered() {
        animationSliderVolumeTimeline.stop();
    }

    @FXML
    private void handleSliderVolumeMouseExited() {
        sliderVolume.setLayoutX(-100.0);
    }

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

            MatrixButton buttonSelected = null;
            for (Node node : matrixGridPane.getChildren()) {
                MatrixButton button = (MatrixButton) node;
                if (button.getPosition() ==  posicao) {
                    buttonSelected = button;
                    break;
                }
            }

            if (buttonSelected != null) {
                buttonSelected.fire();
            }

        }


    }

    private void showAlert(String title, String headerText, String contextText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(root.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.showAndWait();
    }

    // Getters

    ComboBox<LocatedImage> getLanguageIconComboBox() {
        return languageIconComboBox;
    }

    Slider getSliderVolume() {
        return sliderVolume;
    }

    ResourceBundle getResources() {
        return resources;
    }

    @FXML
    public void handleCloseWindow() {
        Platform.exit();
    }

    public void handleMousePressed(MouseEvent mouseEvent) {
        sceneX = mouseEvent.getSceneX();
        sceneY = mouseEvent.getSceneY();
    }

    public void handleMouseDragged(MouseEvent mouseEvent) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - sceneX);
        stage.setY(mouseEvent.getScreenY() - sceneY);
    }
}
