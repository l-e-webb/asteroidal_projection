package com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior;

public enum FireRate {
    SLOW(new float[]{2}),
    FAST(new float[]{0.7f}),
    RAPID(new float[]{0.3f}),
    SLOW_BURST(new float[]{2.5f, 1, 1}),
    FAST_BURST(new float[]{1.5f, 0.5f, 0.5f}),
    SLOW_DOUBLE_SHOT(new float[]{2, 0.3f}),
    FAST_DOUBLE_SHOT(new float[]{1, 0.3f}),
    LONG_BURST(new float[]{3.5f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f}),
    EXTRA_LONG_BURST(new float[]{6f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f})
    ;

    float[] delays;

    FireRate(float[] delays) {
        this.delays = delays;
    }

    public float[] getDelays() {
        return delays;
    }
}
