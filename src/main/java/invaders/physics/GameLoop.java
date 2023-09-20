package invaders.physics;
import javafx.animation.AnimationTimer;
import invaders.entities.Projectile;


public class GameLoop extends AnimationTimer {
    private Projectile projectile;

    public GameLoop(Projectile projectile) {
        this.projectile = projectile;
    }

    @Override
    public void handle(long now) {
        // Update the position of the projectile in each frame
        try {
            projectile.move();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
