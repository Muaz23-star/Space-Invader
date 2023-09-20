package invaders.entities;

import invaders.physics.Vector2D;

public class ProjectileFactory {

    public static Projectile createProjectile(String type, Vector2D position) {
        return createProjectile(type, position, "");
    }

    public static Projectile createProjectile(String type, Vector2D position, String speedType) {
        if (type.equals("player")) {
            return new PlayerProjectile(position);
        } else if (type.equals("enemy")) {
            return new EnemyProjectile(position, speedType);

        } else {
            throw new IllegalArgumentException("Invalid projectile type");
        }
    }
}

