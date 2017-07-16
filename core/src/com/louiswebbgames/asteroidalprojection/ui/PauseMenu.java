package com.louiswebbgames.asteroidalprojection.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.gameplay.geometry.GridRenderer;
import com.louiswebbgames.asteroidalprojection.utility.Assets;
import com.louiswebbgames.asteroidalprojection.utility.SoundManager;

public class PauseMenu extends Table {

    PlayStage playStage;

    CheckBox gridOn;
    CheckBox musicOn;
    CheckBox sfxOn;
    Slider musicVolumeSlider;
    Slider sfxVolumeSlider;

    public PauseMenu(PlayStage playStage) {
        super();
        pad(UiConstants.PADDING);
        this.playStage = playStage;
        setBackground(Assets.instance.squareButtonDark);
        gridOn = new CheckBox(UiText.SHOW_GRID, UiConstants.checkBoxStyle);
        gridOn.getImageCell().spaceRight(UiConstants.CHECKBOX_RIGHT_PADDING);
        gridOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GridRenderer.gridOn = gridOn.isChecked();
                SoundManager.buttonSound();
            }
        });
        sfxOn = new CheckBox(UiText.SFX, UiConstants.checkBoxStyle);
        sfxOn.getImageCell().spaceRight(UiConstants.CHECKBOX_RIGHT_PADDING);
        sfxOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.setSfxOn(sfxOn.isChecked());
                SoundManager.buttonSound();
            }
        });
        musicOn = new CheckBox(UiText.MUSIC, UiConstants.checkBoxStyle);
        musicOn.getImageCell().spaceRight(UiConstants.CHECKBOX_RIGHT_PADDING);
        musicOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.setMusicOn(musicOn.isChecked());
                SoundManager.buttonSound();
            }
        });
        sfxVolumeSlider = new Slider(0, 1, 0.1f, false, UiConstants.sliderStyle);
        sfxVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.setSfxLevel(sfxVolumeSlider.getValue());
            }
        });
        musicVolumeSlider = new Slider(0, 1, 0.1f, false, UiConstants.sliderStyle);
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.setMusicLevel(musicVolumeSlider.getValue());
            }
        });
        TextButton resumeButton = new TextButton(UiText.RESUME, UiConstants.buttonStyle);
        resumeButton.pad(UiConstants.PADDING);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.buttonSound();
                unpause();
            }
        });
        
        add(new Label(UiText.PAUSED, UiConstants.basicLabelStyle)).colspan(2);
        row();
        add(gridOn).left().colspan(2).spaceTop(UiConstants.ROW_PADDING);
        row();
        add(musicOn).left().colspan(2).spaceTop(UiConstants.ROW_PADDING);
        row();
        add(new Label(UiText.MUSIC_LEVEL, UiConstants.basicLabelStyle)).left();
        add(musicVolumeSlider).growX();
        row();
        add(sfxOn).left().colspan(2).spaceTop(UiConstants.ROW_PADDING);
        row();
        add(new Label(UiText.SFX_LEVEL, UiConstants.basicLabelStyle)).left();
        add(sfxVolumeSlider).growX();
        row();
        add(resumeButton).colspan(2).spaceTop(UiConstants.ROW_PADDING);
    }

    public void setPaused(boolean paused) {
        if (paused) {
            pause();
        } else {
            unpause();
        }
    }

    public void pause() {
        playStage.setPaused(true);
        setSize(UiConstants.PAUSE_MENU_WIDTH, getPrefHeight());
        setPosition(
                getStage().getViewport().getWorldWidth() / 2,
                getStage().getViewport().getWorldHeight() / 2,
                Align.center
        );
        gridOn.setChecked(GridRenderer.gridOn);
        musicOn.setChecked(SoundManager.isMusicOn());
        sfxOn.setChecked(SoundManager.isSfxOn());
        musicVolumeSlider.setValue(SoundManager.getMusicLevel());
        sfxVolumeSlider.setValue(SoundManager.getSfxLevel());
        setVisible(true);
    }

    public void unpause() {
        playStage.setPaused(false);
        setVisible(false);
    }

}
