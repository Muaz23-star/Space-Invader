package invaders.entities;

import invaders.physics.Vector2D;

public class CollidePlayerProjectile {




    public  static boolean collidesWith(PlayerProjectile projectile, Vector2D position,double width, double height) {
        // Enemy's boundaries
        double enemyLeft = position.getX();
        double enemyRight = position.getX() + width;
        double enemyTop = position.getY();
        double enemyBottom = position.getY() + height;

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

}
