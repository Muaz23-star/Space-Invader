package invaders.entities;

public interface BunkerInterface {
    public void setColour();
    public int getHealth(); // returns the health of the bunker
    public boolean collidesWith(Projectile projectile); // checks if collision occurs
    public void setHealth(int health); // sets the health of the bunker

}
