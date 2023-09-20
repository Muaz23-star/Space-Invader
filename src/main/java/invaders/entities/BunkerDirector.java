package invaders.entities;

public class BunkerDirector {
    private BunkerBuilderInterface builder;

    public BunkerDirector(BunkerBuilderInterface builder){
        this.builder = builder;
    }

    public void constructBunker(int x, int y, int health, int width, int height){
        builder.setPosition(x,y);
        builder.setHealth(health);
        builder.setWidth(width);
        builder.setHeight(height);
        builder.setImage();
    }

    public Bunker getBunker(){
        return builder.getBunker();
    }
}
