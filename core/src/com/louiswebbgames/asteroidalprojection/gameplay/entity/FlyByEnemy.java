package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.FireAtTarget;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.FireRate;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.LaserWeapon;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.Weapon;
import com.louiswebbgames.asteroidalprojection.utility.Assets;

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
        Weapon weapon = new LaserWeapon(this, false);
        firePattern = new FireAtTarget(target, FireRate.FAST_DOUBLE_SHOT, weapon, GameplayConstants.FLY_BY_WEAPON_RANGE);
        setBehavior(new Pursue<>(this, target));
        pointValue = GameplayConstants.FLY_BY_POINT_VALUE;
    }

    @Override
    public TextureRegion getTexture() {
        return Assets.instance.flyByEnemy;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new FlyByEnemy(getX(), getY(), target);
    }
}
