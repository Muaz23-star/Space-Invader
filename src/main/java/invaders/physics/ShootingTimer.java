package invaders.physics;

import invaders.engine.GameEngine;
import javafx.animation.AnimationTimer;

public class ShootingTimer extends AnimationTimer {

    private long lastUpdate = 0;
    private long minDelayNanos = 1000000000;  // 1 second
    private long maxDelayNanos = 3000000000L;  // 3 seconds
    private long currentDelayNanos;
    private GameEngine gameEngine;  // The class containing randomEnemyShoot()

    public ShootingTimer(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        setRandomDelay();
    }

    private void setRandomDelay() {
        currentDelayNanos = minDelayNanos + (long)(Math.random() * (maxDelayNanos - minDelayNanos));
    }

    @Override
    public void handle(long now) {
        if (now - lastUpdate >= currentDelayNanos) {
            gameEngine.randomEnemyShoot();
            lastUpdate = now;
            setRandomDelay();  // Set a new random delay for next shot
        }
    }
}


