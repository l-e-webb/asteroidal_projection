package com.louiswebbgames.asteroidalprojection.utility;

public class SoundManager {

    private static boolean sfxOn = true;
    private static boolean musicOn = true;
    private static float sfxLevel = 1;
    private static float musicLevel = 1;

    private SoundManager() {}

    public static void buttonSound() {}

    public static void setSfxOn(boolean sfxOn) {
        SoundManager.sfxOn = sfxOn;
    }

    public static void setMusicOn(boolean musicOn) {
        SoundManager.musicOn = musicOn;
    }

    public static void setSfxLevel(float sfxLevel) {
        SoundManager.sfxLevel = sfxLevel;
    }

    public static void setMusicLevel(float musicLevel) {
        SoundManager.musicLevel = musicLevel;
    }

    public static boolean isSfxOn() {
        return sfxOn;
    }

    public static boolean isMusicOn() {
        return musicOn;
    }

    public static float getSfxLevel() {
        return sfxLevel;
    }

    public static float getMusicLevel() {
        return musicLevel;
    }
}
