package com.tangledwebgames.asteroidalprojection.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.tangledwebgames.asteroidalprojection.PlayScreen;
import com.tangledwebgames.asteroidalprojection.utility.SoundManager;
import com.tangledwebgames.asteroidalprojection.utility.UtilityMethods;

public class MainMenu extends Table {

    public MainMenu(PlayScreen playScreen) {
        add(new Label(UiText.TITLE, UiConstants.titleLabelStyle))
                .spaceBottom(UiConstants.BELOW_TITLE_SPACING)
                .center();
        row();
        TextButton playButton = new TextButton(UiText.PLAY, UiConstants.buttonStyle);
        playButton.pad(UiConstants.PADDING);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.buttonSound();
                MainMenu.this.deactivate();
                playScreen.initGame();
            }
        });
        row();
        add(playButton).minWidth(UiConstants.BUTTON_WIDTH);
        row();
        TextButton settingsButton = new TextButton(UiText.SETTINGS, UiConstants.buttonStyle);
        settingsButton.pad(UiConstants.PADDING);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.buttonSound();
                MainMenu.this.deactivate();
                playScreen.togglePause();
            }
        });
        add(settingsButton).center().spaceTop(UiConstants.ROW_PADDING).minWidth(UiConstants.BUTTON_WIDTH);
        /*
        row();
        TextButton aboutButton = new TextButton(UiText.ABOUT, UiConstants.buttonStyle);
        aboutButton.pad(UiConstants.PADDING);
        //TODO: add listener.
        add(aboutButton).center().spaceTop(UiConstants.ROW_PADDING).minWidth(UiConstants.BUTTON_WIDTH);
        */
        if (!UtilityMethods.isWeb()) {
            row();
            TextButton quitButton = new TextButton(UiText.QUIT, UiConstants.buttonStyle);
            quitButton.pad(UiConstants.PADDING);
            quitButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.exit();
                }
            });
            add(quitButton).spaceTop(UiConstants.ROW_PADDING).minWidth(UiConstants.BUTTON_WIDTH);
        }
        setSize(getPrefWidth(), getPrefHeight());
    }

    public void activate() {
        setPosition(
                getStage().getViewport().getWorldWidth() / 2,
                getStage().getViewport().getWorldHeight() / 2,
                Align.center
        );
        setVisible(true);
    }

    public void deactivate() {
        setVisible(false);
    }
}
