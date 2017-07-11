package com.louiswebbgames.asteroidalprojection.gameplay.weapon;

import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.GameObject;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.Projectile;

public class BaseWeapon extends Weapon {

    public BaseWeapon(GameObject mount, Projectile.ProjectileType type) {
        super(mount, type);
    }

    @Override
    public void fire(GameObject target) {
        this.fire(target.getPosition());
    }

    @Override
    public void fire(Vector2 target) {
        Vector2 sourcePosition = mount.getPosition();
        Vector2 heading = new Vector2(target)
                .sub(sourcePosition);
        ((PlayStage) mount.getStage()).addProjectile(new Projectile(sourcePosition.x, sourcePosition.y, heading, projectileType));
    }

}
