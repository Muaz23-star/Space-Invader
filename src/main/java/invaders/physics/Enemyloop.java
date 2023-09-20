package invaders.physics;
import invaders.engine.GameEngine;
import javafx.animation.AnimationTimer;
import invaders.entities.Enemy;


public class Enemyloop extends AnimationTimer {
    private Enemy enemy;

    public Enemyloop(Enemy enemy) {
        this.enemy = enemy;
    }

    @Override
    public void handle(long now) {
        // Update the position of the projectile in each frame
        enemy.move();
    }

}
