package com.sunm.widght.htextview;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.sunm.globle.Config;

/**
 * Created by sunmeng on 2016/12/19.
 */

public class ScaleText extends HText {

    public static final AccelerateDecelerateInterpolator INTERPOLATOR = new AccelerateDecelerateInterpolator();
    float mostCount = 20;
    float charTime = 340;
    private long duration;
    private float progress;
    private ValueAnimator valueAnimator;
    public static final String EMPTY_TEXT = "";
    private ValueAnimator.AnimatorUpdateListener updateListener;

    @Override
    protected void initVariables() {

    }

    @Override
    protected void animateStart(CharSequence text) {
        int length = mText.length();
        length = length <= 0 ? 1 : length;
        // 计算动画总时间
        duration = (long) (charTime + charTime / mostCount * (length - 1));

        cancelCurrentAnimator();

        valueAnimator = ValueAnimator.ofFloat(0, duration).setDuration(duration);
        valueAnimator.setStartDelay(startDelay);
        valueAnimator.setInterpolator(INTERPOLATOR);
        if (null == updateListener) {
            updateListener = new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    progress = (float) animation.getAnimatedValue();
                    mHTextView.invalidateSelfAndParent();
                    if (progress >= 1f) {
                        valueAnimator = null;
                    }
                }
            };
        }
        valueAnimator.addUpdateListener(updateListener);
        valueAnimator.start();
    }

    private void cancelCurrentAnimator() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        progress = 0;
    }

    @Override
    protected void animatePrepare(CharSequence text) {

    }

    @Override
    protected void drawFrame(Canvas canvas) {

        float offset = startX;
        float oldOffset = oldStartX;

        int oldTextLength = mOldText.length();
        int maxLength = Math.max(mText.length(), oldTextLength);

        for (int i = 0; i < maxLength; i++) {

            // draw old text
            if (i < oldTextLength) {

                float percent = progress / duration;
                int move = CharacterUtils.needMove(i, differentList);
                String text = String.valueOf(mOldText.charAt(i));
                if (move != -1) {
                    mOldPaint.setTextSize(mTextSize);
                    mOldPaint.setAlpha(255);

                    float p = percent * 2f;
                    p = p > 1 ? 1 : p;
                    float distX = CharacterUtils.getOffset(i, move, p, startX, oldStartX, gaps, oldGaps);
                    canvas.drawText(text, 0, 1, distX, startY, mOldPaint);
                } else {
                    mOldPaint.setAlpha((int) ((1 - percent) * 255));
                    mOldPaint.setTextSize(mTextSize * (1 - percent));
                    float width = mOldPaint.measureText(text);
                    canvas.drawText(text, 0, 1, oldOffset + (oldGaps[i] - width) / 2, startY, mOldPaint);
                }
                oldOffset += oldGaps[i];
            }

            // draw new text
            if (i < mText.length()) {
                if (!CharacterUtils.stayHere(i, differentList)) {
                    int alpha = (int) (255f / charTime * (progress - charTime * i / mostCount));
                    if (alpha > 255) {
                        alpha = 255;
                    }
                    if (alpha < 0) {
                        alpha = 0;
                    }

                    float size = mTextSize * 1f / charTime * (progress - charTime * i / mostCount);
                    if (size > mTextSize) {
                        size = mTextSize;
                    }
                    if (size < 0) {
                        size = 0;
                    }

                    mPaint.setAlpha(alpha);
                    mPaint.setTextSize(size);

                    String text = mText.charAt(i) + "";
                    float width = mPaint.measureText(text);
                    canvas.drawText(text, 0, 1, offset + (gaps[i] - width) / 2, startY, mPaint);
                }
            }

            offset += gaps[i];
        }
    }

    @Override
    public void clearText() {
        cancelCurrentAnimator();
        mHTextView.setText(EMPTY_TEXT);
        mOldText = mText = EMPTY_TEXT;
    }
}
