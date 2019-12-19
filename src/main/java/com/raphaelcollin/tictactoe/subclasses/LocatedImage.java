package com.raphaelcollin.tictactoe.subclasses;

import javafx.scene.image.Image;

/* This class was created to store de URL of the image.*/

public class LocatedImage extends Image {
    private String url;
    private String language;

    public LocatedImage(String url) {
        super(url);
        this.url = url;
    }

    public LocatedImage(String url, String language) {
        super(url);
        this.url = url;
        this.language = language;
    }

    public String getImageUrl() {
        return url;
    }

    public String getLanguage() {
        return language;
    }
}
