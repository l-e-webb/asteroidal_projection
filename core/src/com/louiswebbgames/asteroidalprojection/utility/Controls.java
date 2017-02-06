package com.louiswebbgames.asteroidalprojection.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Controls {

    public static boolean left() {
        return Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
    }

    public static boolean right() {
        return Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    public static boolean up() {
        return Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
    }

    public static boolean down() {
        return Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
    }

    public static boolean fire() {
        return Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isKeyPressed(Input.Keys.Z);
    }

    public static boolean secondaryFire() {
        return Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
    }

    public static boolean confirm() {
        return Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isButtonPressed(Input.Keys.ENTER);
    }

    public static boolean cancel() {
        return Gdx.input.isKeyPressed(Input.Keys.ESCAPE);
    }

    public static boolean pause() {
        return Gdx.input.isKeyPressed(Input.Keys.ESCAPE);
    }
}
