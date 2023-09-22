package invaders.entities;



import invaders.physics.Vector2D;

import invaders.rendering.Renderable;
import invaders.physics.GameLoop;


import javafx.scene.image.Image;

import java.io.File;

public class PlayerProjectile implements Projectile,Renderable  {
   private  Vector2D position = new Vector2D(0,0);
   private final Image image;
   private final double width = 10;
   private final double height = 10;
   private boolean active = true; // life of projectile

   public PlayerProjectile(Vector2D position){ // position is the position of the player
        this.position.setX(position.getX() + 11.5);
        this.position.setY(position.getY());
//        this.position = position;
        this.image = new Image(new File("src/main/resources/shot.png").toURI().toString(), width, height, true, true);
    }



    @Override
    public void move() {
        int speed = 5;
        if(active && this.position.getY() > -10){
            speed += 1;
            this.position.setY(this.position.getY() - speed); // moves the projectile up
        }
    }


    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }
}
