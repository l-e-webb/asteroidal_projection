package com.tangledwebgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.GameplayConstants;
import com.tangledwebgames.asteroidalprojection.gameplay.PlayStage;
import com.tangledwebgames.asteroidalprojection.utility.Assets;
import com.tangledwebgames.asteroidalprojection.utility.Constants;
import com.tangledwebgames.asteroidalprojection.utility.Log;
import com.tangledwebgames.asteroidalprojection.utility.SoundManager;

import java.util.Iterator;

public class Asteroid extends SteerableObject {

    public static final String LOG_TAG = Asteroid.class.getSimpleName();

    public Asteroid(float x, float y, float radius, Vector2 velocity) {
        super(x, y, radius, EntityType.ASTEROID, CollisionType.CIRCLE);
        independentFacing = true;
        linearVelocity = velocity;
        setMaxLinearAcceleration(0);
        angularVelocity = MathUtils.random(
                GameplayConstants.ASTEROID_MIN_ANGULAR_VEL,
                GameplayConstants.ASTEROID_MAX_ANGULAR_VEL
        );
        setTexture(getRandomAsteroidTexture());
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        PlayStage stage = (PlayStage) getStage();
        for (Iterator<Enemy> iterator = stage.getEnemies().iterator(); iterator.hasNext(); ) {
            Enemy enemy = iterator.next();
            if (collidesWith(enemy)) {
                Log.log(LOG_TAG, "Asteroid colliding with enemy at " + getPosition().toString());
                if (!(enemy instanceof EnemyCruiser && getRadius() < GameplayConstants.ASTEROID_RADIUS_DAMAGE_CUTOFF)
                        && enemy.reportHit(new Vector2(enemy.getPosition()).sub(getPosition()))) {
                    iterator.remove();
                }
                reportHit(new Vector2(getPosition()).sub(enemy.getPosition()));
                return;
            }
        }
        Player player = stage.getPlayer();
        if (player.getState() != Player.PlayerState.BLINKING && collidesWith(player)) {
            Log.log(LOG_TAG, "Asteroid colliding with player at " + getPosition().toString());
            player.reportHit(new Vector2(player.getPosition()).sub(getPosition()));
            //Uncomment to report hits on asteroid when player hits asteroid.
            //reportHit(new Vector2(getPosition()).sub(player.getPosition()));
        }
    }

    @Override
    public void destroy(boolean removeFromCollection) {
        getPlayStage().addExplosion(position.x, position.y, GameplayConstants.EXPLOSION_SMALL_RADIUS);
        float distanceFromOrigin = distanceFromOrigin();
        if (distanceFromOrigin < Constants.DISTANCE_FROM_ORIGIN_SOUND_CUTOFF) {
            float soundMod = (Constants.DISTANCE_FROM_ORIGIN_SOUND_CUTOFF - distanceFromOrigin)
                    / Constants.DISTANCE_FROM_ORIGIN_SOUND_CUTOFF;
            soundMod *= Constants.ASTEROID_EXPLOSION_SOUND_MOD;
            SoundManager.playSoundEffect(SoundManager.SoundEffect.EXPLOSION, soundMod);
        }
        super.destroy(removeFromCollection);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Asteroid(getX(), getY(), getRadius(), linearVelocity);
    }

    @Override
    public float getProjectedRadius() {
        return super.getProjectedRadius() * GameplayConstants.ASTEROID_COLLISION_RADIUS_MOD;
    }

    @Override
    public boolean reportHit(Vector2 hitDirection) {
        float radius = getWidth() / 2;
        if (radius < GameplayConstants.ASTEROID_MIN_RADIUS) {
            destroy();
            return true;
        }
        int pieces = MathUtils.random(2, 3);
        float averageRadius;
        if (pieces == 2) {
            averageRadius = radius * 0.7f;
        } else {
            averageRadius = radius * 0.6f;
        }
        float angle = hitDirection.angle() + 90;
        PlayStage stage = (PlayStage) getStage();
        boolean spawnedPowerup = false;
        for (int i = 0; i < pieces; i++) {
            Vector2 heading = new Vector2(1, 0);
            angle += 360f / pieces;
            heading.setAngle(angle);
            if (!spawnedPowerup && Math.random() < GameplayConstants.POWERUP_SPAWN_CHANCE) {
                stage.addPowerup(Powerup.makeRandomPowerup(position.x, position.y, heading));
                spawnedPowerup = true;
            } else {
                float newRadius = MathUtils.random(0.9f, 1.1f) * averageRadius;
                stage.addAsteroid(new Asteroid(position.x, position.y, newRadius, heading));
            }
        }
        destroy();
        return true;
    }

    public static TextureRegion getRandomAsteroidTexture() {
        TextureRegion[] asteroids = Assets.instance.asteroids;
        return asteroids[MathUtils.random(asteroids.length - 1)];
    }
}
