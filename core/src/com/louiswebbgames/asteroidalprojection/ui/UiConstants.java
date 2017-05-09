package com.louiswebbgames.asteroidalprojection.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class UiConstants {

    private UiConstants() {}

    public static Label.LabelStyle basicLabelStyle;

    public static void init() {
        BitmapFont font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(1.5f);
        basicLabelStyle = new Label.LabelStyle(font, Color.WHITE);
    }

}
