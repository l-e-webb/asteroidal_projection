package com.tangledwebgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.GameplayConstants;
import com.tangledwebgames.asteroidalprojection.gameplay.geometry.Projection;
import com.tangledwebgames.asteroidalprojection.gameplay.weapon.BaseWeapon;
import com.tangledwebgames.asteroidalprojection.gameplay.weapon.MissileLauncher;
import com.tangledwebgames.asteroidalprojection.gameplay.weapon.TripleLaserWeapon;
import com.tangledwebgames.asteroidalprojection.gameplay.weapon.Weapon;
import com.tangledwebgames.asteroidalprojection.utility.*;

public class Player extends SteerableObject {

    public static final String LOG_TAG = Player.class.getSimpleName();

    protected PlayerState state;

    protected int health;
    protected int missileAmmo;

    protected float dampening;
    protected float fireTimer;
    protected float missileFireTimer;
    protected float blinkingTimer;

    protected Weapon primaryWeapon;
    protected Weapon secondaryWeapon;
    protected Weapon laserWeapon;
    protected Weapon tripleLaserWeapon;
    protected Weapon missileLauncher;

    protected GameObject cannon;

    protected float tripleLaserDuration;
    protected float piercingLaserDuration;

    boolean active;

    public Player() {
        super(0, 0, GameplayConstants.PLAYER_RADIUS, EntityType.PLAYER, CollisionType.CIRCLE);
        independentFacing = true;
        setMaxAngularSpeed(0);
        setMaxLinearSpeed(GameplayConstants.PLAYER_MAX_SPEED);
        setMinLinearSpeed(0);
        setMaxLinearAcceleration(GameplayConstants.PLAYER_ACCEL);
        dampening = GameplayConstants.PLAYER_DAMPENING;
        setAnimation(Assets.instance.player);
    }

    public void init() {
        clearChildren();
        laserWeapon = new BaseWeapon(this, Projectile.ProjectileType.PLAYER_LASER);
        tripleLaserWeapon = new TripleLaserWeapon(
                this,
                Projectile.ProjectileType.PLAYER_LASER,
                GameplayConstants.PLAYER_TRIPLE_LASER_SPREAD
        );
        primaryWeapon = laserWeapon;
        missileLauncher = new MissileLauncher(this, getPlayStage().getEnemies(), true);
        secondaryWeapon = missileLauncher;
        setState(PlayerState.BLINKING);
        health = GameplayConstants.PLAYER_MAX_HEALTH;
        missileAmmo = GameplayConstants.STARTING_MISSILE_AMMO;
        cannon = new GameObject(
                getWidth() * (Constants.PLAYER_CANNON_WIDTH_RATIO - 0.5f),
                getHeight() * (Constants.PLAYER_CANNON_HEIGHT_RATIO - 0.5f),
                Constants.PLAYER_CANNON_RADIUS,
                EntityType.TURRET
        );
        cannon.independentExistence = false;
        cannon.independentScaling = false;
        cannon.setTexture(Assets.instance.simpleCannon);
        Booster booster = new Booster(
                getWidth() / 2,
                getHeight() / 4,
                getHeight() / 3.5f,
                0.1f,
                2,
                Color.BLACK
        );
        addActor(cannon);
        addActor(booster);
        FlightTrail flightTrail = new FlightTrail(Color.WHITE);
        flightTrail.setTarget(this);
        getStage().addActor(flightTrail);
        setActive(true);
        setPosition(0, 0);

    }

    @Override
    public void act(float delta) {
        if (!active || !alive()) return;
        super.act(delta);
    }

    @Override
    public void update(float delta) {
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
        cannon.setRotation(mousePosition.angle() - 90);
        if (tripleLaserDuration > 0) {
            tripleLaserDuration -= delta;
            if (tripleLaserDuration < 0) {
                primaryWeapon = laserWeapon;
            }
        }
        if (piercingLaserDuration > 0) {
            piercingLaserDuration -= delta;
            if (piercingLaserDuration < 0) {
                laserWeapon.setProjectileType(Projectile.ProjectileType.PLAYER_LASER);
                tripleLaserWeapon.setProjectileType(Projectile.ProjectileType.PLAYER_LASER);
            }
        }
        if (fireTimer < GameplayConstants.PLAYER_LASER_COOLDOWN) {
            fireTimer += delta;
        } else if (Controls.fire() && state == PlayerState.NORMAL) {
            primaryWeapon.fire(mousePosition);
            fireTimer = 0;
            SoundManager.SoundEffect effect;
            if (piercingLaserDuration > 0) {
                effect = SoundManager.SoundEffect.PLAYER_PIERCING_LASER;
            } else {
                effect = SoundManager.SoundEffect.PLAYER_LASER;
            }
            float soundMod = tripleLaserDuration > 0 ? Constants.TRIPLE_LASER_SOUND_AMP : 1f;
            SoundManager.playSoundEffect(effect, soundMod);
        }
        if (missileFireTimer < GameplayConstants.PLAYER_MISSILE_COOLDOWN) {
            missileFireTimer += delta;
        } else if (missileAmmo > 0 && Controls.secondaryFire() && state == PlayerState.NORMAL) {
            Vector2 targetLoc = Projection.unproject(mousePosition);
            secondaryWeapon.fire(targetLoc);
            missileFireTimer = 0;
            missileAmmo--;
            SoundManager.playSoundEffect(SoundManager.SoundEffect.PLAYER_MISSILE);
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

    public PlayerState getState() {
        return state;
    }

    public int currentHealth() {
        return health;
    }

    public float getTripleLaserDuration() {
        return tripleLaserDuration;
    }

    public float getPiercingLaserDuration() {
        return piercingLaserDuration;
    }

    public int getMissileAmmo() {
        return missileAmmo;
    }

    @Override
    public void moveBy(float x, float y) {
        getPlayStage().moveWorld(-x, -y);
        positionChanged();
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Player();
    }

    @Override
    public boolean reportHit(Vector2 hitDirection) {
        if (state != PlayerState.NORMAL) {
            return false;
        }
        health -= 1;
        setState(health > 0 ? PlayerState.BLINKING : PlayerState.DEAD);
        SoundManager.SoundEffect effect = alive() ? SoundManager.SoundEffect.IMPACT : SoundManager.SoundEffect.LARGE_EXPLOSION;
        SoundManager.playSoundEffect(effect);
        if (!alive()) {
            setActive(false);
            getPlayStage().addExplosion(new PlayerExplosion(0, 0, GameplayConstants.EXPLOSION_LARGE_RADIUS));

        }
        return true;
}

    public void applyPowerup(Powerup.PowerupType type) {
        SoundManager.SoundEffect effect = SoundManager.SoundEffect.POWERUP;
        switch (type) {
            case PIERCING_LASERS:
                piercingLaserDuration = GameplayConstants.PIERCING_LASER_DURATION;
                laserWeapon.setProjectileType(Projectile.ProjectileType.PLAYER_PIERCING_LASER);
                tripleLaserWeapon.setProjectileType(Projectile.ProjectileType.PLAYER_PIERCING_LASER);
                break;
            case TRIPLE_LASERS:
                tripleLaserDuration = GameplayConstants.TRIPLE_LASER_DURATION;
                primaryWeapon = tripleLaserWeapon;
                break;
            case EXTRA_HEALTH:
                health = Math.min(health + GameplayConstants.HEALTH_POWERUP_ADD, GameplayConstants.PLAYER_MAX_HEALTH);
                break;
            case MISSILE_AMMO:
                missileAmmo += GameplayConstants.MISSILE_AMMO_AMOUNT;
                break;
            case POINTS:default:
                getPlayStage().incrementScore(GameplayConstants.POINTS_POWERUP_VALUE);
                effect = SoundManager.SoundEffect.COIN;
                break;
        }
        SoundManager.playSoundEffect(effect);
    }

    public void setActive(boolean active) {
        this.active = active;
        this.setVisible(active);
    }

    public enum PlayerState {
        NORMAL,
        BLINKING,
        DEAD
    }

}
