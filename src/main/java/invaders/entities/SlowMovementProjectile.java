package invaders.entities;

import invaders.physics.Vector2D;

public class SlowMovementProjectile implements  MovementProjectile{
    public void moveProjectile(boolean active, Vector2D position) throws InterruptedException {
        double speed = 5;
        if(active && position.getY() > -10){
            speed += 1;
            position.setY(position.getY() + speed); // moves the projectile up
        }
    }
}
