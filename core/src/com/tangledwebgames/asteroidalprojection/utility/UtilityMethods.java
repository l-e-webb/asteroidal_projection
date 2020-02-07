package com.tangledwebgames.asteroidalprojection.utility;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class UtilityMethods {

    public static boolean isWeb() {
        return Gdx.app.getType() == Application.ApplicationType.WebGL;
    }
}
