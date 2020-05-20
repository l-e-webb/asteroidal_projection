package com.tangledwebgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.tangledwebgames.asteroidalprojection.utility.Assets;
import com.tangledwebgames.asteroidalprojection.utility.Log;

public class Booster extends GameObject {

    private static final String LOG_TAG = Booster.class.getSimpleName();

    protected SteerableObject steerableParent;
    protected float heightVariation;
    protected float minHeight;
    protected float noiseRatio;
    protected float noisePeriod;

    public Booster(float width, float minHeight, float maxHeight, float noiseRatio, float noisePeriod, Color tint) {
        super(0, 0, width, 0, EntityType.OTHER);
        this.heightVariation = maxHeight - minHeight;
        this.minHeight = minHeight;
        this.noiseRatio = noiseRatio;
        this.noisePeriod = noisePeriod;
        independentExistence = false;
        independentScaling = false;
        independentRotation = false;
        setTexture(Assets.instance.gradient);
    }

    @Override
    protected void setParent(Group parent) {
        super.setParent(parent);
        try {
            steerableParent = (SteerableObject) parent;
        } catch (ClassCastException e) {
            Log.log(LOG_TAG, "Attempting to add booster to non-steerable parent");
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setHeight(getBoosterHeight());
        setY(-getParent().getHeight() / 2 - getHeight() / 2);
    }

    protected float getBoosterHeight() {
        if (steerableParent == null) return 0;
        float targetHeightRatio = steerableParent.linearVelocity.len2() /
                steerableParent.getMaxLinearSpeed() * steerableParent.getMaxLinearSpeed();
        float height = minHeight + heightVariation * targetHeightRatio;
        height *= (1 + MathUtils.sin(timeSinceSpawn * MathUtils.PI2 / noisePeriod) * noiseRatio);
        return height;
    }

}
