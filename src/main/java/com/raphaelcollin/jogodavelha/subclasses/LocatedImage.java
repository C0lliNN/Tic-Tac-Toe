package com.raphaelcollin.jogodavelha.subclasses;

import javafx.scene.image.Image;

/* Esta classe foi criada obter a URL da imagem mais facilmente. Essa classe facilitará o controle do idioma da aplicação.*/

public class LocatedImage extends Image {
    private String url;
    private String idioma;

    public LocatedImage(String url) {
        super(url);
        this.url = url;
    }

    public LocatedImage(String url, String idioma) {
        super(url);
        this.url = url;
        this.idioma = idioma;
    }

    public String getUrl() {
        return url;
    }

    public String getIdioma() {
        return idioma;
    }
}
