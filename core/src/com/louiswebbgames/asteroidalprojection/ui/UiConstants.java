package com.louiswebbgames.asteroidalprojection.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.louiswebbgames.asteroidalprojection.utility.Assets;

public class UiConstants {

    private UiConstants() {}

    public static Label.LabelStyle basicLabelStyle;
    public static Label.LabelStyle titleLabelStyle;
    public static TextButton.TextButtonStyle buttonStyle;
    public static CheckBox.CheckBoxStyle checkBoxStyle;
    public static Slider.SliderStyle sliderStyle;

    public static final float PAUSE_MENU_WIDTH = 400f;

    public static final float FONT_SCALE = 1;
    public static final float TITLE_FONT_SCALE = 1;

    public static final float PADDING = 12f;
    public static final float ICON_SIZE = 48f;
    public static final float CHECKBOX_RIGHT_PADDING = 10f;
    public static final int SLIDER_KNOB_WIDTH = 15;
    public static final int SLIDER_KNOB_HEIGHT = 20;
    public static final float ROW_PADDING = 15f;
    public static final float BELOW_TITLE_SPACING = 100f;
    public static final float BUTTON_WIDTH = 250f;

    public static final float HEALTH_BAR_WIDTH = 20f;
    public static final float[] HEALTH_BAR_HEIGHTS = new float[]{170f, 120f, 85f, 50f, 25f};
    public static final Color HEALTH_BAR_TINT = Color.GREEN;

    public static final int SQUARE_BUTTON_9PATCH_OFFSET = 5;

    public static final Color TEXT_COLOR = new Color(1f, 1f, 1f, 1f);
    public static final Color TEXT_COLOR_INVERTED = new Color(0.1f, 0.1f, 0.1f, 1f);

    public static void init() {
        BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(FONT_SCALE);
        BitmapFont titleFont = new BitmapFont(Gdx.files.internal("titleFont.fnt"), false);
        titleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        titleFont.getData().setScale(TITLE_FONT_SCALE);
        basicLabelStyle = new Label.LabelStyle(font, TEXT_COLOR);
        titleLabelStyle = new Label.LabelStyle(titleFont, TEXT_COLOR);
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = Assets.instance.squareButtonDark;
        buttonStyle.over = Assets.instance.squareButtonLight;
        buttonStyle.fontColor = TEXT_COLOR;
        buttonStyle.overFontColor = TEXT_COLOR_INVERTED;
        buttonStyle.font = font;
        checkBoxStyle = new CheckBox.CheckBoxStyle(
                Assets.instance.checkboxUnchecked,
                Assets.instance.checkboxChecked,
                font,
                TEXT_COLOR
        );
        sliderStyle = new Slider.SliderStyle(Assets.instance.slider, Assets.instance.sliderKnob);
    }

}
