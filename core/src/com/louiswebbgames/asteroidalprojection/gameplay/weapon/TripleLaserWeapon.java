package com.louiswebbgames.asteroidalprojection.gameplay.weapon;

import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.GameObject;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.Projectile;

public class TripleLaserWeapon extends BaseWeapon {

    float spread;

    public TripleLaserWeapon(GameObject mount, Projectile.ProjectileType type, float spread) {
        super(mount, type);
        this.spread = spread;
    }

    public TripleLaserWeapon(GameObject mount, Projectile.ProjectileType type) {
        this(mount, type, 45f);
    }

    @Override
    public void fire(Vector2 target) {
        super.fire(target);
        super.fire(target.rotate(spread));
        super.fire(target.rotate(-2 * spread));
    }

    public void setSpread(float spread) {
        this.spread = spread;
    }
}
