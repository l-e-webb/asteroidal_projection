package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.FireAtTarget;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.FireRate;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.PointDefense;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.BaseWeapon;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.TripleLaserWeapon;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.WaveWeapon;
import com.louiswebbgames.asteroidalprojection.utility.Assets;

public class EnemyCruiser extends Enemy {

    protected GameObject target;
    protected int health;

    public EnemyCruiser(float x, float y, GameObject target) {
        super(x, y, GameplayConstants.CRUISER_WIDTH, GameplayConstants.CRUISER_HEIGHT);
        setBehavior(new Seek<>(this, target));
        this.target = target;
        setMinLinearSpeed(GameplayConstants.CRUISER_MIN_SPEED);
        setMaxLinearSpeed(GameplayConstants.CRUISER_MAX_SPEED);
        setMaxLinearAcceleration(GameplayConstants.CRUISER_ACCEL);
        setMaxAngularSpeed(GameplayConstants.CRUISER_MAX_ANGULAR_SPEED);
        setMaxAngularAcceleration(GameplayConstants.CRUISER_MAX_ANGULAR_ACCEL);
        health = GameplayConstants.CRUISER_HEALTH;
        pointValue = GameplayConstants.CRUISER_POINT_VALUE;
        addActor(new FireAtTarget(
                0,
                0,
                target,
                FireRate.SLOW_DOUBLE_SHOT,
                new TripleLaserWeapon(this, Projectile.ProjectileType.ENEMY_PIERCING_LASER,
                        GameplayConstants.CRUISER_MAIN_GUN_SPREAD),
                GameplayConstants.CRUISER_MAIN_GUN_RANGE
        ));
        addActor(new FireAtTarget(
                0,
                getHeight() / 2,
                target,
                FireRate.FAST_DOUBLE_SHOT,
                new BaseWeapon(this, Projectile.ProjectileType.ENEMY_LASER),
                GameplayConstants.CRUISER_SECONDARY_GUN_RANGE
        ));
        addActor(new FireAtTarget(
                0,
                getHeight() / 4,
                target,
                FireRate.EXTRA_LONG_BURST,
                new WaveWeapon(
                        this,
                        Projectile.ProjectileType.ENEMY_ROUND_LASER,
                        GameplayConstants.CRUISER_SPREAD_GUN_SPREAD,
                        GameplayConstants.CRUISER_SPREAD_GUN_NUM_SHOTS
                ),
                GameplayConstants.CRUISER_SPREAD_GUN_RANGE
        ));
    }

    public void initPointDefense() {
        Iterable<Asteroid> asteroids = ((PlayStage)getStage()).getAsteroids();
        addActor(new PointDefense(0, getHeight() / 2, asteroids));
        addActor(new PointDefense(getWidth() / 2, -getHeight() / 2, asteroids));
        addActor(new PointDefense(-getWidth() / 2, -getHeight() / 2, asteroids));
    }

    @Override
    public void destroy(boolean removeFromCollection) {
        ((PlayStage)getStage()).incrementNumCruisers(-1);
        super.destroy(removeFromCollection);
    }

    @Override
    public boolean reportHit(Vector2 direction) {
        health--;
        if (health <= 0) {
            destroy(false);
            return true;
        }
        return false;
    }

    @Override
    public TextureRegion getTexture() {
        return Assets.instance.cruiserEnemy;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new EnemyCruiser(getX(), getY(), target);
    }
}
