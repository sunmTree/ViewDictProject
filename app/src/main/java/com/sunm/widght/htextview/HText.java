package com.sunm.widght.htextview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import com.sunm.globle.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunmeng on 2016/12/19.
 */

public abstract class HText implements IHtext{

    public static final int LENGTH_MAX = 127;   // 字符最大长度
    protected Paint mPaint, mOldPaint;

    /***the gap between characters*/
    protected  float[] gaps = new float[LENGTH_MAX];
    protected  float[] oldGaps = new float[LENGTH_MAX];

    /***current text size*/
    protected float mTextSize;

    protected CharSequence mText;
    protected CharSequence mOldText;

    protected List<CharacterDiffResult> differentList = new ArrayList<>();

    protected float oldStartX = 0; // 原来的字符串开始画X位置
    protected float startX    = 0; // 新的字符串开始画的x位置
    protected float startY    = 0; // 字符串开始画Y， baseline

    protected HTextView mHTextView;
    protected long startDelay;

    @Override
    public void init(HTextView hTextView, AttributeSet attrs, int defStyle) {
        mHTextView = hTextView;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mHTextView.getCurrentTextColor());
        mPaint.setStyle(Paint.Style.FILL);

        mOldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOldPaint.setColor(mHTextView.getCurrentTextColor());
        mOldPaint.setStyle(Paint.Style.FILL);

        mText = mHTextView.getText();
        mOldText = mHTextView.getText();

        mTextSize = mHTextView.getTextSize();
        initVariables();
        mHTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHTextView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                prepareAnimate();
            }
        });
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawFrame(canvas);
    }

    @Override
    public void animateText(CharSequence text) {
        if (TextUtils.isEmpty(text)){
            text = "";
        }

        if (text.length() > LENGTH_MAX){
            text = text.subSequence(0,LENGTH_MAX);
        }

        mHTextView.setText(text);
        mOldText = mText;
        mText = text;
        prepareAnimate();
        animatePrepare(text);
        animateStart(text);
    }

    private void prepareAnimate(){
        mTextSize = mHTextView.getTextSize();

        mPaint.setTextSize(mTextSize);
        for (int i = 0; i < mText.length(); i++){
            gaps[i] = mPaint.measureText(String.valueOf(mText.charAt(i)));
        }

        mOldPaint.setTextSize(mTextSize);
        for (int i = 0; i < mOldText.length(); i++){
            oldGaps[i] = mOldPaint.measureText(String.valueOf(mOldText.charAt(i)));
        }
        String logMsg = "HText prepareAnimate  gaps: "+gaps.toString()+" oldGaps: "+oldGaps.toString();
        Config.i(logMsg);

        oldStartX = 0;
        startX = 0;
        startY = mHTextView.getBaseline();
        differentList.clear();
        differentList.addAll(CharacterUtils.diff(mOldText,mText));
    }

    public void reset(CharSequence text){
        animatePrepare(text);
        mHTextView.invalidate();
    }

    /**
     * 类被实例化时初始化
     */
    protected abstract void initVariables();

    /**
     * 具体实现动画
     * @param text
     */
    protected abstract void animateStart(CharSequence text);

    /**
     * 每次动画前初始化调用
     * @param text
     */
    protected abstract void animatePrepare(CharSequence text);

    /**
     * 动画每次刷新界面时调用
     * @param canvas
     */
    protected abstract void drawFrame(Canvas canvas);

    @Override
    public void setStartDelay(long startDelay) {
        this.startDelay = startDelay;
    }
}
