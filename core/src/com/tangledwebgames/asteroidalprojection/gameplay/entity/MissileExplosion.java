package com.tangledwebgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.GameplayConstants;
import com.tangledwebgames.asteroidalprojection.gameplay.PlayStage;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MissileExplosion extends Explosion {

    protected boolean appliedDamage;

    public MissileExplosion(float x, float y) {
        super(x, y, GameplayConstants.MISSILE_EXPLOSION_RADIUS);
        appliedDamage = false;
        onlyUpdatePositionOnRender = false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!appliedDamage) {
            PlayStage stage = (PlayStage) getStage();
            float radius2 = GameplayConstants.MISSILE_EXPLOSION_RADIUS * GameplayConstants.MISSILE_EXPLOSION_RADIUS;
            Set<Asteroid> affectedAsteroids = new HashSet<>();
            for (Asteroid asteroid : stage.getAsteroids()) {
                if (distance2(asteroid) < radius2) {
                    affectedAsteroids.add(asteroid);
                }
            }
            //Since asteroids spawn more asteroids when they die, we need to call reportHit outside
            //of the initial search loop.
            for (Asteroid asteroid : affectedAsteroids) {
                asteroid.reportHit(new Vector2(asteroid.getPosition()).sub(getPosition()));
            }
            for (Iterator<Enemy> enemies = stage.getEnemies().iterator(); enemies.hasNext(); ) {
                SteerableObject enemy = enemies.next();
                if (distance2(enemy) < radius2) {
                    if (enemy instanceof EnemyCruiser) {
                        EnemyCruiser cruiser = (EnemyCruiser) enemy;
                        cruiser.takeDamage(GameplayConstants.MISSILE_DAMAGE);
                        if (cruiser.getHealth() <= 0) {
                            cruiser.destroy(false);
                            enemies.remove();
                        }
                    } else if (enemy.reportHit(new Vector2())) {
                        enemies.remove();
                    }
                }
            }
            SteerableObject player = stage.getPlayer();
            if (distance2(player) < radius2) {
                player.reportHit(new Vector2(player.getPosition()).sub(getPosition()));
            }
            appliedDamage = true;
        }
    }
}
