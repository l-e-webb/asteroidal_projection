package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.FireRate;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.CollisionType;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.EntityType;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.GameObject;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.Weapon;

public abstract class EnemyGun extends GameObject {

    public FireRate rate;
    public Weapon weapon;
    protected int delayIndex;
    protected float fireTimer;

    public EnemyGun(float x, float y, FireRate rate, Weapon weapon) {
        super(x, y, 0, 0, EntityType.TURRET, CollisionType.NONE);
        this.rate = rate;
        this.weapon = weapon;
        delayIndex = 0;
        fireTimer = 0;
        weapon.setMount(this);
        independentExistence = false;
    }

    @Override
    public void update(float delta) {
        if (ready()) {
            fire();
            resetFireTimer();
        } else {
            fireTimer += delta;
        }
    }

    public boolean ready() {
        return rate.getDelays()[delayIndex] <= fireTimer;
    }

    public abstract void fire();

    public void resetFireTimer() {
        fireTimer = 0;
        delayIndex = (delayIndex + 1) % rate.getDelays().length;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public TextureRegion getTexture() {
        return null;
    }
}
