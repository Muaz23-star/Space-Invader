package invaders.entities;

import invaders.physics.Vector2D;

public interface Projectile {

//   public boolean collidesWith(invaders.physics.Vector2D enemy); //checks if collison occurs

//   public void onCollision(invaders.logic.Damagable other); //what happens when collision occurs

   public void move() throws InterruptedException; //moves the projectile
   public Vector2D getPosition(); //gets the position of the projectile
   public double getWidth(); //gets the width of the projectile
   public double getHeight(); //gets the height of the projectile

}
