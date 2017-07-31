package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.FireAtTarget;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.FireRate;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.MaintainDistance;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.BaseWeapon;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.WaveWeapon;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.Weapon;
import com.louiswebbgames.asteroidalprojection.utility.Assets;
import com.louiswebbgames.asteroidalprojection.utility.Constants;

public class SniperEnemy extends Enemy {

    protected GameObject target;

    public SniperEnemy(float x, float y, GameObject target) {
        super(x, y, GameplayConstants.SNIPER_RADIUS);
        setMaxLinearSpeed(GameplayConstants.SNIPER_MAX_SPEED);
        setMaxLinearAcceleration(GameplayConstants.SNIPER_ACCEL);
        setMaxAngularSpeed(GameplayConstants.SNIPER_MAX_ANGULAR_SPEED);
        setMinLinearSpeed(GameplayConstants.SNIPER_MIN_SPEED);
        setMaxAngularAcceleration(GameplayConstants.SNIPER_MAX_ANGULAR_ACCEL);
        this.target = target;
        setBehavior(new MaintainDistance(this, target, GameplayConstants.SNIPER_MAINTAIN_DISTANCE));
        pointValue = GameplayConstants.SNIPER_POINT_VALUE;
        setAnimation(Assets.instance.sniperEnemy);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new SniperEnemy(getX(), getY(), target);
    }

    public static Enemy getRandomSniper(float x, float y, GameObject target, int epoch) {
        Enemy enemy = new SniperEnemy(x, y, target);
        Projectile.ProjectileType type;
        FireRate fireRate;
        Weapon weapon;
        switch (epoch) {
            case 0:
                if (MathUtils.random() < 0.75) {
                    fireRate = MathUtils.randomBoolean() ? FireRate.SLOW : FireRate.SLOW_DOUBLE_SHOT;
                    type = Projectile.ProjectileType.ENEMY_LASER;
                    weapon = new BaseWeapon(enemy, type);
                } else {
                    fireRate = FireRate.LONG_BURST;
                    type = Projectile.ProjectileType.ENEMY_ROUND_LASER;
                    weapon = new WaveWeapon(enemy, type, GameplayConstants.ENEMY_SPREAD_ATTACK_SPREAD, GameplayConstants.ENEMY_SPREAD_ATTACK_NUMSHOTS);
                }
                break;
            case 1:case 2:default:
                if (MathUtils.randomBoolean()) {
                    fireRate = MathUtils.randomBoolean() ? FireRate.SLOW_BURST : FireRate.SLOW_DOUBLE_SHOT;
                    type = MathUtils.randomBoolean() ? Projectile.ProjectileType.ENEMY_LASER : Projectile.ProjectileType.ENEMY_PIERCING_LASER;
                    weapon = new BaseWeapon(enemy, type);
                } else {
                    fireRate = FireRate.LONG_BURST;
                    type = Projectile.ProjectileType.ENEMY_ROUND_LASER;
                    weapon = new WaveWeapon(enemy, type, GameplayConstants.ENEMY_SPREAD_ATTACK_SPREAD, GameplayConstants.ENEMY_SPREAD_ATTACK_NUMSHOTS);
                }
                break;
            case 3:
                if (MathUtils.randomBoolean()) {
                    fireRate = MathUtils.randomBoolean() ? FireRate.FAST_DOUBLE_SHOT : FireRate.SLOW_BURST;
                    type = Projectile.ProjectileType.ENEMY_PIERCING_LASER;
                    weapon = new BaseWeapon(enemy, type);
                } else {
                    fireRate = MathUtils.randomBoolean() ? FireRate.LONG_BURST : FireRate.EXTRA_LONG_BURST;
                    type = Projectile.ProjectileType.ENEMY_ROUND_LASER;
                    weapon = new WaveWeapon(enemy, type, GameplayConstants.ENEMY_SPREAD_ATTACK_SPREAD, GameplayConstants.ENEMY_SPREAD_ATTACK_NUMSHOTS);
                }
                break;

        }
        float cannonWidth;
        float cannonHeight;
        TextureRegion cannonTexture;
        boolean rotateWithShot;
        if (type == Projectile.ProjectileType.ENEMY_ROUND_LASER) {
            cannonWidth = Constants.ENEMY_HOLE_CANNON_RADIUS * 2;
            cannonHeight = cannonWidth;
            cannonTexture = Assets.instance.holeCannon;
            rotateWithShot = false;
        } else {
            cannonWidth = Constants.ENEMY_CANNON_RADIUS * 2;
            cannonHeight = cannonWidth;
            cannonTexture = Assets.instance.simpleCannon;
            rotateWithShot = true;
        }
        enemy.addActor(new FireAtTarget(
                Constants.SNIPER_CANNON_OFFSET_X,
                Constants.SNIPER_CANNON_OFFSET_Y,
                cannonWidth,
                cannonHeight,
                target,
                fireRate,
                weapon,
                GameplayConstants.SNIPER_WEAPON_RANGE,
                cannonTexture,
                rotateWithShot
        ));
        return enemy;
    }
}
