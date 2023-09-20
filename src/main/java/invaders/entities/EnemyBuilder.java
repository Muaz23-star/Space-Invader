package invaders.entities;

public interface EnemyBuilder {
   public EnemyBuilder setPosition(int x,int y);
   public EnemyBuilder setSpeedType(String type);
   public EnemyBuilder setSpeed(Double speed);
   public Enemy getEnemy();


}
