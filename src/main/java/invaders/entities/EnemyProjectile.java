package invaders.entities;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;


import java.io.File;

public class EnemyProjectile implements Projectile, Renderable  {
    private  Vector2D position = new Vector2D(0,0);
    private final Image image;
    private final double width = 10;
    private final double height = 10;
    private boolean active = true; // life of projectile
    private final String Speedtype;
    private double speed;
    private MovementProjectile movement_type;



    public EnemyProjectile(Vector2D position, String Speedtype){ // position is the position of the player
        this.position.setX(position.getX() + 11.5);
        this.position.setY(position.getY());
        this.image = new Image(new File("src/main/resources/shot.png").toURI().toString(), width, height, true, true);
        this.Speedtype = Speedtype;
        if (Speedtype.equals("slow_straight")) {
            movement_type = new SlowMovementProjectile();
        }else {
            movement_type = new FastMovementProjectile();
        }
    }



    @Override
    public void move() throws InterruptedException {
        movement_type.moveProjectile(active, position); //Strategy pattern
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