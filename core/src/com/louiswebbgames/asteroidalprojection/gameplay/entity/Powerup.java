package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.utility.Assets;

public class Powerup extends GameObject {

    public final PowerupType powerupType;

    Player player;

    public Powerup(float x, float y, Vector2 heading, PowerupType type) {
        super(
                x,
                y,
                GameplayConstants.POWERUP_WIDTH,
                GameplayConstants.POWERUP_HEIGHT,
                EntityType.POWERUP,
                CollisionType.CIRCLE
        );
        linearVelocity = heading.setLength(
                MathUtils.random(GameplayConstants.POWERUP_MIN_SPEED, GameplayConstants.POWERUP_MAX_SPEED)
        );
        powerupType = type;
        setTexture(getPowerupTexture(type));
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (collidesWith(player)) {
            player.applyPowerup(powerupType);
            //TODO: play sound?
            destroy();
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Powerup(getX(), getY(), linearVelocity, powerupType);
    }

    public static TextureRegion getPowerupTexture(PowerupType type) {
        switch (type) {
            case EXTRA_HEALTH:
                return Assets.instance.extraHealthPowerup;
            case MISSILE_AMMO:
                return Assets.instance.missileAmmoPowerup;
            case PIERCING_LASERS:
                return Assets.instance.piercingLaserPowerup;
            case POINTS:default:
                return Assets.instance.pointsPowerup;
            case TRIPLE_LASERS:
                return Assets.instance.tripleLaserPowerup;
        }
    }

    public static Powerup makeRandomPowerup(float x, float y, Vector2 heading) {
        float seed = (float)Math.random();
        PowerupType type;
        if (seed < 0.2f) {
            type = PowerupType.EXTRA_HEALTH;
        } else if (seed < 0.4f) {
            type = PowerupType.TRIPLE_LASERS;
        } else if (seed < 0.6f) {
            type = PowerupType.PIERCING_LASERS;
        } else if (seed < 0.8f) {
            type = PowerupType.POINTS;
        } else {
            type = PowerupType.MISSILE_AMMO;
        }
        return new Powerup(x, y, heading, type);
    }

    public enum PowerupType {
        EXTRA_HEALTH,
        TRIPLE_LASERS,
        PIERCING_LASERS,
        MISSILE_AMMO,
        POINTS
    }
}
