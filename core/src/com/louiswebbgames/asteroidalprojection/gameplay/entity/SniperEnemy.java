package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.FireAtTarget;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.FireRate;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.MaintainDistance;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.LaserWeapon;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.Weapon;
import com.louiswebbgames.asteroidalprojection.utility.Assets;

public class SniperEnemy extends Enemy {

    protected GameObject target;

    public SniperEnemy(float x, float y, GameObject target) {
        super(x, y, GameplayConstants.SNIPER_RADIUS);
        setMaxLinearSpeed(GameplayConstants.SNIPER_MAX_SPEED);
        setMaxLinearAcceleration(GameplayConstants.SNIPER_ACCEL);
        setMaxAngularSpeed(GameplayConstants.SNIPER_MAX_ANGULAR_SPEED);
        setMinLinearSpeed(GameplayConstants.SNIPER_MIN_SPEED);
        setMaxLinearAcceleration(GameplayConstants.SNIPER_MAX_ANGULAR_ACCEL);
        this.target = target;
        setBehavior(new MaintainDistance(this, target, GameplayConstants.SNIPER_MAINTAIN_DISTANCE));
        Weapon weapon = new LaserWeapon(this, false);
        firePattern = new FireAtTarget(target, FireRate.SLOW_DOUBLE_SHOT, weapon, GameplayConstants.SNIPER_WEAPON_RANGE);
    }

    @Override
    public TextureRegion getTexture() {
        return Assets.instance.sniperEnemy;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new SniperEnemy(getX(), getY(), target);
    }
}
