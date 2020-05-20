package com.tangledwebgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.GameplayConstants;
import com.tangledwebgames.asteroidalprojection.gameplay.PlayStage;
import com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior.FireAtTarget;
import com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior.FireRate;
import com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior.PointDefense;
import com.tangledwebgames.asteroidalprojection.gameplay.weapon.BaseWeapon;
import com.tangledwebgames.asteroidalprojection.gameplay.weapon.TripleLaserWeapon;
import com.tangledwebgames.asteroidalprojection.gameplay.weapon.WaveWeapon;
import com.tangledwebgames.asteroidalprojection.utility.Assets;
import com.tangledwebgames.asteroidalprojection.utility.Constants;

public class EnemyCruiser extends Enemy {

    protected SteerableObject target;
    protected int health;

    public EnemyCruiser(float x, float y, SteerableObject target) {
        super(x, y, GameplayConstants.CRUISER_WIDTH, GameplayConstants.CRUISER_HEIGHT);
        setBehavior(new Seek<>(this, target));
        this.target = target;
        setMinLinearSpeed(GameplayConstants.CRUISER_MIN_SPEED);
        setMaxLinearSpeed(GameplayConstants.CRUISER_MAX_SPEED);
        setMaxLinearAcceleration(GameplayConstants.CRUISER_ACCEL);
        setMaxAngularSpeed(GameplayConstants.CRUISER_MAX_ANGULAR_SPEED);
        setMaxAngularAcceleration(GameplayConstants.CRUISER_MAX_ANGULAR_ACCEL);
        pointValue = GameplayConstants.CRUISER_POINT_VALUE;
    }

    @Override
    public void init() {
        PlayStage stage = (PlayStage) getStage();
        stage.incrementNumCruisers(1);
        health = GameplayConstants.CRUISER_HEALTH;
        addActor(new FireAtTarget(
                getWidth() * Constants.CRUISER_MAIN_CANNON_WIDTH_RATIO,
                getHeight() * Constants.CRUISER_MAIN_CANNON_HEIGHT_RATIO,
                Constants.ENEMY_CANNON_RADIUS * 2,
                Constants.ENEMY_CANNON_RADIUS * 2,
                target,
                FireRate.SLOW_DOUBLE_SHOT,
                new TripleLaserWeapon(this, Projectile.ProjectileType.ENEMY_PIERCING_LASER,
                        GameplayConstants.CRUISER_MAIN_GUN_SPREAD),
                GameplayConstants.CRUISER_MAIN_GUN_RANGE,
                Assets.instance.simpleCannon,
                true
        ));
        addActor(new FireAtTarget(
                getWidth() * Constants.CRUISER_SECONDARY_CANNON_WIDTH_RATIO,
                getHeight() * Constants.CRUISER_SECONDARY_CANNON_HEIGHT_RATIO,
                Constants.ENEMY_CANNON_RADIUS * 2,
                Constants.ENEMY_CANNON_RADIUS * 2,
                target,
                FireRate.FAST_DOUBLE_SHOT,
                new BaseWeapon(this, Projectile.ProjectileType.ENEMY_LASER),
                GameplayConstants.CRUISER_SECONDARY_GUN_RANGE,
                Assets.instance.simpleCannon,
                true
        ));
        addActor(new FireAtTarget(
                getWidth() * Constants.CRUISER_SPREAD_CANNON_WIDTH_RATIO,
                getHeight() * Constants.CRUISER_SPREAD_CANNON_HEIGHT_RATIO,
                Constants.ENEMY_HOLE_CANNON_RADIUS * 2,
                Constants.ENEMY_HOLE_CANNON_RADIUS * 2,
                target,
                FireRate.EXTRA_LONG_BURST,
                new WaveWeapon(
                        this,
                        Projectile.ProjectileType.ENEMY_ROUND_LASER,
                        GameplayConstants.CRUISER_SPREAD_GUN_SPREAD,
                        GameplayConstants.CRUISER_SPREAD_GUN_NUM_SHOTS
                ),
                GameplayConstants.CRUISER_SPREAD_GUN_RANGE,
                Assets.instance.holeCannon,
                false
        ));
        Iterable<Asteroid> asteroids = stage.getAsteroids();
        addActor(new PointDefense(
                getWidth() * Constants.CRUISER_POINT_DEF_1_WIDTH_RATIO,
                getHeight() * Constants.CRUISER_POINT_DEF_1_HEIGHT_RATIO,
                Constants.POINT_DEFENSE_CANNON_RADIUS * 2,
                Constants.POINT_DEFENSE_CANNON_RADIUS * 2,
                Assets.instance.holeCannon,
                true,
                asteroids
        ));
        addActor(new PointDefense(
                getWidth() * Constants.CRUISER_POINT_DEF_2_WIDTH_RATIO,
                getHeight() * Constants.CRUISER_POINT_DEF_2_HEIGHT_RATIO,
                Constants.POINT_DEFENSE_CANNON_RADIUS * 2,
                Constants.POINT_DEFENSE_CANNON_RADIUS * 2,
                Assets.instance.holeCannon,
                true,
                asteroids
        ));
        addActor(new PointDefense(
                getWidth() * Constants.CRUISER_POINT_DEF_3_WIDTH_RATIO,
                getHeight() * Constants.CRUISER_POINT_DEF_3_HEIGHT_RATIO,
                Constants.POINT_DEFENSE_CANNON_RADIUS * 2,
                Constants.POINT_DEFENSE_CANNON_RADIUS * 2,
                Assets.instance.holeCannon,
                true,
                asteroids
        ));
        setAnimation(Assets.instance.cruiserEnemy);
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public void destroy(boolean removeFromCollection) {
        ((PlayStage)getStage()).incrementNumCruisers(-1);
        super.destroy(removeFromCollection);
    }

    @Override
    public float getExplosionRadius() {
        return GameplayConstants.CRUISER_EXPLOSION_RADIUS;
    }

    @Override
    public boolean reportHit(Vector2 direction) {
        takeDamage(1);
        if (health <= 0) {
            destroy(false);
            return true;
        }
        return false;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new EnemyCruiser(getX(), getY(), target);
    }
}
