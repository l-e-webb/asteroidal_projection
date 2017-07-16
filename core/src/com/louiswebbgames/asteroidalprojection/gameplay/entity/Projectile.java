package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.utility.Assets;
import com.louiswebbgames.asteroidalprojection.utility.Log;

import java.util.Iterator;

public class Projectile extends GameObject {

    public final ProjectileType projectileType;

    float timeSinceCollision;
    TextureRegion texture;
    
    public Projectile(float x, float y, Vector2 heading, ProjectileType type) {
        super(x, y, getProjectileWidth(type), getProjectileHeight(type), EntityType.PROJECTILE, CollisionType.POINT);
        this.projectileType = type;
        texture = getProjectileTexture(type);
        setMaxLinearSpeed(Projectile.getProjectileSpeed(type));
        setMinLinearSpeed(Projectile.getProjectileSpeed(type));
        this.linearVelocity = heading.setLength(Projectile.getProjectileSpeed(type));
        setOrientation(heading.angleRad());
        timeSinceCollision = Float.MAX_VALUE;
    }
    
    @Override
    public void update(float delta) {
        super.update(delta);

        if (isPiercing() && timeSinceCollision < GameplayConstants.PIERCING_LASER_COLLISION_COOLDOWN) {
            timeSinceCollision += delta;
            return;
        }

        PlayStage stage = (PlayStage) getStage();
        for (Iterator<Asteroid> iterator = stage.getAsteroids().iterator(); iterator.hasNext(); ) {
            Asteroid asteroid = iterator.next();
            if (collidesWith(asteroid)) {
                Log.log(LOG_TAG, "Projectile colliding with asteroid at " + getPosition().toString());
                asteroid.reportHit(linearVelocity);
                if (isPiercing()) {
                    timeSinceCollision = 0;
                } else {
                    destroy();
                }
                return;
            }
        }

        if (isPlayerProjectile()) {
            for (Iterator<Enemy> iterator = stage.getEnemies().iterator(); iterator.hasNext(); ) {
                Enemy enemy = iterator.next();
                if (collidesWith(enemy)) {
                    Log.log(LOG_TAG, "Player projectile colliding with enemy at " + getPosition().toString());
                    if (enemy.reportHit(linearVelocity)) {
                        iterator.remove();
                    }
                    if (isPiercing()) {
                        timeSinceCollision = 0;
                        ((PlayStage)getStage()).addExplosion(position.x, position.y, GameplayConstants.EXPLOSION_TINY_RADIUS);
                    } else {
                        if (enemy instanceof EnemyCruiser) {
                            ((PlayStage)getStage()).addExplosion(position.x, position.y, GameplayConstants.EXPLOSION_TINY_RADIUS);
                        }
                        destroy();
                    }
                    return;
                }
            }
        } else {
            Player player = ((PlayStage)getStage()).getPlayer();
            if (collidesWith(player)) {
                Log.log(LOG_TAG, "Enemy projectile colliding with player at " + getPosition().toString());
                player.reportHit(linearVelocity);
                if (isPiercing()) {
                    timeSinceCollision = 0;
                } else {
                    destroy();
                }
            }
        }
    }



    public boolean isPiercing() {
        return (projectileType == ProjectileType.ENEMY_PIERCING_LASER || projectileType == ProjectileType.PLAYER_PIERCING_LASER);
    }

    public boolean isPlayerProjectile() {
        return (projectileType == ProjectileType.PLAYER_LASER ||
                projectileType == ProjectileType.PLAYER_PIERCING_LASER ||
                projectileType == ProjectileType.PLAYER_ROUND_LASER ||
                projectileType == ProjectileType.PLAYER_MISSILE);
    }

    @Override
    public TextureRegion getTexture() {
        return texture;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Projectile(getX(), getY(), getLinearVelocity(), projectileType);
    }

    public static float getProjectileWidth(ProjectileType type) {
        switch (type) {
            case PLAYER_LASER:case ENEMY_LASER:default:
                return GameplayConstants.LASER_WIDTH;
            case PLAYER_PIERCING_LASER:case ENEMY_PIERCING_LASER:
                return GameplayConstants.PIERCING_LASER_WIDTH;
            case PLAYER_ROUND_LASER:case ENEMY_ROUND_LASER:
                return GameplayConstants.ROUND_PROJECTILE_WIDTH;
            case PLAYER_MISSILE:case ENEMY_MISSILE:
                return GameplayConstants.MISSILE_WIDTH;
        }
    }

    public static float getProjectileHeight(ProjectileType type) {
        switch (type) {
            case PLAYER_LASER:case ENEMY_LASER:default:
                return GameplayConstants.LASER_HEIGHT;
            case PLAYER_PIERCING_LASER:case ENEMY_PIERCING_LASER:
                return GameplayConstants.PIERCING_LASER_HEIGHT;
            case PLAYER_ROUND_LASER:case ENEMY_ROUND_LASER:
                return GameplayConstants.ROUND_PROJECTILE_WIDTH;
            case PLAYER_MISSILE:case ENEMY_MISSILE:
                return GameplayConstants.MISSILE_HEIGHT;
        }
    }

    public static float getProjectileSpeed(ProjectileType type) {
        switch (type) {
            case PLAYER_LASER:case PLAYER_PIERCING_LASER:default:
                return GameplayConstants.PLAYER_LASER_SPEED;
            case ENEMY_LASER:case ENEMY_PIERCING_LASER:
                return GameplayConstants.ENEMY_LASER_SPEED;
            case PLAYER_ROUND_LASER:
                return GameplayConstants.PLAYER_ROUND_SPEED;
            case ENEMY_ROUND_LASER:
                return GameplayConstants.ENEMY_ROUND_SPEED;
            case PLAYER_MISSILE:case ENEMY_MISSILE:
                return GameplayConstants.MISSILE_MIN_SPEED;
        }
    }

    public static TextureRegion getProjectileTexture(ProjectileType type) {
        switch (type) {
            case PLAYER_LASER:default:
                return Assets.instance.playerLaser;
            case PLAYER_PIERCING_LASER:
                return Assets.instance.playerPiercingLaser;
            case PLAYER_ROUND_LASER:
                return Assets.instance.playerRoundLaser;
            case ENEMY_LASER:
                return Assets.instance.enemyLaser;
            case ENEMY_PIERCING_LASER:
                return Assets.instance.enemyPiercingLaser;
            case ENEMY_ROUND_LASER:
                return Assets.instance.enemyRoundLaser;
            case PLAYER_MISSILE: case ENEMY_MISSILE:
                return null;
        }
    }

    public enum ProjectileType {
        PLAYER_LASER,
        ENEMY_LASER,
        PLAYER_PIERCING_LASER,
        ENEMY_PIERCING_LASER,
        PLAYER_ROUND_LASER,
        ENEMY_ROUND_LASER,
        PLAYER_MISSILE,
        ENEMY_MISSILE
    }
}
