package com.louiswebbgames.asteroidalprojection.gameplay.weapon;

import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.Enemy;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.GameObject;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.Missile;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.Projectile;
import com.louiswebbgames.asteroidalprojection.utility.Log;

public class MissileLauncher extends BaseWeapon {

    public Iterable<? extends GameObject> targetSet;

    public MissileLauncher(GameObject mount, Iterable<? extends GameObject> targetSet, boolean playerMissile) {
        super(mount, playerMissile ? Projectile.ProjectileType.PLAYER_MISSILE : Projectile.ProjectileType.ENEMY_MISSILE);
        this.targetSet = targetSet;
    }

    @Override
    public void fire(Vector2 target) {
        GameObject nearest = null;
        float closest2 = Float.MAX_VALUE;
        float lockRad = GameplayConstants.MISSILE_LOCK_RADIUS * GameplayConstants.MISSILE_LOCK_RADIUS;
        for (GameObject object : targetSet) {
            float distance2 = target.dst2(object.getPosition());
            if (distance2 < lockRad && distance2 < closest2) {
                nearest = object;
                closest2 = distance2;
            }
        }
        if (nearest != null) {
            fire(nearest);
        } else {
            ((PlayStage)mount.getStage()).addProjectile(new Missile(
                    mount.getX(),
                    mount.getY(),
                    new Vector2(target).sub(mount.getPosition()),
                    projectileType == Projectile.ProjectileType.PLAYER_MISSILE
            ));
        }
    }

    @Override
    public void fire(GameObject target) {
        ((PlayStage)mount.getStage()).addProjectile(new Missile(
                mount.getX(),
                mount.getY(),
                target,
                projectileType == Projectile.ProjectileType.PLAYER_MISSILE
        ));
    }
}
