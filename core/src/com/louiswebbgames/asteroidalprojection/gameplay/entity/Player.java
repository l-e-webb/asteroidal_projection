package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.gameplay.geometry.Projection;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.LaserWeapon;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.Weapon;
import com.louiswebbgames.asteroidalprojection.utility.Assets;
import com.louiswebbgames.asteroidalprojection.utility.Controls;

public class Player extends GameObject {

    public static final String LOG_TAG = Player.class.getSimpleName();

    protected PlayerState state;

    protected int health;

    protected float dampening;
    protected float fireTimer;
    protected float blinkingTimer;

    protected Weapon laserWeapon;

    public Player() {
        super(0, 0, GameplayConstants.PLAYER_RADIUS, EntityType.PLAYER, CollisionType.CIRCLE);
        independentFacing = true;
        setMaxAngularSpeed(0);
        setMaxLinearSpeed(GameplayConstants.PLAYER_MAX_SPEED);
        setMinLinearSpeed(0);
        setMaxLinearAcceleration(GameplayConstants.PLAYER_ACCEL);
        dampening = GameplayConstants.PLAYER_DAMPENING;
        laserWeapon = new LaserWeapon(this, true);
        setState(PlayerState.BLINKING);
        health = GameplayConstants.PLAYER_MAX_HEALTH;
    }

    @Override
    public void updatePositionVector() {}

    @Override
    public Vector2 getProjectedPosition() {
        return Projection.project(localToStageCoordinates(new Vector2()));
    }

    @Override
    public void update(float delta) {
        if (!alive()) return;
        if (state == PlayerState.BLINKING) {
            blinkingTimer -= delta;
            if (blinkingTimer < 0) {
                setState(PlayerState.NORMAL);
            }
        }
        if (linearVelocity.len() > getZeroLinearSpeedThreshold()) {
            setOrientation(linearVelocity.angleRad());
        }
        Vector2 mousePosition = getStage().screenToStageCoordinates(
                new Vector2(Gdx.input.getX(), Gdx.input.getY())
        );
        //setOrientation(mouseDirection.angleRad());
        if (fireTimer < GameplayConstants.PLAYER_LASER_COOLDOWN) {
            fireTimer += delta;
        } else if (Controls.fire() && state != PlayerState.BLINKING) {
            laserWeapon.fire(mousePosition);
            fireTimer = 0;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!alive()) return;
        if (state == PlayerState.BLINKING && blinkingTimer % GameplayConstants.PLAYER_BLINDING_PERIOD <
                        GameplayConstants.PLAYER_BLINDING_PERIOD / 2) {
            return;
        }
        super.draw(batch, parentAlpha);
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

    public void setState(PlayerState state) {
        this.state = state;
        if (state == PlayerState.BLINKING) {
            blinkingTimer = GameplayConstants.PLAYER_BLINKING_DURATION;
        }
    }

    public boolean alive() {
        return state != PlayerState.DEAD;
    }

    public int currentHealth() {
        return health;
    }

    @Override
    public void moveBy(float x, float y) {
        ((PlayStage) getStage()).moveWorld(-x, -y);
    }

    @Override
    public TextureRegion getTexture() {
        return Assets.instance.player;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Player();
    }

    @Override
    public boolean reportHit(Vector2 hitDirection) {
        if (state != PlayerState.BLINKING) {
            health -= 1;
            setState(health > 0 ? PlayerState.BLINKING : PlayerState.DEAD);
            return true;
        }
        return false;
    }

    public enum PlayerState {
        NORMAL,
        BLINKING,
        DEAD
    }

}
