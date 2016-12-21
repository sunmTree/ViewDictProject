package com.sunm.widght;

/**
 * Created by sunmeng on 2016/12/13.
 */

public class FontMeasurement {
    public int textSize;
    public float ascendPercentage;
    public float baseline;
    public int textHeight;

    public FontMeasurement() {
    }

    @Override
    public String toString() {
        return "FontMeasurement{" +
                "textSize=" + textSize +
                ", ascendPercentage=" + ascendPercentage +
                ", baseline=" + baseline +
                ", textHeight=" + textHeight +
                '}';
    }
}
