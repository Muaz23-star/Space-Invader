package invaders.entities;

import invaders.engine.GameEngine;
import invaders.physics.GameLoop;
import invaders.physics.Vector2D;
import invaders.rendering.Animator;
import invaders.rendering.Renderable;

import javafx.scene.image.Image;

import java.io.File;

public class Enemy implements Enemy_Interface,Renderable {
    private  Vector2D position = null;

    private boolean alive = true;
    private final double width = 25;
    private final double height = 30;
    private final Image image;
    private final int points = 10;
    private final String Speedtype;
    private GameEngine.Direction direction;
    private boolean remove = false;
    private double speed = 0.5;



    public Enemy(String Speedtype, int x, int y,double speed){
        this.image = new Image(new File("src/main/resources/enemy.png").toURI().toString(), width, height, true, true);
        this.Speedtype = Speedtype;
        this.position = new Vector2D(x,y);
        this.speed = speed;
    }




    public EnemyProjectile shoot(){
        // create a new projectile

        Projectile projectile = ProjectileFactory.createProjectile("enemy",this.position,this.Speedtype); //Projectile created using Factory pattern


        GameLoop gameLoop = new GameLoop(projectile); // Replace 'playerProjectile' with your actual projectile instance
        gameLoop.start();

        return (EnemyProjectile) projectile;

    }

    @Override
    public void move() {

        if(alive && direction == GameEngine.Direction.LEFT){
            this.position.setX(this.position.getX() - speed); // moves the enemy left
        }else{
            this.position.setX(this.position.getX() + speed); // moved the enemy right
        }
    }

    @Override
    public boolean collidesWith(PlayerProjectile projectile) {
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

    public boolean collidesWithBunker(Bunker bunker){
        // Enemy's boundaries
        double enemyLeft = this.position.getX();
        double enemyRight = this.position.getX() + this.width;
        double enemyTop = this.position.getY();
        double enemyBottom = this.position.getY() + this.height;

        // Bunker's boundaries
        double bunkerLeft = bunker.getPosition().getX();
        double bunkerRight = bunker.getPosition().getX() + bunker.getWidth();
        double bunkerTop = bunker.getPosition().getY();
        double bunkerBottom = bunker.getPosition().getY() + bunker.getHeight();

        // Check for intersection
        if (enemyLeft < bunkerRight &&
                enemyRight > bunkerLeft &&
                enemyTop < bunkerBottom &&
                enemyBottom > bunkerTop) {
            return true;  // Collision occurs
        } else {
            return false;
        }
    }

    @Override
    public int getPoints() {
        return 0;
    }

    @Override
    public boolean collidesWithPlayer(Player player) {
        // Enemy's boundaries
        double enemyLeft = this.position.getX();
        double enemyRight = this.position.getX() + this.width;
        double enemyTop = this.position.getY();
        double enemyBottom = this.position.getY() + this.height;

        // Player's boundaries
        double playerLeft = player.getPosition().getX();
        double playerRight = player.getPosition().getX() + player.getWidth();
        double playerTop = player.getPosition().getY();
        double playerBottom = player.getPosition().getY() + player.getHeight();

        // Check for intersection
        if (enemyLeft < playerRight &&
                enemyRight > playerLeft &&
                enemyTop < playerBottom &&
                enemyBottom > playerTop) {
            return true;  // Collision occurs
        } else {
            return false;
        }
    }


    @Override
    public void setPosition(double x, double y) {
        this.position.setX(x);
        this.position.setY(y);
    }

    @Override
    public Image getImage() {
        return this.image;
    }
   @Override
    public double getWidth(){
        return this.width;
   };
   @Override
    public double getHeight(){
        return this.height;
    };

    @Override
    public Vector2D getPosition(){
        return this.position;
    };
    @Override
    public Renderable.Layer getLayer(){
        if (remove){
            return Renderable.Layer.BACKGROUND;
        }else{
            return Renderable.Layer.FOREGROUND;
        }

    };





    public void moveDown() {
        int downStep = 10;  // or whatever value you desire
        this.position.setY(this.position.getY() + downStep);
    }
    public void setDirection (GameEngine.Direction direction){
        this.direction = direction;
    }

    public void SetSpeed(Double speed){
        this.speed = speed;
    }

}
