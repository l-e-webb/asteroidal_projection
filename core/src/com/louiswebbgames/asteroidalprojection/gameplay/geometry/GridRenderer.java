package com.louiswebbgames.asteroidalprojection.gameplay.geometry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.utility.ShapeRenderRequest;

public class GridRenderer implements ShapeRenderRequest {

    float gridWidth;
    float dotRadius;
    Vector2 offset;

    public GridRenderer(float gridWidth, float dotRadius) {
        this.gridWidth = gridWidth;
        this.dotRadius = dotRadius;
        offset = new Vector2();
    }

    @Override
    public void draw(ShapeRenderer renderer) {
        renderer.setColor(Color.WHITE);
        renderer.set(ShapeRenderer.ShapeType.Point);
        int dotsPerRow = (int) (GameplayConstants.HORIZON * 2 / gridWidth);
        for (int i = 0; i < dotsPerRow; i++) {
            for (int j = 0; j < dotsPerRow; j++) {
                Vector2 position = Projection.project(
                        new Vector2(
                                i * gridWidth - GameplayConstants.HORIZON,
                                j * gridWidth - GameplayConstants.HORIZON
                        ).add(offset)
                );
                renderer.point(position.x, position.y, 0);
            }
        }
    }

    public void setGridWidth(float gridWidth) {
        this.gridWidth = gridWidth;
    }

    public void setDotRadius(float dotRadius) {
        this.dotRadius = dotRadius;
    }

    public void setOffset(Vector2 offset) {
        this.offset.x = offset.x % gridWidth;
        this.offset.y = offset.y % gridWidth;
    }
}