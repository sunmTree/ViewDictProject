package com.sunm.widght.htextview;

import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * interface used in HTextView
 * Created by sunmeng on 2016/12/19.
 */

public interface IHtext {
    void init(HTextView hTextView, AttributeSet attrs, int defStyle);
    void animateText(CharSequence text);
    void clearText();
    void onDraw(Canvas canvas);
    void reset(CharSequence text);
    void setStartDelay(long delay);
}
