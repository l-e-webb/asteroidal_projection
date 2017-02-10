package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.utility.Assets;
import com.louiswebbgames.asteroidalprojection.utility.Log;

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
    public void update(float delta) {
        super.update(delta);
        for (Asteroid asteroid : ((PlayStage)getStage()).getAsteroids()) {
            if (collidesWith(asteroid)) {
                Log.log(LOG_TAG, "Laser colliding with asteroid.");
                asteroid.reportHit(linearVelocity);
                destroy();
                return;
            }
        }
        if (playerLaser) {
            for (Enemy enemy : ((PlayStage)getStage()).getEnemies()) {
                if (collidesWith(enemy)) {
                    Log.log(LOG_TAG, "Player laser colliding with enemy.");
                    enemy.reportHit(linearVelocity);
                    destroy();
                    return;
                }
            }
        } else {
            Player player = ((PlayStage)getStage()).getPlayer();
            if (collidesWith(player)) {
                Log.log(LOG_TAG, "Enemy laser colliding with player.");
                player.reportHit(linearVelocity);
                destroy();
                return;
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
