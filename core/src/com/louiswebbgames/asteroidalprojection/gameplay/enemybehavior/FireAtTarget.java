package com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior;

import com.louiswebbgames.asteroidalprojection.gameplay.entity.GameObject;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.Weapon;

public class FireAtTarget extends EnemyFirePattern {

    protected float range;
    protected GameObject target;

    public FireAtTarget(GameObject target, FireRate rate, Weapon weapon, float range) {
        super(rate, weapon);
        this.target = target;
        this.range = range;
    }

    @Override
    public void fire() {
        weapon.fire(target);
    }

    @Override
    public boolean ready() {
        return super.ready() && weapon.mount.distance(target) < range;
    }
}
