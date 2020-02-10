package com.tangledwebgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.GameplayConstants;
import com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior.FireAtTarget;
import com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior.FireRate;
import com.tangledwebgames.asteroidalprojection.gameplay.weapon.BaseWeapon;
import com.tangledwebgames.asteroidalprojection.gameplay.weapon.Weapon;
import com.tangledwebgames.asteroidalprojection.utility.Assets;
import com.tangledwebgames.asteroidalprojection.utility.Constants;

public class FlyByEnemy extends Enemy {

    GameObject target;

    public FlyByEnemy(float x, float y, GameObject target) {
        super(x, y, GameplayConstants.FLY_BY_RADIUS);
        setMaxLinearSpeed(GameplayConstants.FLY_BY_MAX_SPEED);
        setMinLinearSpeed(GameplayConstants.FLY_BY_MIN_SPEED);
        setMaxLinearAcceleration(GameplayConstants.FLY_BY_ACCEL);
        setMaxAngularSpeed(GameplayConstants.FLY_BY_MAX_ANGULAR_SPEED);
        setMaxAngularAcceleration(GameplayConstants.FLY_BY_MAX_ANGULAR_ACCEL);
        this.target = target;
        setBehavior(new Pursue<>(this, target));
        pointValue = GameplayConstants.FLY_BY_POINT_VALUE;
        setAnimation(Assets.instance.flyByEnemy);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new FlyByEnemy(getX(), getY(), target);
    }

    public static Enemy getRandomFlyBy(float x, float y, GameObject target, int epoch) {
        Enemy enemy = new FlyByEnemy(x, y, target);
        Weapon weapon = new BaseWeapon(enemy, Projectile.ProjectileType.ENEMY_LASER);
        FireRate fireRate;
        switch (epoch) {
            case 0:
                fireRate = MathUtils.randomBoolean() ? FireRate.SLOW_BURST : FireRate.SLOW_DOUBLE_SHOT;
                break;
            case 1:case 2:default:
                fireRate = MathUtils.randomBoolean() ? FireRate.SLOW_BURST : FireRate.FAST_DOUBLE_SHOT;
                break;
            case 3:
                fireRate = MathUtils.randomBoolean() ? FireRate.FAST_DOUBLE_SHOT : FireRate.FAST_BURST;
                break;

        }
        enemy.addActor(new FireAtTarget(
                enemy.getWidth() * (Constants.FLY_BY_CANNON_WIDTH_RATIO - 0.5f),
                enemy.getHeight() * (Constants.FLY_BY_CANNON_HEIGHT_RATIO - 0.5f),
                Constants.ENEMY_CANNON_RADIUS * 2,
                Constants.ENEMY_CANNON_RADIUS * 2,
                target,
                fireRate,
                weapon,
                GameplayConstants.FLY_BY_WEAPON_RANGE,
                Assets.instance.simpleCannon,
                true
        ));
        return enemy;
    }
}
