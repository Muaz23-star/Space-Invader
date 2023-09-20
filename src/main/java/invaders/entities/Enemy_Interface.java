package invaders.entities;

public interface Enemy_Interface {
    public EnemyProjectile shoot(); // shoots a projectile at the player
    public void move(); // moves the enemy
    public boolean collidesWith(PlayerProjectile projectile); // checks if collision occurs
    public int getPoints(); // returns the points of the enemy
    public boolean collidesWithPlayer(Player player); // checks if the enemy is near the player
    public void setPosition(double x, double y); // sets the position of the enemy
}
