package com.louiswebbgames.asteroidalprojection.utility;

import com.badlogic.gdx.Gdx;

public class Log {

    public static LogLevel level = LogLevel.DEBUG;

    public static void log(String message) {
        log(message, LogLevel.INFO);
    }

    public static void log(String message, LogLevel level) {
        log("LOG", message, level);
    }

    public static void log(String tag, String message) {
        log(tag, message, LogLevel.INFO);
    }

    public static void log(String tag, String message, LogLevel level) {
        if (level.priority >= Log.level.priority) {
            Gdx.app.log(tag, message);
        }
    }

    public enum LogLevel {
        INFO(0),
        DEBUG(1),
        ERROR(2)
        ;

        int priority;

        LogLevel(int priority) {
            this.priority = priority;
        }
    }
}
