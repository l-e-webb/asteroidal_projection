package com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.EnemyGun;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.GameObject;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.Weapon;

public class FireAtTarget extends EnemyGun {

    protected float range;
    protected GameObject target;

    public FireAtTarget(float x, float y, GameObject target, FireRate rate, Weapon weapon, float range) {
        super(x, y, rate, weapon);
        this.target = target;
        this.range = range;
    }

    @Override
    public void fire() {
        weapon.fire(target);
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
