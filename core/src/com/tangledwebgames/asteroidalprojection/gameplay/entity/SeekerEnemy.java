package com.tangledwebgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.GameplayConstants;
import com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior.FireAtTarget;
import com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior.FireRate;
import com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior.MaintainDistance;
import com.tangledwebgames.asteroidalprojection.gameplay.weapon.BaseWeapon;
import com.tangledwebgames.asteroidalprojection.gameplay.weapon.Weapon;
import com.tangledwebgames.asteroidalprojection.utility.Assets;
import com.tangledwebgames.asteroidalprojection.utility.Constants;

public class SeekerEnemy extends Enemy {

    SteerableObject target;

    public SeekerEnemy(float x, float y, SteerableObject target) {
        super(x, y, GameplayConstants.SEEKER_RADIUS);
        this.target = target;
        setBehavior(new MaintainDistance(this, target, GameplayConstants.SEEKER_MAINTAIN_DISTANCE));
        setMaxLinearSpeed(GameplayConstants.SEEKER_MAX_SPEED);
        setMinLinearSpeed(GameplayConstants.SEEKER_MIN_SPEED);
        setMaxLinearAcceleration(GameplayConstants.SEEKER_ACCEL);
        setMaxAngularSpeed(GameplayConstants.SEEKER_MAX_ANGULAR_SPEED);
        setMaxAngularAcceleration(GameplayConstants.SEEKER_MAX_ANGULAR_ACCEL);
        pointValue = GameplayConstants.SEEKER_POINT_VALUE;
        setAnimation(Assets.instance.seekerEnemy);
    }

    @Override
    public void init() {
        super.init();
        FlightTrail trail = new FlightTrail(Color.RED);
        trail.setTarget(this);
        getStage().addActor(trail);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new SeekerEnemy(getX(), getY(), target);
    }

    public static Enemy getRandomSeeker(float x, float y, SteerableObject target, int epoch) {
        Enemy enemy = new SeekerEnemy(x, y, target);
        Weapon weapon = new BaseWeapon(enemy, Projectile.ProjectileType.ENEMY_LASER);
        FireRate fireRate;
        switch (epoch) {
            case 0:
                fireRate = MathUtils.randomBoolean() ? FireRate.SLOW : FireRate.SLOW_DOUBLE_SHOT;
                break;
            case 1:case 2:default:
                fireRate = MathUtils.randomBoolean() ? FireRate.SLOW_BURST : FireRate.FAST_DOUBLE_SHOT;
                break;
            case 3:
                fireRate = MathUtils.randomBoolean() ? FireRate.FAST_DOUBLE_SHOT : FireRate.RAPID;
                break;

        }
        enemy.addActor(new FireAtTarget(
                enemy.getWidth() * (Constants.SEEKER_CANNON_WIDTH_RATIO - 0.5f),
                enemy.getHeight() * (Constants.SEEKER_CANNON_HEIGHT_RATIO - 0.5f),
                Constants.ENEMY_CANNON_RADIUS * 2,
                Constants.ENEMY_CANNON_RADIUS * 2,
                target,
                fireRate,
                weapon,
                GameplayConstants.SEEKER_WEAPON_RANGE,
                Assets.instance.simpleCannon,
                true
        ));
        return enemy;
    }
}
