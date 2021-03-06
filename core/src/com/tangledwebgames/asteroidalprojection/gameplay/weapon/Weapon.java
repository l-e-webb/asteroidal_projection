package com.tangledwebgames.asteroidalprojection.gameplay.weapon;

import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.GameObject;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.SteerableObject;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.Projectile;

public abstract class Weapon {

    public GameObject mount;
    public Projectile.ProjectileType projectileType;

    public Weapon(GameObject mount, Projectile.ProjectileType type) {
        this.mount = mount;
        this.projectileType = type;
    }

    public abstract void fire(SteerableObject target);

    public abstract void fire(Vector2 target);

    public Projectile.ProjectileType getProjectileType() {
        return projectileType;
    }

    public void setProjectileType(Projectile.ProjectileType projectileType) {
        this.projectileType = projectileType;
    }

    public void setMount(GameObject mount) {
        this.mount = mount;
    }
}
