package com.tangledwebgames.asteroidalprojection.gameplay.weapon;

import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.GameplayConstants;
import com.tangledwebgames.asteroidalprojection.gameplay.PlayStage;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.GameObject;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.Missile;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.Projectile;

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
        float lockRad = GameplayConstants.MISSILE_LOCK_RADIUS2;
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
            //Disable firing when no target available.
            //TODO: play sound?
//            ((PlayStage)mount.getStage()).addProjectile(new Missile(
//                    mount.getX(),
//                    mount.getY(),
//                    new Vector2(target).sub(mount.getPosition()),
//                    projectileType == Projectile.ProjectileType.PLAYER_MISSILE
//            ));
        }
    }

    @Override
    public void fire(GameObject target) {
        Vector2 start = mount.getPosition();
        Vector2 headingToTarget = new Vector2(target.getPosition()).sub(start);
        PlayStage stage = mount.getPlayStage();
        stage.addProjectile(new Missile(
                start.x,
                start.y,
                target,
                new Vector2(headingToTarget).rotate(GameplayConstants.MISSILE_INITIAL_TRAJECTORY_DELTA),
                projectileType == Projectile.ProjectileType.PLAYER_MISSILE
        ));
        stage.addProjectile(new Missile(
                start.x,
                start.y,
                target,
                new Vector2(headingToTarget).rotate(-GameplayConstants.MISSILE_INITIAL_TRAJECTORY_DELTA),
                projectileType == Projectile.ProjectileType.PLAYER_MISSILE
        ));
    }
}
