package invaders.entities;

import invaders.rendering.Renderable;

import java.util.HashSet;
import java.util.Set;
import invaders.engine.GameEngine;
import invaders.engine.GameWindow;
import java.util.*;

public class CollisionDetector {
    private GameEngine model;
    private List<Renderable> renderables;
    private GameWindow view;

    public CollisionDetector(GameEngine model,GameWindow view){
         this.renderables = model.getRenderables();
         this.view = view;
            this.model = model;
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

                            view.markEntityViewForDelete(ro);
                            view.markEntityViewForDelete(ro2);
                            // increase speed
                            model.SetEnemySpeed(model.getEnemySpeed() + 0.25);



                        }
                    }else if (ro2 instanceof Bunker) {  // if the enemy collides with the bunker then remove bunker
                        Bunker bunker = (Bunker) ro2;
                        if (enemy.collidesWithBunker(bunker)) {

                            toBeRemoved.add(ro2);
                            view.markEntityViewForDelete(ro2); // remove the bunker

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

                                view.markEntityViewForDelete(ro);
                                view.markEntityViewForDelete(ro2);
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
                                view.markEntityViewForDelete(ro);
                            }
                            toBeRemoved.add(ro2); // always remove the projectile after a hit
                            view.markEntityViewForDelete(ro2);
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


    public void removeProjectiles() {
        List<Renderable> toBeRemoved = new ArrayList<>();

        for (Renderable projectile : renderables) {
            if (projectile instanceof  EnemyProjectile){
                EnemyProjectile enemyProjectile = (EnemyProjectile) projectile;
                for(Renderable projectile2 : renderables){
                    if(projectile instanceof PlayerProjectile){
                        PlayerProjectile playerProjectile = (PlayerProjectile) projectile2;
                        if(enemyProjectile.collidesWith(playerProjectile)){
                            toBeRemoved.add(projectile);
                            toBeRemoved.add(projectile2);
                            view.markEntityViewForDelete(projectile);
                            view.markEntityViewForDelete(projectile2);
                        }
                    }
                }
            }



            if (projectile instanceof EnemyProjectile && projectile.getPosition().getY() > model.getWindowHeight() - 13) {
                toBeRemoved.add(projectile);
                view.markEntityViewForDelete(projectile);
                model.setEnemyProjectiles(model.getEnemyProjectiles() - 1);
            } else if (projectile instanceof PlayerProjectile && projectile.getPosition().getY() < 30) {
                toBeRemoved.add(projectile);
                view.markEntityViewForDelete(projectile);
            }
        }

        renderables.removeAll(toBeRemoved);
    }

}
