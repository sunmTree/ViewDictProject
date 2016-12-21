package com.sunm.widght;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunm.utils.CommonUtils;
import com.sunm.utils.FontUtils;
import com.sunm.vd.R;

import java.util.Calendar;

/**
 * Created by sunmeng on 2016/12/13.
 */

public class NormalAlphaDigitalClock extends LinearLayout implements IDigitalClock {

    private static final boolean DEBUG = true;
    private static final String TAG = "NormalAlphaDigitalClock";

    private TextView mTextDate;
    private FontMeasurement mFontMeasurement = null;

    private Paint mTextPaint = new Paint();
    private Typeface mTypeface = null;
    private String mTimeText = "";
    private int mDrawFromY = 0;
    //    private DeviceProfile mDeviceProfile = null;
    private int mPaddingLAR = 0;

    private int position = IDigitalClock.POSITION_LEFT;

    private int lastMeasuredWidth, lastMeasuredHeight;

    private Rect mRectDate = new Rect();
    private Rect mRectTime = new Rect();

    Object test_hook;
    private int mVerticalMargin;
    private int mDateExtraPadding;

    public NormalAlphaDigitalClock(Context context) {
        super(context);
    }

    public NormalAlphaDigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init(getContext());
    }

    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        mTextDate.setTextColor(color);
        invalidate();
    }

    public Rect getDateRect() {
        return mRectDate;
    }

    public Rect getTimeRect() {
        return mRectTime;
    }

    private void init(Context context) {
        mDateExtraPadding = CommonUtils.dip2px(context, 4);
        mTextDate = (TextView) findViewById(R.id.digital_date);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setSubpixelText(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setShadowLayer(6, 0, 2, 0x40000000);
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        int measuredWidth = getMeasuredWidth();  // 测量的宽度
        int measuredHeight = getMeasuredHeight();  // 测量的高度
        int textDateMeasuredWidth = mTextDate.getMeasuredWidth();  // 控件的宽度
        int textDateMeasuredHeight = mTextDate.getMeasuredHeight(); // 控件的高度

        int newPaddingLAR = measuredWidth / 6;

        if (mPaddingLAR != newPaddingLAR) {
            mPaddingLAR = newPaddingLAR;
            updateDatePadding();  // 在切换时间的位置的时候，不伦怎么动，它总是和左右边界有一定的距离，这就是onLayout起到的作用
        }

        int timeMaxHeight = measuredHeight - textDateMeasuredHeight; //  ？？？？？？
        int timeMaxWidth = measuredWidth - mPaddingLAR;

        if ((timeMaxHeight > 0 && timeMaxWidth > 0)
                && (timeMaxHeight != lastMeasuredHeight || timeMaxWidth != lastMeasuredWidth)) {
            lastMeasuredHeight = timeMaxHeight;
            lastMeasuredWidth = timeMaxWidth;
            mFontMeasurement = FontUtils.measureText(getContext(), timeMaxWidth,
                    timeMaxHeight, null);
            if (mFontMeasurement.textSize > 0) {
                mTextPaint.setTextSize(mFontMeasurement.textSize);
            }
        }

        if (mFontMeasurement != null) {
            int maxHeight = textDateMeasuredHeight + mFontMeasurement.textHeight;
            mVerticalMargin = (int) ((measuredHeight - maxHeight) * 0.2f);
            mDrawFromY = (measuredHeight - maxHeight - mVerticalMargin) / 2;
            int top = mDrawFromY + mFontMeasurement.textHeight + mVerticalMargin;
            mTextDate.layout(0, top, textDateMeasuredWidth, top + textDateMeasuredHeight);
            mRectDate.left = 0;
            mRectDate.top = top;
            mRectDate.right = textDateMeasuredWidth;
            mRectDate.bottom = top + textDateMeasuredHeight;
            mRectTime.top = mDrawFromY;
            mRectTime.left = 0;
            mRectTime.right = getMeasuredWidth();
            mRectTime.bottom = mDrawFromY + mFontMeasurement.textHeight;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null != mFontMeasurement && mTextPaint.getTextSize() > 0) {
            float textX = 0;
            float textY = mFontMeasurement.baseline + mDrawFromY;
            float textWidth = mTextPaint.measureText(mTimeText);
            switch (position) {
                case IDigitalClock.POSITION_LEFT:
                    mTextPaint.setTextAlign(Paint.Align.CENTER);
                    textX = mPaddingLAR + textWidth / 2f;
                    break;
                case IDigitalClock.POSITION_RIGHT:
                    mTextPaint.setTextAlign(Paint.Align.CENTER);
                    textX = getMeasuredWidth() - textWidth / 2f - mPaddingLAR;
                    break;
                case IDigitalClock.POSITION_CENTER:
                default:
                    textX = getMeasuredWidth() / 2;
                    break;
            }
            canvas.drawText(mTimeText, textX, textY, mTextPaint);
        }
    }

    private void updateDatePadding() {
        int padding = mDateExtraPadding + mPaddingLAR;
        if (null != mTextDate) {
            switch (position) {
                case IDigitalClock.POSITION_LEFT:
                    mTextDate.setPadding(padding, 0, 0, 0);
                    break;
                case IDigitalClock.POSITION_RIGHT:
                    mTextDate.setPadding(0, 0, padding, 0);
                    break;
                default:
                    mTextDate.setPadding(0, 0, 0, 0);
            }
        }
    }


    @Override
    public void updateTime(Calendar calendar, String date) {
        boolean is24Hour = DateFormat.is24HourFormat(getContext().getApplicationContext());
        int hour;
        if (is24Hour) {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
        } else {
            hour = calendar.get(Calendar.HOUR);
            if (hour == 0) {
                hour = 12;
            }
        }


        int minute = calendar.get(Calendar.MINUTE);

        updateTimeView(hour / 10, hour % 10, minute / 10, minute % 10, date, is24Hour);
    }

    private void updateTimeView(int hourTens, int hourDigit, int minuteTens, int minuteDigit, String date,
                                boolean is24Hour) {
        mTextDate.setText(date);
        if (!is24Hour && hourTens == 0) {
            mTimeText = hourDigit + ":" + minuteTens + "" + minuteDigit;
        } else {
            mTimeText = hourTens + "" + hourDigit + ":" + minuteTens + "" + minuteDigit;
        }

        invalidate();
    }

    @Override
    public void updateTheme() {

    }

    @Override
    public void updatePosition(int position) {
        this.position = position;
        if (null != mTextDate) {
            switch (position) {
                case IDigitalClock.POSITION_LEFT:
                    mTextDate.setGravity(Gravity.LEFT);
                    break;
                case IDigitalClock.POSITION_RIGHT:
                    mTextDate.setGravity(Gravity.RIGHT);
                    break;
                default:
                    mTextDate.setGravity(Gravity.CENTER);
            }
        }

        updateDatePadding();
        invalidate();
    }
}
