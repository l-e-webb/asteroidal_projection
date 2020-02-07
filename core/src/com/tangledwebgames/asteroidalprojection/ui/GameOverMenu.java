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

public class GameOverMenu extends Table {

    public GameOverMenu(PlayScreen playScreen) {
        add(new Label(UiText.GAME_OVER, UiConstants.basicLabelStyle));
        row();
        TextButton playAgainButton = new TextButton(UiText.PLAY_AGAIN, UiConstants.buttonStyle);
        playAgainButton.pad(UiConstants.PADDING);
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.buttonSound();
                GameOverMenu.this.deactivate();
                playScreen.initGame();
            }
        });
        add(playAgainButton).spaceTop(UiConstants.ROW_PADDING).minWidth(UiConstants.BUTTON_WIDTH);
        row();
        TextButton returnToTitleButton = new TextButton(UiText.RETURN_TO_TITLE, UiConstants.buttonStyle);
        returnToTitleButton.pad(UiConstants.PADDING);
        returnToTitleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.buttonSound();
                GameOverMenu.this.deactivate();
                playScreen.init();
            }
        });
        add(returnToTitleButton).spaceTop(UiConstants.ROW_PADDING).minWidth(UiConstants.BUTTON_WIDTH);
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
            add(quitButton).spaceTop(UiConstants.PADDING).minWidth(UiConstants.BUTTON_WIDTH);
        }
        setVisible(false);
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
