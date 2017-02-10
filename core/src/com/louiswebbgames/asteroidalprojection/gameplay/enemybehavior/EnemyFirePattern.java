package com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior;

import com.louiswebbgames.asteroidalprojection.gameplay.weapon.Weapon;

public abstract class EnemyFirePattern {

    public FireRate rate;
    public Weapon weapon;
    protected int delayIndex;
    protected float fireTimer;

    EnemyFirePattern(FireRate rate, Weapon weapon) {
        this.rate = rate;
        this.weapon = weapon;
        delayIndex = 0;
        fireTimer = 0;
    }

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
}
