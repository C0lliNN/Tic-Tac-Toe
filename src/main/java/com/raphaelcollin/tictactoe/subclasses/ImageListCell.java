package com.raphaelcollin.tictactoe.subclasses;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

// This class is necessary to show an image in a ComboBox

public class ImageListCell extends ListCell<LocatedImage> {
    private final ImageView imageView;

    public ImageListCell() {
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        imageView = new ImageView();
    }

    @Override
    protected void updateItem(LocatedImage item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setGraphic(null);
        } else {
            imageView.setImage(item);

            double imageSize = Screen.getPrimary().getBounds().getWidth() * 0.015625;

            imageView.setFitWidth(imageSize);
            imageView.setFitHeight(imageSize);
            setGraphic(imageView);
        }
    }
}
