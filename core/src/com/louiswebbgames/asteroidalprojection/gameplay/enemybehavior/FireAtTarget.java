package com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.EnemyGun;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.GameObject;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.Weapon;

public class FireAtTarget extends EnemyGun {

    protected float range;
    protected GameObject target;

    public FireAtTarget(float x, float y, float width, float height, GameObject target, FireRate rate, Weapon weapon, float range, TextureRegion texture, boolean rotateWithShot) {
        super(x, y, width, height, rate, weapon, texture, rotateWithShot);
        this.target = target;
        this.range = range;
    }

    public FireAtTarget(float x, float y, GameObject target, FireRate rate, Weapon weapon, float range) {
        this(x, y, 0, 0, target, rate, weapon, range, null, false);
    }

    @Override
    public Vector2 fire() {
        weapon.fire(target);
        return new Vector2(target.getPosition()).sub(getPosition());
    }

    @Override
    public boolean ready() {
        return super.ready() && weapon.mount.distance2(target) < range * range;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new FireAtTarget(getX(), getY(), target, rate, weapon, range);
    }
}
