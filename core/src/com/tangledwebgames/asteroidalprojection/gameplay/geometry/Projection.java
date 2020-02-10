package com.tangledwebgames.asteroidalprojection.gameplay.geometry;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.GameplayConstants;
import com.tangledwebgames.asteroidalprojection.utility.Log;

public class Projection {

    private Projection() {}

    static float pFactor;
    static float pFactorInverse;

    public static void init() {
        pFactor = MathUtils.log(
                GameplayConstants.LENGTH_MAPPED_TO_HALF_RADIUS / (GameplayConstants.LENGTH_MAPPED_TO_HALF_RADIUS + 1),
                0.5f
        );
        pFactorInverse = 1 / pFactor;
        Log.log("pFactor", pFactor, Log.LogLevel.INFO);
    }

    /**
     * Projects a point in the Euclidean plane to a point on the unit disk via the mapping:
     *
     * r , theta --> (r / (1 + r))^p , theta
     *
     * where r is the distance of the unprojected point from the origin, theta is the angle of
     * the point, and p > 0. The p value is a scale factor. Larger p values mean more of the plane
     * is thrust closer to the center of the disk, while small p values (between 0 and 1) mean the
     * projection quickly pushes points away from the origin to the far recesses of the disk.
     * @param point  Unprojected point.
     * @return       Image of projected point in disc.
     */
    public static Vector2 project(Vector2 point) {
        Vector2 projection = new Vector2(point);
        projection.setLength(projectLength(point.len()));
        return projection;
    }

    public static Vector2 project(float x, float y) {
        return project(new Vector2(x, y));
    }

    public static float projectLength(float length) {
        if (length == 0) return 0;
        length = Math.abs(length);
        return (float) Math.pow(length / (1 + length), pFactor);
    }

    public static Vector2 unproject(Vector2 point) {
        Vector2 unprojection = new Vector2(point);
        unprojection.setLength(getUnprojectedLength(point.len()));
        return unprojection;
    }

    public static Vector2 unproject(float x, float y) {
        return unproject(new Vector2(x, y));
    }

    public static float getUnprojectedLength(float length) {
        length = (float) Math.pow(length, pFactorInverse);
        return -length / (1 - length);
    }

    public static float getProjectedScale(float distance, float radius) {
        float l1 = projectLength(Math.abs(distance - radius));
        float l2 = projectLength(distance + radius);
        float r1 = distance >= radius ?
                (l2 - l1) * 0.5f : (l2 + l1) * 0.5f;
        float r2 = project(distance, radius).y;
        return (r1 + r2) * 0.5f / radius;
    }
}
