package com.raphaelcollin.jogodavelha.subclasses;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

// Classe necessária para exibir Imagem em um ComboBox

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

            double tamanhoImagem = Screen.getPrimary().getBounds().getWidth() * 0.015625; // 30px - 1920px

            imageView.setFitWidth(tamanhoImagem);
            imageView.setFitHeight(tamanhoImagem);
            setGraphic(imageView);
        }
    }
}
