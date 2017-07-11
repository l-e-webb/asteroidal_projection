package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.FireAtTarget;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.FireRate;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.MaintainDistance;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.BaseWeapon;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.Weapon;
import com.louiswebbgames.asteroidalprojection.utility.Assets;

public class SeekerEnemy extends Enemy {

    GameObject target;

    public SeekerEnemy(float x, float y, GameObject target) {
        super(x, y, GameplayConstants.SEEKER_RADIUS);
        this.target = target;
        setBehavior(new MaintainDistance(this, target, GameplayConstants.SEEKER_MAINTAIN_DISTANCE));
        Weapon weapon = new BaseWeapon(this, Projectile.ProjectileType.ENEMY_LASER);
        addActor(new FireAtTarget(0, 0, target, FireRate.FAST, weapon, GameplayConstants.SEEKER_WEAPON_RANGE));
        setMaxLinearSpeed(GameplayConstants.SEEKER_MAX_SPEED);
        setMinLinearSpeed(GameplayConstants.SEEKER_MIN_SPEED);
        setMaxLinearAcceleration(GameplayConstants.SEEKER_ACCEL);
        setMaxAngularSpeed(GameplayConstants.SEEKER_MAX_ANGULAR_SPEED);
        setMaxAngularAcceleration(GameplayConstants.SEEKER_MAX_ANGULAR_ACCEL);
        pointValue = GameplayConstants.SEEKER_POINT_VALUE;
    }

    @Override
    public TextureRegion getTexture() {
        return Assets.instance.seekerEnemy;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new SeekerEnemy(getX(), getY(), target);
    }
}
