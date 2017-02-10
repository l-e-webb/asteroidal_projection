package com.louiswebbgames.asteroidalprojection.gameplay.weapon;

import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.GameObject;

public abstract class Weapon {

    public GameObject mount;

    public Weapon(GameObject mount) {
        this.mount = mount;
    }

    public abstract void fire(GameObject target);

    public abstract void fire(Vector2 target);

}
