package invaders.engine;
import java.util.*;

import java.util.stream.Collectors;

import invaders.entities.*;
import javafx.animation.AnimationTimer;
import javafx.util.Duration;

import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import invaders.physics.ShootingTimer;

public class GameWindow {
	private final int width;
    private final int height;
	private Scene scene;
    private Pane pane;
    private GameEngine model;
    private List<EntityView> entityViews;
    private Renderable background;

    private double xViewportOffset = 0.0;
    private double yViewportOffset = 0.0;
    private static final double VIEWPORT_MARGIN = 280.0;
    private List<Renderable> renderables;
    private ShootingTimer timer;

	public GameWindow(GameEngine model, int width, int height){
		this.width = width;
        this.height = height;
        this.model = model;
        pane = new Pane();
        scene = new Scene(pane, width, height);
        this.background = new SpaceBackground(model, pane);

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(this.model);

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

        entityViews = new ArrayList<EntityView>();
        this.renderables = model.getRenderables();
        this.timer = new ShootingTimer(this.model);



    }

	public void run() {
         Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17), t -> this.draw()));

         timeline.setCycleCount(Timeline.INDEFINITE);
         timeline.play();


    }

    private void draw(){
        model.update();
        CollisionEnemy();
        CollisionBunker();
        removeProjectiles();
        CollisionPlayer();
        this.timer.start();
//        model.randomEnemyShoot();





        if (renderables.isEmpty()) {
            System.out.println("renderables is null");
            System.exit(0);
        }
        for (Renderable entity : renderables) {
            boolean notFound = true;
            for (EntityView view : entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update(xViewportOffset, yViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }


        for (EntityView entityView : entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }
        entityViews.removeIf(EntityView::isMarkedForDelete);
    }

	public Scene getScene() {
        return scene;
    }


    public void CollisionPlayer(){
        Set<Renderable> toBeRemoved = new HashSet<>();

        for (Renderable ro : renderables) {
            if (ro instanceof Player) {
                Player player = (Player) ro;

                for (Renderable ro2 : renderables) {
                    if (ro2 instanceof EnemyProjectile) {
                        EnemyProjectile projectile = (EnemyProjectile) ro2;
                        if (player.collidesWith(projectile)) {
                            if(player.getHealth() > 0){
                                player.setHealth(player.getHealth() - 1);
                                player.getPosition().setX(model.getPlayerInitialX()); //Respawn the player at the center or the initial position

                            } else if (player.getHealth() == 0){
                                toBeRemoved.add(ro); // do pane remove stuff // we need to remove the enemy and the projectile
                                toBeRemoved.add(ro2); // ro2 is the player projectile

                                markEntityViewForDelete(ro);
                                markEntityViewForDelete(ro2);
                                System.out.println("Game is over");
                                System.exit(0);



                            }
                        }
                    }
                }
            }
        }

        // Now, remove the collided objects
        renderables.removeAll(toBeRemoved);

    }

    public void CollisionEnemy(){
        Set<Renderable> toBeRemoved = new HashSet<>();

        for (Renderable ro : renderables) {
            if (ro instanceof Enemy) {
                Enemy enemy = (Enemy) ro;



                for (Renderable ro2 : renderables) {
                    if (ro2 instanceof PlayerProjectile) {  // if the enemy collides with the player projectile
                        PlayerProjectile projectile = (PlayerProjectile) ro2;
                        if (enemy.collidesWith(projectile)) {
                            toBeRemoved.add(ro); // do pane remove stuff // we need to remove the enemy and the projectile
                            toBeRemoved.add(ro2); // ro2 is the player projectile

                            markEntityViewForDelete(ro);
                            markEntityViewForDelete(ro2);
                            // increase speed
                            model.SetEnemySpeed(model.getEnemySpeed() + 0.25);



                        }
                    }else if (ro2 instanceof Bunker) {  // if the enemy collides with the bunker then remove bunker
                        Bunker bunker = (Bunker) ro2;
                        if (enemy.collidesWithBunker(bunker)) {

                            toBeRemoved.add(ro2);
                            markEntityViewForDelete(ro2); // remove the bunker

                        }
                    } else if (ro2 instanceof Player) {
                        Player player = (Player) ro2;
                        if (enemy.collidesWithPlayer(player)) {
                            System.out.println("Game is over!!");
                            System.exit(0);
                        }

                    }
                }
            }
        }

        // Now, remove the collided objects
        renderables.removeAll(toBeRemoved);
    }


    private void processProjectileCollisionWithBunker(Class<? extends Projectile> projectileClass) {
        Set<Renderable> toBeRemoved = new HashSet<>();

        for (Renderable ro : renderables) {
            if (ro instanceof Bunker) {
                Bunker bunker = (Bunker) ro;

                for (Renderable ro2 : renderables) {
                    if (projectileClass.isInstance(ro2)) {
                        Projectile projectile = (Projectile) ro2;
                        if (bunker.collidesWith(projectile)) {
                            if (bunker.getHealth() > 0) {
                                bunker.setHealth(bunker.getHealth() - 1);
                                bunker.updateColourState();
                            }
                            if (bunker.getHealth() == 0) {
                                toBeRemoved.add(ro); // remove the bunker when its health is 0
                                bunker.setHealth(bunker.getHealth() - 1);
                                markEntityViewForDelete(ro);
                            }
                            toBeRemoved.add(ro2); // always remove the projectile after a hit
                            markEntityViewForDelete(ro2);
                            model.setEnemyProjectiles(model.getEnemyProjectiles() - 1);
                        }
                    }
                }
            }
        }
        renderables.removeAll(toBeRemoved);
    }

    public void CollisionBunker() {
        processProjectileCollisionWithBunker(PlayerProjectile.class);
        processProjectileCollisionWithBunker(EnemyProjectile.class); // Assuming you have an EnemyProjectile class
    }



    private void markEntityViewForDelete(Renderable entity) {
        for (EntityView view : entityViews) {
            if (view.matchesEntity(entity)) {
                view.markForDelete();
                break;
            }
        }
    }

    public void removeProjectiles() {
        List<Renderable> toBeRemoved = new ArrayList<>();

        for (Renderable projectile : renderables) {
            if (projectile instanceof EnemyProjectile && projectile.getPosition().getY() > model.getWindowHeight() - 13) {
                toBeRemoved.add(projectile);
                markEntityViewForDelete(projectile);
                model.setEnemyProjectiles(model.getEnemyProjectiles() - 1);
            } else if (projectile instanceof PlayerProjectile && projectile.getPosition().getY() < 30) {
                toBeRemoved.add(projectile);
                markEntityViewForDelete(projectile);
            }
        }

        renderables.removeAll(toBeRemoved);
    }


}
