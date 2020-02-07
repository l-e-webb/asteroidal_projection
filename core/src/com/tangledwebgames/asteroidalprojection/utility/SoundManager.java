package com.tangledwebgames.asteroidalprojection.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private static Music backgroundMusic;
    private static Map<SoundEffect, Sound> soundEffects;
    private static Map<SoundEffect, Float> soundEffectMods;

    private static boolean sfxOn = true;
    private static boolean musicOn = true;
    private static float sfxLevel = 0.75f;
    private static float musicLevel = 0.75f;

    private SoundManager() {}

    public static void buttonSound() {}

    public static void playMusic() {
        if (musicOn && backgroundMusic != null && !backgroundMusic.isPlaying()) {
            //Not redundant the first time playMusic() is called.
            setMusicLevel(musicLevel);
            backgroundMusic.play();
        }
    }

    public static void stopMusic() {
        if (backgroundMusic != null) backgroundMusic.stop();
    }

    public static void playSoundEffect(SoundEffect effect, float volumeMod) {
        if (sfxOn && soundEffects.containsKey(effect)) {
            soundEffects.get(effect).play(sfxLevel * volumeMod * soundEffectMods.get(effect));
        }
    }

    public static void playSoundEffect(SoundEffect effect) {
        playSoundEffect(effect, 1);
    }

    public static void setSfxOn(boolean sfxOn) {
        SoundManager.sfxOn = sfxOn;
    }

    public static void setMusicOn(boolean musicOn) {
        SoundManager.musicOn = musicOn;
        if (!musicOn) stopMusic();
    }

    public static void setSfxLevel(float sfxLevel) {
        SoundManager.sfxLevel = MathUtils.clamp(sfxLevel, 0f, 1f);
    }

    public static void setMusicLevel(float musicLevel) {
        musicLevel = MathUtils.clamp(musicLevel, 0f, 1f);
        SoundManager.musicLevel = musicLevel;
        if (backgroundMusic != null) backgroundMusic.setVolume(musicLevel * Constants.MUSIC_VOLUME_MOD);
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

    public static void loadAudio() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(Constants.BACKGROUND_MUSIC));
        backgroundMusic.setLooping(true);
        soundEffects = new HashMap<>();
        soundEffects.put(
                SoundEffect.PLAYER_LASER,
                Gdx.audio.newSound(Gdx.files.internal(Constants.PLAYER_LASER_SOUND))
        );
        soundEffects.put(
                SoundEffect.PLAYER_PIERCING_LASER,
                Gdx.audio.newSound(Gdx.files.internal(Constants.PLAYER_PIERCING_LASER_SOUND))
        );
        soundEffects.put(
                SoundEffect.PLAYER_MISSILE,
                Gdx.audio.newSound(Gdx.files.internal(Constants.PLAYER_MISSILE_SOUND))
        );
        soundEffects.put(
                SoundEffect.ENEMY_LASER,
                Gdx.audio.newSound(Gdx.files.internal(Constants.ENEMY_LASER_SOUND))
        );
        soundEffects.put(
                SoundEffect.ENEMY_PIERCING_LASER,
                Gdx.audio.newSound(Gdx.files.internal(Constants.ENEMY_PIERCING_LASER_SOUND))
        );
        soundEffects.put(
                SoundEffect.ENEMY_ROUND_LASER,
                Gdx.audio.newSound(Gdx.files.internal(Constants.ENEMY_ROUND_LASER_SOUND))
        );
        soundEffects.put(
                SoundEffect.IMPACT,
                Gdx.audio.newSound(Gdx.files.internal(Constants.IMPACT_SOUND))
        );
        soundEffects.put(
                SoundEffect.EXPLOSION,
                Gdx.audio.newSound(Gdx.files.internal(Constants.EXPLOSION_SOUND))
        );
        soundEffects.put(
                SoundEffect.LARGE_EXPLOSION,
                Gdx.audio.newSound(Gdx.files.internal(Constants.LARGE_EXPLOSION_SOUND))
        );
        soundEffects.put(
                SoundEffect.POWERUP,
                Gdx.audio.newSound(Gdx.files.internal(Constants.POWERUP_SOUND))
        );
        soundEffects.put(
                SoundEffect.COIN,
                Gdx.audio.newSound(Gdx.files.internal(Constants.COIN_SOUND))
        );
        soundEffectMods = new HashMap<>();
        for (SoundEffect effect : SoundEffect.values()) {
            soundEffectMods.put(effect, 1f);
        }
        soundEffectMods.put(SoundEffect.EXPLOSION, Constants.EXPLOSION_SOUND_MOD);
        soundEffectMods.put(SoundEffect.POWERUP, Constants.POWERUP_SOUND_MOD);
    }

    public static void disposeAudio() {
        if (backgroundMusic != null) backgroundMusic.dispose();
    }

    public enum SoundEffect {
        PLAYER_LASER,
        PLAYER_PIERCING_LASER,
        PLAYER_MISSILE,
        ENEMY_LASER,
        ENEMY_PIERCING_LASER,
        ENEMY_ROUND_LASER,
        EXPLOSION,
        LARGE_EXPLOSION,
        IMPACT,
        POWERUP,
        COIN
    }
}
