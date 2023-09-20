package invaders.entities;

public interface BunkerBuilderInterface {
    public void setPosition(int x,int y);
    public void setHealth(int health);
    public void setWidth(int width);
    public void setHeight(int height);
    public Bunker getBunker();
    public void setImage();
}
