package com.sunm.widght.htextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.sunm.vd.R;

/**
 * Created by sunmeng on 2016/12/19.
 */

public class HTextView extends TextView {

    private IHtext mIHtext = new ScaleText();
    private AttributeSet attrs;
    private int defStyle;
    private boolean animationEnabled = true;

    public HTextView(Context context) {
        super(context);
        init(null,0);
    }

    public HTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public HTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        this.attrs = attrs;
        this.defStyle = defStyleAttr;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HTextView);
        int animateType = typedArray.getInt(R.styleable.HTextView_animateType, 0);
        typedArray.recycle();
        switch (animateType){
            case 0:
                mIHtext = new ScaleText();
                break;
        }
        initHText(attrs,defStyleAttr);
    }

    private void initHText(AttributeSet attrs, int defStyleAttr) {
        mIHtext.init(this,attrs,defStyleAttr);
    }

    public void animateText(CharSequence text){
        if (animationEnabled){
            mIHtext.animateText(text);
        }else {
            setText(text);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (animationEnabled){
            mIHtext.onDraw(canvas);
        }else {
            super.onDraw(canvas);
        }
    }

    public void reset(CharSequence text){
        mIHtext.reset(text);
    }

    public void setAnimateType(HTextViewType type){
        switch (type){
            case SCALE:
                mIHtext = new ScaleText();
                break;
        }

        initHText(attrs,defStyle);
    }

    public void clearText(){
        mIHtext.clearText();
    }

    public void setStartDelay(long delay) {
        mIHtext.setStartDelay(delay);
    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    public void setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
    }

    public void invalidateSelfAndParent() {
        invalidate();
        ViewParent parent = getParent();
        if (parent != null) {
            ((ViewGroup) parent).invalidate();
        }
    }
}
