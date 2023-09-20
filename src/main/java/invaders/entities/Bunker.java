package invaders.entities;

import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

import java.io.File;

public class Bunker implements BunkerInterface, Renderable {

    private Vector2D position = null;
    private double width;
    private double height;
    private int health = 3;
    private Image image;
    private String colour = "green";

    public Bunker(int x, int y, double width, double height){
        this.position = new Vector2D(x,y);
        this.width = width;
        this.height = height;
        this.image = new Image(new File("src/main/resources/bunker.png").toURI().toString(), this.width, this.height, false, true);
    }


    @Override
    public void setColour() {  //State pattern
        ColourBunker colourBunker = null;
        if (health == 3) {
            colourBunker = new GreenBunker(width, height);
            colourBunker.SetColour(this);
        }else if (health == 2) {
            colourBunker = new YellowBunker(width, height);
            colourBunker.SetColour(this);

        }else if (health == 1){
            colourBunker = new RedBunker(width, height);
            colourBunker.SetColour(this);
        }
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public boolean collidesWith(Projectile projectile) {  //everytime it collides we do health - 1 and change colour
        // Enemy's boundaries
        double bunkerLeft = this.position.getX();
        double bunkerRight = this.position.getX() + this.width;
        double bunkerTop = this.position.getY();
        double bunkerBottom = this.position.getY() + this.height;

        // PlayerProjectile's boundaries
        double projectileLeft = projectile.getPosition().getX();
        double projectileRight = projectile.getPosition().getX() + projectile.getWidth();
        double projectileTop = projectile.getPosition().getY();
        double projectileBottom = projectile.getPosition().getY() + projectile.getHeight();

        // Check for intersection
        if (bunkerLeft < projectileRight &&
                bunkerRight > projectileLeft &&
                bunkerTop < projectileBottom &&
                bunkerBottom > projectileTop) {
            return true;  // Collision occurs
        } else {
            return false;
        }
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
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
        return Renderable.Layer.FOREGROUND;
    }
}
