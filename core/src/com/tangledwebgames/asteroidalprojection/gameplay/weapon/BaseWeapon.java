package com.tangledwebgames.asteroidalprojection.gameplay.weapon;

import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.PlayStage;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.GameObject;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.SteerableObject;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.Projectile;

public class BaseWeapon extends Weapon {

    public BaseWeapon(GameObject mount, Projectile.ProjectileType type) {
        super(mount, type);
    }

    @Override
    public void fire(SteerableObject target) {
        this.fire(new Vector2(target.getPosition()));
    }

    @Override
    public void fire(Vector2 target) {
        Vector2 sourcePosition = mount.getPosition();
        Vector2 heading = new Vector2(target)
                .sub(sourcePosition);
        launchProjectile(heading);
    }

    protected void launchProjectile(Vector2 heading) {
        Vector2 sourcePosition = mount.getPosition();
        ((PlayStage) mount.getStage()).addProjectile(new Projectile(sourcePosition.x, sourcePosition.y, heading, projectileType));
    }

}
