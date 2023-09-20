package invaders.entities;
import invaders.physics.Vector2D;

public class FastMovementProjectile implements MovementProjectile{
    @Override
    public void moveProjectile(boolean active, Vector2D position) throws InterruptedException {
        int speed = 10;
        if(active && position.getY() > -10){
            speed += 1;
            position.setY(position.getY() + speed); // moves the projectile up
        }
    }
}
