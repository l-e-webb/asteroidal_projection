package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;

public class PlayerExplosion extends Explosion {

    public PlayerExplosion(float x, float y, float radius) {
        super(x, y, radius);
    }

    @Override
    public void destroy(boolean removeFromCollection) {
        ((PlayStage)getStage()).gameOver();
        super.destroy(removeFromCollection);
    }
}
