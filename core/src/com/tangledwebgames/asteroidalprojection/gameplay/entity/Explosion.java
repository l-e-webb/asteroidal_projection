package com.tangledwebgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.utility.Assets;

public class Explosion extends GameObject {

    public Explosion(float x, float y, float radius) {
        super(x, y, radius, EntityType.EXPLOSION);
        setAnimation(Assets.instance.explosion);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (animation.isAnimationFinished(timeSinceSpawn)) destroy();
    }

}
