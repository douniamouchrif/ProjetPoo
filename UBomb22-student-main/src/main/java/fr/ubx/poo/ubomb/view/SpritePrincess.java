package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.go.character.Princess;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;


import static fr.ubx.poo.ubomb.view.ImageResource.*;

public final class SpritePrincess extends Sprite {
    public SpritePrincess(Pane layer, Image image, Princess princess) {
        super(layer, PRINCESS.getImage(), princess);
    }

}
