package com.raphaelcollin.jogodavelha.subclasses;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

public class MatrizButton extends Button {

    /* Esta classe foi criada com dois objetivos:
    *
    * 1 - Manter a posição (número que está escrito no button)
    * 2 - Gerenciar a troca de imagens do botão */

    private int posicao;
    private static final String URL_BASE_IMAGEM_ICONE_NUMEROS = "file:img/num-";

    public MatrizButton(int posicao) {
        super();
        this.posicao = posicao;

        Rectangle2D tamanhoTela = Screen.getPrimary().getBounds();

        /* As imagemserão escolhidas de acordo com a posição do button.
        * Existem 9 imagens de número da pasta img e estão nomeadas de acordo com o número delas */

        ImageView imageView = new ImageView(new Image(URL_BASE_IMAGEM_ICONE_NUMEROS + posicao + ".png"));
        imageView.setOpacity(0.45);
        imageView.setFitHeight(tamanhoTela.getWidth() * 0.02604166);
        imageView.setFitWidth(tamanhoTela.getWidth() * 0.02604166);
        this.setGraphic(imageView);

    }

    public int getPosicao() {
        return posicao;
    }

    public void setIconeNumero() {
        ImageView imageView = (ImageView) this.getGraphic();
        imageView.setOpacity(0.45);
        imageView.setImage(new Image(URL_BASE_IMAGEM_ICONE_NUMEROS + posicao + ".png"));
    }

}
