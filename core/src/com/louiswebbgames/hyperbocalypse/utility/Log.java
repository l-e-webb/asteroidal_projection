package com.louiswebbgames.hyperbocalypse.utility;

import com.badlogic.gdx.Gdx;

public class Log {

    public static void log(String message) {
        Log.log("LOG", message);
    }

    public static void log(String tag, String message) {
        Gdx.app.log(tag, message);
    }
}
