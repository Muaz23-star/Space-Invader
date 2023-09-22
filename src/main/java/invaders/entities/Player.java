package invaders.entities;
import invaders.physics.GameLoop;



import invaders.physics.Vector2D;

import invaders.rendering.Renderable;

import javafx.scene.image.Image;

import java.io.File;

public class Player implements  Renderable {

    private final Vector2D position;

    private double health = 3;

    private final double width = 25;
    private final double height = 30;
    private final Image image;

    public Player(Vector2D position){
        this.image = new Image(new File("src/main/resources/player.png").toURI().toString(), width, height, true, true);
        this.position = position;
    }



    public double getHealth() {
        return this.health;
    }



    public void setHealth(double health){
        this.health = health;
    }




    public void left() {
        this.position.setX(this.position.getX() - 1);
    }


    public void right() {
        this.position.setX(this.position.getX() + 1);
    }

    public PlayerProjectile shoot(){
        // create a new projectile using Factory pattern

        Projectile projectile =  ProjectileFactory.createProjectile("player",this.position);


        GameLoop gameLoop = new GameLoop(projectile); // Helps the projectile to move smoothly
        gameLoop.start();  // Starts the game loop

        return (PlayerProjectile) projectile;

    }



    public boolean collidesWith(EnemyProjectile projectile) {
        // Enemy's boundaries
        double enemyLeft = this.position.getX();
        double enemyRight = this.position.getX() + this.width;
        double enemyTop = this.position.getY();
        double enemyBottom = this.position.getY() + this.height;

        // PlayerProjectile's boundaries
        double projectileLeft = projectile.getPosition().getX();
        double projectileRight = projectile.getPosition().getX() + projectile.getWidth();
        double projectileTop = projectile.getPosition().getY();
        double projectileBottom = projectile.getPosition().getY() + projectile.getHeight();

        // Check for intersection
        if (enemyLeft < projectileRight &&
                enemyRight > projectileLeft &&
                enemyTop < projectileBottom &&
                enemyBottom > projectileTop) {
            return true;  // Collision occurs
        } else {
            return false;
        }
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }



    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

}
