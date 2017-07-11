package com.louiswebbgames.asteroidalprojection.gameplay.weapon;

import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.GameObject;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.Projectile;
import com.louiswebbgames.asteroidalprojection.utility.Log;

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
    protected void launchProjectile(Vector2 heading) {
        super.launchProjectile(heading);
        super.launchProjectile(new Vector2(heading).rotate(spread));
        super.launchProjectile(new Vector2(heading).rotate(-spread));
    }

    public void setSpread(float spread) {
        this.spread = spread;
    }
}
