package invaders.entities;

public class BunkerABuilder implements BunkerBuilderInterface {
    private Bunker bunker;

    public BunkerABuilder(){
        bunker = new Bunker();
    }



    @Override
    public void setPosition(int x, int y) {
        bunker.setPosition(x,y);
    }

    @Override
    public void setHealth(int health) {
        bunker.setHealth(health);
    }

    @Override
    public void setWidth(int width) {
        bunker.setWidth(width);

    }

    @Override
    public void setHeight(int height) {
       bunker.setHeight(height);
    }
    @Override
    public void setImage() {
        bunker.setImage();
    }


    @Override
    public Bunker getBunker() {
        return bunker;
    }




}
