package com.louiswebbgames.asteroidalprojection.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.utility.SoundManager;

public class GameOverMenu extends Table {

    public GameOverMenu(PlayStage playStage) {
        add(new Label(UiText.GAME_OVER, UiConstants.basicLabelStyle));
        row();
        TextButton playAgainButton = new TextButton(UiText.PLAY_AGAIN, UiConstants.buttonStyle);
        playAgainButton.pad(UiConstants.PADDING);
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playStage.initGame();
                SoundManager.buttonSound();
                GameOverMenu.this.deactivate();
            }
        });
        row();
        add(playAgainButton).spaceTop(UiConstants.ROW_PADDING);
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
