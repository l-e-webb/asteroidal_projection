package com.louiswebbgames.hyperbocalypse.gameplay.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.louiswebbgames.hyperbocalypse.gameplay.GameplayConstants;
import com.louiswebbgames.hyperbocalypse.gameplay.PlayStage;
import com.louiswebbgames.hyperbocalypse.gameplay.geometry.Projection;
import com.louiswebbgames.hyperbocalypse.utility.Assets;
import com.louiswebbgames.hyperbocalypse.utility.Controls;
import com.louiswebbgames.hyperbocalypse.utility.Log;

public class Player extends GameObject {

    public static final String LOG_TAG = Player.class.getSimpleName();

    protected float dampening;

    protected float fireTimer;

    public Player() {
        super(0, 0, GameplayConstants.PLAYER_RADIUS, EntityType.PLAYER);
        independentFacing = true;
        setMaxAngularSpeed(0);
        setMaxLinearSpeed(GameplayConstants.PLAYER_MAX_SPEED);
        setMinLinearSpeed(0);
        setMaxLinearAcceleration(GameplayConstants.PLAYER_ACCEL);
        dampening = GameplayConstants.PLAYER_DAMPENING;
    }

    @Override
    public void updatePositionVector() {}

    @Override
    public Vector2 getProjectedPosition() {
        return Projection.project(localToStageCoordinates(new Vector2()));
    }

    @Override
    public void update(float delta) {
        Vector2 mouseDirection = getStage().screenToStageCoordinates(
                new Vector2(Gdx.input.getX(), Gdx.input.getY())
        );
        setOrientation(mouseDirection.angleRad());
        if (fireTimer < GameplayConstants.PLAYER_SHOT_COOLDOWN) {
            fireTimer += delta;
        } else if (Controls.fire()) {
            ((PlayStage)getStage()).addPlayerProjectile(mouseDirection);
            fireTimer = 0;
        }
    }

    @Override
    public void calculateVelocity(float delta) {
        Vector2 accel = new Vector2();
        if (Controls.left()) {
            accel.x -= 1;
        }
        if (Controls.right()) {
            accel.x += 1;
        }
        if (Controls.down()) {
            accel.y -= 1;
        }
        if (Controls.up()) {
            accel.y += 1;
        }
        if (accel.len2() > 0) {
            accel.setLength(getMaxLinearAcceleration());
        } else {
            if (dampening > linearVelocity.len()) {
                linearVelocity = new Vector2();
                return;
            } else {
                accel.x = -linearVelocity.x;
                accel.y = -linearVelocity.y;
                accel.setLength(dampening);
            }
        }
        linearVelocity.add(accel).limit(getMaxLinearSpeed());
    }

    @Override
    public void moveBy(float x, float y) {
        ((PlayStage) getStage()).moveWorld(-x, -y);
    }

    @Override
    public TextureRegion getTexture() {
        return Assets.instance.triangle;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Player();
    }

}
