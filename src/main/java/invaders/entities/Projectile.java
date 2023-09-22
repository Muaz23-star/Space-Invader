package invaders.entities;

import invaders.physics.Vector2D;

public interface Projectile {


   public void move() throws InterruptedException; //moves the projectile
   public Vector2D getPosition(); //gets the position of the projectile
   public double getWidth(); //gets the width of the projectile
   public double getHeight(); //gets the height of the projectile

}
