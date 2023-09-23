package invaders.engine;
import java.io.File;
import java.util.*;

import java.util.stream.Collectors;

import invaders.entities.*;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import invaders.physics.ShootingTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


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
    private CollisionDetector collisionDetector;
    private Image gameover;
    private ImageView gameOverView;
    private boolean isGameOverDisplayed = false;


    public GameWindow(GameEngine model, int width, int height){

        this.width = width;
        this.height = height;
        this.model = model;
        pane = new Pane();
        scene = new Scene(pane, width, height);
        this.background = new SpaceBackground(model, pane);
        collisionDetector = new CollisionDetector(model, this);


        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(this.model);

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

        entityViews = new ArrayList<EntityView>();
        this.renderables = model.getRenderables();
        this.timer = new ShootingTimer(this.model);
        gameover = new Image(new File("src/main/resources/gameover.png").toURI().toString(),model.getWindowWidth(), model.getWindowHeight(), false, true);
        gameOverView = new ImageView(gameover);

    }

	public void run() {
         Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17), t -> this.draw()));

         timeline.setCycleCount(Timeline.INDEFINITE);
         timeline.play();


    }

    private void draw(){
        model.update();
        model.checkRenderables();
        collisionDetector.CollisionEnemy();
        collisionDetector.CollisionBunker();
        collisionDetector.CollisionPlayer();
        collisionDetector.removeProjectiles();
        gameOver();


        this.timer.start();






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


    public void markEntityViewForDelete(Renderable entity) {
        for (EntityView view : entityViews) {
            if (view.matchesEntity(entity)) {
                view.markForDelete();
                break;
            }
        }
    }

    public void gameOver(){
        if (model.checkGameOver() && !isGameOverDisplayed){
            pane.getChildren().removeAll();
            pane.getChildren().add(gameOverView);
            isGameOverDisplayed = true;
        }else{
            return;
        }
    }



}
