package com.raphaelcollin.tictactoe.subclasses;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

public class MatrixButton extends Button {

    /* This class was created with two goals:
    *
    * 1 - Store the position (The number inside the button)
    * 2 - Manage the images changes in the button */

    private int position;
    private static final String URL_BASE_IMAGE_ICON_NUMBERS = "file:img/num-";

    public MatrixButton(int position) {
        super();
        this.position = position;

        Rectangle2D screenSize = Screen.getPrimary().getBounds();

        ImageView imageView = new ImageView(new Image(URL_BASE_IMAGE_ICON_NUMBERS + position + ".png"));
        imageView.setOpacity(0.45);
        imageView.setFitHeight(screenSize.getWidth() * 0.02604166);
        imageView.setFitWidth(screenSize.getWidth() * 0.02604166);
        this.setGraphic(imageView);

    }

    public int getPosition() {
        return position;
    }

    public void setNumberIcon() {
        ImageView imageView = (ImageView) this.getGraphic();
        imageView.setOpacity(0.45);
        imageView.setImage(new Image(URL_BASE_IMAGE_ICON_NUMBERS + position + ".png"));
    }

}
