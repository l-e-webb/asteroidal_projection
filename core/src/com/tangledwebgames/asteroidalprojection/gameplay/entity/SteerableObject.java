package com.tangledwebgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.tangledwebgames.asteroidalprojection.gameplay.GameplayConstants;
import com.tangledwebgames.asteroidalprojection.gameplay.PlayStage;
import com.tangledwebgames.asteroidalprojection.utility.Log;

/**
 * Abstract superclass for all objects in the game, implements most Steerable
 * interface methods and some scene2d Actor methods.  Defines each object
 * as having radius, position, and velocity properties.
 */
public abstract class SteerableObject extends GameObject implements Steerable<Vector2> {

    public static final String LOG_TAG = SteerableObject.class.getSimpleName();

    protected static SteeringAcceleration<Vector2> accel =
            new SteeringAcceleration<Vector2>(new Vector2(0,0));

    protected SteeringBehavior<Vector2> behavior;

    protected Vector2 linearVelocity;
    protected float orientation;
    protected float angularVelocity;
    private float maxAngularAcceleration;
    private float maxAngularSpeed;
    private float maxLinearAcceleration;
    private float maxLinearSpeed;
    private float minLinearSpeed;
    private boolean tagged;
    protected boolean independentFacing = false;

    private int updateFrame;

    protected CollisionType collisionType;

    public SteerableObject(float x, float y, float width, float height, EntityType type, CollisionType colType) {
        super(x, y, width, height, type);
        linearVelocity = new Vector2(1, 0);
        angularVelocity = 0;
        orientation = 0;
        updateFrame = 0;
        this.collisionType = colType;
    }

    public SteerableObject(float x, float y, float radius, EntityType type, CollisionType colType) {
        this(x, y, radius * 2, radius * 2, type, colType);
    }

    @Override
    public void act(float delta) {
        if (independentExistence) {
            calculateVelocity(delta);
            applyMotion(delta);
            if (distanceFromOrigin() > GameplayConstants.HORIZON) {
                destroy();
                return;
            }
        }

        update(delta);

        super.act(delta);
    }

    protected void update(float delta) {}

    protected void applyMotion(float delta) {
        moveBy(linearVelocity.x * delta, linearVelocity.y * delta);
        if (independentFacing) {
            adjustOrientation(angularVelocity * delta);
        }
    }

    public void calculateVelocity(float delta) {
        if (behavior == null) return;
        updateFrame = (updateFrame + 1) % 2;
        if (updateFrame == 0) return;

        Vector2 accel = behavior.calculateSteering(SteerableObject.accel).linear;
        accel = accel.nor();
        float desiredOrientation = accel.angleRad();
        float desiredOrientationChange = desiredOrientation - orientation;
        //If the desired change in angle is greater than 180 degrees or less than -180 degrees,
        //we adjust so that it is between -180 and 180.  For example, a desired rotation of 300
        //degrees will change to -60.
        while (desiredOrientationChange > MathUtils.PI) {
            desiredOrientationChange -= 2 * MathUtils.PI;
        }
        while (desiredOrientationChange < - MathUtils.PI) {
            desiredOrientationChange += 2 * MathUtils.PI;
        }
        float angularAccel = desiredOrientationChange > 0 ?
                maxAngularAcceleration : -maxAngularAcceleration;
        angularVelocity = angularVelocity + angularAccel * delta;
        angularVelocity = MathUtils.clamp(angularVelocity, -maxAngularSpeed, maxAngularSpeed);

        //Check whether there is sufficient angular velocity to reach desired orientation.
        float angleChange = angularVelocity * delta;
        boolean sufficientAngularVel = false;
        if (desiredOrientationChange > 0 && angleChange > desiredOrientationChange ||
                desiredOrientationChange < 0 && angleChange < desiredOrientationChange) {
            sufficientAngularVel = true;
        }

        if (independentFacing) {
            //If it is independently facing, simply accelerate in the indicated direction.
            linearVelocity.mulAdd(accel, maxLinearAcceleration * delta).limit(maxLinearSpeed);
            if (sufficientAngularVel) {
                setOrientation(desiredOrientation);
                angularVelocity = 0;
            }
        } else {
            //If not independently facing, rotate linear velocity by angular velocity.
            if (sufficientAngularVel) {
                linearVelocity.setAngleRad(desiredOrientation);
                angularVelocity = 0;
            } else {
                linearVelocity.setAngleRad(orientation + angleChange);
                //If facing near desired heading, accelerate; if facing away from desired heading,
                //de-accelerate to make a sharper turn.
                if (Math.abs(desiredOrientationChange) < MathUtils.PI / 2) {
                    accelerate(maxLinearAcceleration * delta);
                } else {
                    accelerate(-maxLinearAcceleration * delta);
                }
            }
            //Always orient in direction of velocity.
            setOrientation(linearVelocity.angleRad());
        }
    }

    public void accelerate(float accel) {
        float newSpeed = linearVelocity.len() + accel;
        newSpeed = MathUtils.clamp(newSpeed, minLinearSpeed, maxLinearSpeed);
        linearVelocity.setLength(newSpeed);
    }

    @Override
    public float getBoundingRadius() {
        return getRadius();
    }

    public void adjustOrientation(float angle) {
        setOrientation(orientation + angle);
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    public float getHeadingAngle() {
        if (linearVelocity.len() > getZeroLinearSpeedThreshold()) {
            return linearVelocity.angleRad();
        }
        return orientation;
    }

    @Override
    public void setOrientation(float orientation) {
        this.orientation = orientation % (2 * MathUtils.PI);
        setRotation(orientation * MathUtils.radiansToDegrees - 90);
    }

    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    public float getMinLinearSpeed() {
        return minLinearSpeed;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return GameplayConstants.ZERO_SPEED_THRESHOLD;
    }

    @Override
    public void setMaxAngularAcceleration(float accel) {
        maxAngularAcceleration = accel;
    }

    @Override
    public void setMaxAngularSpeed(float speed) {
        maxAngularSpeed = speed;
    }

    @Override
    public void setMaxLinearAcceleration(float accel) {
        maxLinearAcceleration = accel;
    }

    @Override
    public void setMaxLinearSpeed(float speed) {
        maxLinearSpeed = speed;
    }

    public void setMinLinearSpeed(float speed) {
        minLinearSpeed = speed;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float threshold) {}

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = MathUtils.cos(angle);
        outVector.y = MathUtils.sin(angle);
        return outVector;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return vector.angleRad();
    }

    public boolean collidesWith(SteerableObject object) {
        if (projectedDistance(object) > getProjectedRadius() + object.getProjectedRadius())
            return false;
        switch (collisionType) {
            case CIRCLE:
                switch (object.collisionType) {
                    case CIRCLE:
                        return Intersector.overlaps(
                                getCircle(), object.getCircle()
                        );
                    case RECTANGLE:
                        return Intersector.overlapConvexPolygons(
                                getHexagon(), object.getRectangle()
                        );
                    case POINT:
                        return getCircle().contains(object.projectedPosition);
                }
            case RECTANGLE:
                switch (object.collisionType) {
                    case CIRCLE:
                        return Intersector.overlapConvexPolygons(
                                getRectangle(), object.getHexagon()
                        );
                    case RECTANGLE:
                        return Intersector.overlapConvexPolygons(
                                getRectangle(), object.getRectangle()
                        );
                    case POINT:
                        return getRectangle().contains(object.projectedPosition);
                }
            case POINT:
                switch (object.collisionType) {
                    case CIRCLE:
                        return object.getCircle().contains(projectedPosition);
                    case RECTANGLE:
                        return object.getRectangle().contains(projectedPosition);
                    case POINT:
                        return false;
                }
        }
        return false;
    }

    protected Circle getCircle() {
        return new Circle(projectedPosition, getProjectedRadius());
    }

    protected Polygon getRectangle() {
        float w = getWidth() * projectedScaleFactor / 2;
        float h = getHeight() * projectedScaleFactor / 2;
        Vector2 p1 = new Vector2(w, h);
        Vector2 p2 = new Vector2(w, -h);
        Vector2 p3 = new Vector2(-w, -h);
        Vector2 p4 = new Vector2(-w, h);
        Polygon rect = new Polygon(new float[]{
                p1.x, p1.y,
                p2.x, p2.y,
                p3.x, p3.y,
                p4.x, p4.y
        });
        rect.translate(projectedPosition.x, projectedPosition.y);
        rect.rotate(getRotation());
        return rect;
    }

    protected Polygon getHexagon() {
        float r = getProjectedRadius();
        float rot = MathUtils.PI / 3f;
        float[] vertices = new float[12];
        for (int i = 0; i < 6; i++) {
            Vector2 p = new Vector2(r, 0);
            p.rotateRad(rot * i);
            vertices[2 * i] = p.x;
            vertices[2 * i + 1] = p.y;
        }
        Polygon hex = new Polygon(vertices);
        hex.translate(projectedPosition.x, projectedPosition.y);
        return hex;
    }

    public boolean reportHit(Vector2 hitDirection) {
        return false;
    }

}
