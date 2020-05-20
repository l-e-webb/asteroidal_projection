package com.tangledwebgames.asteroidalprojection.gameplay.weapon;

import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.SteerableObject;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.Projectile;

public class WaveWeapon extends BaseWeapon {

    float spread;
    int numShotAngles;
    int shotIndex;

    public WaveWeapon(SteerableObject mount, Projectile.ProjectileType type, float spread, int numShotAngles) {
        super(mount, type);
        this.spread = spread;
        this.numShotAngles = numShotAngles;
        shotIndex = numShotAngles / 2;
    }

    @Override
    protected void launchProjectile(Vector2 heading) {
        float angleOffset;
        if (shotIndex <= numShotAngles) {
            angleOffset = (2f * shotIndex / numShotAngles - 1) * spread;
        } else {
            angleOffset = (-2f * shotIndex / numShotAngles + 3) * spread;
        }
        heading.rotate(angleOffset);
        super.launchProjectile(heading);
        shotIndex = (shotIndex + 1) % (numShotAngles * 2);
    }
}
