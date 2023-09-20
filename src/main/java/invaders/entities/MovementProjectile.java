package invaders.entities;

import invaders.physics.Vector2D;

public interface MovementProjectile {
    public void moveProjectile(boolean active, Vector2D position) throws InterruptedException; //moves the projectile
}
