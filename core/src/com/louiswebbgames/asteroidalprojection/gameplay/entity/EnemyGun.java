package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
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
    protected boolean rotateWithShot;

    public EnemyGun(float x, float y, float width, float height, FireRate rate, Weapon weapon, TextureRegion texture, boolean rotateWithShot) {
        super(x, y, width, height, EntityType.TURRET, CollisionType.NONE);
        this.rate = rate;
        this.weapon = weapon;
        delayIndex = 0;
        fireTimer = 0;
        weapon.setMount(this);
        independentExistence = false;
        independentScaling = false;
        setTexture(texture);
        this.rotateWithShot = rotateWithShot;
    }

    public EnemyGun(float x, float y, FireRate rate, Weapon weapon) {
        this(x, y, 0, 0, rate, weapon, null, false);
    }

    @Override
    public void update(float delta) {
        if (ready()) {
            Vector2 fireDirection = fire();
            if (rotateWithShot && fireDirection != null) {
                setOrientation(fireDirection.angleRad());
            }
            resetFireTimer();
        } else {
            fireTimer += delta;
        }
    }

    public boolean ready() {
        return rate.getDelays()[delayIndex] <= fireTimer;
    }

    public abstract Vector2 fire();

    public void resetFireTimer() {
        fireTimer = 0;
        delayIndex = (delayIndex + 1) % rate.getDelays().length;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

}
