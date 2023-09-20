package invaders.entities;

import javafx.scene.image.Image;

import java.io.File;

public class RedBunker implements ColourBunker {
    private double width;
    private double height;

    public RedBunker(double width, double height){
        this.width = width;
        this.height = height;
    }
    public void SetColour(Bunker bunker){
        bunker.setImage(new Image(new File("src/main/resources/red_bunker.png").toURI().toString(), width, height, false, true));
    }
}
