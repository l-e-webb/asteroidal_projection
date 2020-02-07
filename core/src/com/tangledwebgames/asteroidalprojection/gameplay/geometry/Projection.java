package com.tangledwebgames.asteroidalprojection.gameplay.geometry;

import com.badlogic.gdx.math.Vector2;

public class Projection {

    static float projectionScale = 1;

    private Projection() {}

    public static float getProjectionScale() {
        return projectionScale;
    }

    public static void setProjectionScale(float projectionScale) {
        Projection.projectionScale = projectionScale;
    }

    /**
     * Projects a point in euclidean space into a point in the hyperbolic projection, given by
     * the mapping:
     *
     * r * e ^ (i * theta) --> s * r / (1 + r) * e ^ (i * theta)
     *
     * where r is the distance of the unprojected point from the origin, theta is the angle of the
     * point, and s is the scale factor.
     * @param point  Unprojected point.
     * @return       Image of projected point in hyperbolic disc.
     */
    public static Vector2 project(Vector2 point) {
        Vector2 projection = new Vector2(1, 0);
        projection.setAngle(point.angle());
        projection.setLength(
                projectionScale * point.len() / (1 + point.len())
        );
        return projection;
    }

    public static Vector2 project(float x, float y) {
        return project(new Vector2(x, y));
    }

    public static Vector2 unproject(Vector2 point) {
        Vector2 unprojection = new Vector2(1, 0);
        unprojection.setAngle(point.angle());
        unprojection.setLength(
                (1 / projectionScale) * -point.len() / (1 - point.len())
        );
        return unprojection;
    }

    public static Vector2 unproject(float x, float y) {
        return unproject(new Vector2(x, y));
    }
}
