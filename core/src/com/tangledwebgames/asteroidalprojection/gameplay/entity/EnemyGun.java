package com.tangledwebgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior.FireRate;
import com.tangledwebgames.asteroidalprojection.gameplay.weapon.Weapon;
import com.tangledwebgames.asteroidalprojection.utility.Constants;
import com.tangledwebgames.asteroidalprojection.utility.SoundManager;

public abstract class EnemyGun extends GameObject {

    public FireRate rate;
    public Weapon weapon;
    public boolean makesSound;

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
        makesSound = true;
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
            if (makesSound) {
                float distanceFromOrigin = distanceFromOrigin();
                if (distanceFromOrigin < Constants.DISTANCE_FROM_ORIGIN_SOUND_CUTOFF) {
                    float soundMod = (Constants.DISTANCE_FROM_ORIGIN_SOUND_CUTOFF - distanceFromOrigin)
                            / Constants.DISTANCE_FROM_ORIGIN_SOUND_CUTOFF;
                    switch (weapon.getProjectileType()) {
                        case ENEMY_LASER:
                            SoundManager.playSoundEffect(SoundManager.SoundEffect.ENEMY_LASER, soundMod);
                            break;
                        case ENEMY_PIERCING_LASER:
                            SoundManager.playSoundEffect(SoundManager.SoundEffect.ENEMY_PIERCING_LASER, soundMod);
                            break;
                        case ENEMY_ROUND_LASER:
                            SoundManager.playSoundEffect(SoundManager.SoundEffect.ENEMY_ROUND_LASER, soundMod);
                    }
                }
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
