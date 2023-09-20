package invaders.entities;

public class EnemyABuilder implements EnemyBuilder{
    private int x;
    private int y;
    private String type;
    private double speed;
    @Override
    public EnemyBuilder setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public EnemyBuilder setSpeedType(String type) {
        this.type = type;
        return this;

    }

    @Override
    public EnemyBuilder setSpeed(Double speed) {
        this.speed = speed;
        return this;

    }

    @Override
    public Enemy getEnemy() {
        return new Enemy(type,x,y,speed);

    }
}
