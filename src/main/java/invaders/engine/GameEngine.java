package invaders.engine;

import invaders.GameObject;
import invaders.entities.*;
import invaders.entities.EnemyABuilder;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import invaders.physics.Enemyloop;
import invaders.entities.Bunker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;


/**
 * This class manages the main loop and logic of the game
 */
public class GameEngine {


	private List<Renderable> renderables;
	private final Player player;

	private boolean left;
	private boolean right;
	private Direction enemyDirection = Direction.RIGHT;
	private double enemySpeed = 0.5;

	private int  Window_width;
	private int  Window_height;

	private int player_X;
	private int player_Y;

	private int player_lives;
	private String player_colour;
	private int player_speed;

	private boolean isGameOver = false;



    public static enum Direction {
		LEFT,
		RIGHT
	}
	private int  enemyProjectiles = 0;

	public GameEngine(String config){

		renderables = new ArrayList<Renderable>();
		EnemyABuilder enemyABuilder = new EnemyABuilder();



		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(new FileReader("src/main/resources/config.json"));
			JSONObject jsonObject = (JSONObject) obj;


			JSONObject player = (JSONObject) jsonObject.get("Player");  // player info
			this.player_lives = ((Number) player.get("lives")).intValue();
			this.player_colour = (String) player.get("colour");
			this.player_speed = ((Number) player.get("speed")).intValue();



			JSONObject playerPosition = (JSONObject) player.get("position"); // player position
			this.player_X = ((Number) playerPosition.get("x")).intValue();
			this.player_Y = ((Number) playerPosition.get("y")).intValue();


			// getting game window size
			JSONObject game = (JSONObject) jsonObject.get("Game");
			JSONObject windowSize = (JSONObject) game.get("size");
			this.Window_width = ((Number) windowSize.get("x")).intValue(); // Game window size
			this.Window_height = ((Number) windowSize.get("y")).intValue();

			// Looping through Bunkers
			JSONArray bunkers = (JSONArray) jsonObject.get("Bunkers");
			Iterator<?> bunkerIterator = bunkers.iterator();
			while (bunkerIterator.hasNext()) {
				JSONObject bunker = (JSONObject) bunkerIterator.next();
				JSONObject bunkerPosition = (JSONObject) bunker.get("position");
				JSONObject bunkerSize = (JSONObject) bunker.get("size");
				int width = ((Number) bunkerSize.get("x")).intValue();
				int height = ((Number) bunkerSize.get("y")).intValue();

				int x = ((Number) bunkerPosition.get("x")).intValue();
				int y = ((Number) bunkerPosition.get("y")).intValue();

				BunkerABuilder bunkerABuilder = new BunkerABuilder();
				BunkerDirector bunkerDirector = new BunkerDirector(bunkerABuilder);

				bunkerDirector.constructBunker(x,y,3,width,height); //creates new bunker using the builder pattern
				Bunker newBunker = bunkerDirector.getBunker();
				this.renderables.add(newBunker);
			}

			// Looping through Enemies
			JSONArray enemies = (JSONArray) jsonObject.get("Enemies");
			Iterator<?> enemyIterator = enemies.iterator();
			while (enemyIterator.hasNext()) {
				JSONObject enemy = (JSONObject) enemyIterator.next();
				JSONObject enemyPosition = (JSONObject) enemy.get("position");
				int x = ((Number) enemyPosition.get("x")).intValue();
				int y = ((Number) enemyPosition.get("y")).intValue();

				//makes new enemy using the builder pattern
				Enemy newEnemy = enemyABuilder.setSpeedType((String) enemy.get("projectile")).setPosition(x,y).setSpeed(0.5).getEnemy() ; // Example speed type



				Enemyloop enemyloop = new Enemyloop(newEnemy);
				enemyloop.start();
				// (String) enemy.get("projectile")
//				//adds enemy to list of game objects
				this.renderables.add(newEnemy);


			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}


		player = new Player(new Vector2D(player_X, player_Y)); //creates player
		player.setHealth(this.player_lives -1 ); //sets player health
		player.setColour(player_colour.toLowerCase()); //sets player colour


		renderables.add(player); //adds player to list of renderables
        for (Renderable ro : renderables) {
			if (ro instanceof Player) {
				System.out.println("Player found");
			}
		}
	}

	/**
	 * Updates the game/simulation
	 */
	public void update(){
		movePlayer();
		updateEnemies();
		//Collision();


		// ensure that renderable foreground objects don't go off-screen
		for(Renderable ro: renderables){
			if(!ro.getLayer().equals(Renderable.Layer.FOREGROUND)){
				continue;
			}
			if(ro.getPosition().getX() + ro.getWidth() >= getWindowWidth()) {
				ro.getPosition().setX(getWindowWidth() -1 -ro.getWidth());
			}

			if(ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(1);
			}

			if(ro.getPosition().getY() + ro.getHeight() >= getWindowHeight()) {
				ro.getPosition().setY((getWindowHeight() - 1)-ro.getHeight());
			}

			if(ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(1);
			}




		}
	}
	private void updateEnemies() {
		Enemy leftmost = null, rightmost = null;

		// Move all enemies and find leftmost & rightmost enemies.
		for (Renderable renderable : renderables) {
			if (renderable instanceof Enemy enemy) {
				enemy.SetSpeed(enemySpeed);

				enemy.setDirection(enemyDirection);
				enemy.move();

				if (leftmost == null || enemy.getPosition().getX() < leftmost.getPosition().getX()) { //finds the leftmost enemy
					leftmost = enemy;
				}

				if (rightmost == null || enemy.getPosition().getX() > rightmost.getPosition().getX()) { //finds the rightmost enemy
					rightmost = enemy;
				}
			}

		}


		if (rightmost == null || leftmost == null) {
			isGameOver = true;
			return;
		};

		// Check for boundary collision with edge enemies.
		if ((enemyDirection == Direction.RIGHT && rightmost.getPosition().getX() + rightmost.getWidth() >= getWindowWidth()) ||
				(enemyDirection == Direction.LEFT && leftmost.getPosition().getX() <= 0)) {

			enemyDirection = (enemyDirection == Direction.RIGHT) ? Direction.LEFT : Direction.RIGHT; // if else statement to change direction

			// Move all enemies down a step if desired.
			for (Renderable renderable : renderables) {
				if (renderable instanceof Enemy) {
					Enemy enemy = (Enemy) renderable;
					enemy.moveDown();
				}
			}
		}
	}
	public void SetEnemySpeed(double speed){
		this.enemySpeed = speed;
	}

    public double getEnemySpeed() {
		return this.enemySpeed;
	}


	public List<Renderable> getRenderables(){
		return renderables;
	}


	public void leftReleased() {
		this.left = false;
	}

	public void rightReleased(){
		this.right = false;
	}

	public void leftPressed() {
		this.left = true;
	}
	public void rightPressed(){
		this.right = true;
	}

	public boolean shootPressed(){
		//check if there is already a PlayerProjectile on the screen
		List<Renderable> projectiles = renderables.stream()
				.filter(r -> r instanceof PlayerProjectile)
				.collect(Collectors.toList());
		if(projectiles.size() > 0){
			return false;
		}
		renderables.add(player.shoot());
		return true;
	}

	public void EnemyShot(Enemy enemy){

		renderables.add(enemy.shoot());
		enemyProjectiles++;
	}

	public int getEnemyProjectiles() {
		return enemyProjectiles;
	}
	public void setEnemyProjectiles(int enemyProjectiles) {
		this.enemyProjectiles = enemyProjectiles;
	}

	private void movePlayer(){
		if(left){
			player.left(player_speed);
		}

		if(right){
			player.right(player_speed);
		}
	}

	public void randomEnemyShoot() {
		List<Renderable> enemies = renderables.stream()
				.filter(r -> r instanceof Enemy)
				.collect(Collectors.toList());

		if (enemies.isEmpty()) return;  // No enemies to shoot.

		Random random = new Random();
		Enemy randomEnemy = (Enemy) enemies.get(random.nextInt(enemies.size()));
        System.out.println("Came here but " + getEnemyProjectiles()  );
		if (getEnemyProjectiles() < 3) {
			System.out.println("Enemy shot");
			EnemyShot(randomEnemy);
		}
	}

	public int getWindowWidth() {
		return this.Window_width;
	}
	public int getWindowHeight() {
		return this.Window_height;
	}

	public int getPlayerInitialX(){
		return player_X;
	}

	public void checkRenderables(){
//		if(renderables.isEmpty()){
//			System.out.println("Game over!");
//			System.exit(0);
//		}

		// check if there is no enemy then exit
		List<Renderable> enemies = renderables.stream()
				.filter(r -> r instanceof Enemy)
				.collect(Collectors.toList());
		if(enemies.isEmpty()){
			isGameOver = true;
			return;

		}
	}


	public boolean checkGameOver() {
		return isGameOver;
	}
	public void setGameOver() { // used in collision deterctor
		isGameOver = true;
	}
}
