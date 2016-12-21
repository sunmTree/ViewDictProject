package com.sunm.widght;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import com.sunm.bean.Point;

/**
 * Created by sunmeng on 2016/11/18.
 */

public class TouchScrollView extends View {

    private Point mPoint;
    private Paint mPaint;

    private Scroller mScroller;

    private GestureDetector mGestureDetector;

    public TouchScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onContextClick(MotionEvent e) {
            Log.i("TAG", "onContextClick event: " + e.getAction() + " position [" + e.getX() + "," + e.getY() + "]");
            return super.onContextClick(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i("TAG", "onFling velocity" + velocityX + " velocityY " + velocityY);
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    private void initData(Context con) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#99ff8960"));
        mPaint.setTextSize(30);
        mPaint.setStyle(Paint.Style.STROKE);

        mPoint = new Point(30.0f, 30.0f);

        mScroller = new Scroller(con);

        mGestureDetector = new GestureDetector(con, mGestureListener);
    }

    public TouchScrollView(Context context) {
        super(context);
        initData(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawText("Hello, world", mPoint.getX(), mPoint.getY(), mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        VelocityTracker tracker = VelocityTracker.obtain();
        tracker.addMovement(event);

        mGestureDetector.onTouchEvent(event);

        float firstX = 0;
        float firstY = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstX = event.getX();
                firstY = event.getY();
                Log.d("TAG", "ACTION_DOWN firstX: " + firstX);
                break;
            case MotionEvent.ACTION_MOVE:
//                mPoint = new Point(event.getX(),event.getY());
//                invalidate();
                int offSetX = (int) (event.getX() - firstX);
                int offSetY = (int) (event.getY() - firstY);
                Log.d("TAG", "ACTION_MOVE offSetX: " + offSetX + "  offSetY: " + offSetY + " firstX:" + firstX + " x: " + event.getX());
//                offsetLeftAndRight(offSetX);
//                offsetTopAndBottom(offSetY);
//                layout(getLeft()+offSetX,getTop()+offSetY,getRight()+offSetX,getBottom()+offSetY);
                ((View) getParent()).scrollBy(-offSetX, -offSetY);
                break;
        }

        tracker.clear();
        tracker.recycle();
        return super.onTouchEvent(event);
    }

    public void smoothScrollTo(int destX, int time) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        //1000秒内滑向destX
        mScroller.startScroll(scrollX, 0, delta, 0, time);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            ((View) getParent()).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
