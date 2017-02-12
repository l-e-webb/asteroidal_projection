package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.utility.Assets;
import com.louiswebbgames.asteroidalprojection.utility.Log;

import java.util.Iterator;

public class Laser extends Projectile {

    public static final String LOG_TAG = Laser.class.getSimpleName();

    boolean playerLaser;

    public Laser(float x, float y, Vector2 velocity, boolean playerLaser) {
        super(x, y, GameplayConstants.LASER_WIDTH, GameplayConstants.LASER_HEIGHT);
        this.linearVelocity = velocity;
        setOrientation(velocity.angleRad());
        this.playerLaser = playerLaser;
    }

    @Override
    public void destroy() {
        ((PlayStage)getStage()).removeObject(this);
        super.destroy();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        PlayStage stage = (PlayStage) getStage();
        for (Iterator<Asteroid> iterator = stage.getAsteroids().iterator(); iterator.hasNext(); ) {
            Asteroid asteroid = iterator.next();
            if (collidesWith(asteroid)) {
                Log.log(LOG_TAG, "Laser colliding with asteroid at " + getPosition().toString());
                if (asteroid.reportHit(linearVelocity)) {
                    destroy();
                    return;
                }
            }
        }
        if (playerLaser) {
            for (Iterator<Enemy> iterator = stage.getEnemies().iterator(); iterator.hasNext(); ) {
                Enemy enemy = iterator.next();
                if (collidesWith(enemy)) {
                    Log.log(LOG_TAG, "Player laser colliding with enemy at " + getPosition().toString());
                    if (enemy.reportHit(linearVelocity)) {
                        destroy();
                        iterator.remove();
                        return;
                    }
                }
            }
        } else {
            Player player = ((PlayStage)getStage()).getPlayer();
            if (collidesWith(player)) {
                Log.log(LOG_TAG, "Enemy laser colliding with player at " + getPosition().toString());
                if (player.reportHit(linearVelocity)) {
                    destroy();
                }
            }
        }
    }

    @Override
    public TextureRegion getTexture() {
        return playerLaser ? Assets.instance.playerLaser : Assets.instance.enemyLaser;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Laser(getX(), getY(), linearVelocity, playerLaser);
    }
}
