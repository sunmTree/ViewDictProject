package com.sunm.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.Log;

import com.sunm.widght.FontMeasurement;

/**
 * Created by sunmeng on 2016/12/13.
 */

public class FontUtils {
    private static final String fatClock = "69:88";
    private static final char[] testString = new char[]{':', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    public static final boolean USE_MONOSPACE = false;
    private static final Paint paint = new Paint();

    public FontUtils() {
    }

    public static FontMeasurement measureText(Context context, int width, int height, Typeface typeface) {
        long startTime = System.currentTimeMillis();
        Log.i("AA", "width = " + width + ", " + height);
        paint.setTypeface(typeface);
        paint.setTextSize((float)height);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        int estimatedCharWidth = calculateMaxCharWidth();
        int estimatedCharHeight = calculateCharHeight(fontMetrics);
        Bitmap bitmap = Bitmap.createBitmap(estimatedCharWidth, (int)((float)estimatedCharHeight * 1.5F), Bitmap.Config.ARGB_8888);
        Log.i("AA", "bitmap = " + bitmap.getWidth() + ", " + bitmap.getHeight());
        Log.i("AA", "leading = " + fontMetrics.leading + ", bottom = " + fontMetrics.bottom + ", as = " + fontMetrics.ascent + ", des = " + fontMetrics.descent + ", top = " + fontMetrics.top);
        Canvas canvas = new Canvas(bitmap);
        drawText(testString, canvas, paint);
        int firstPixelY = getFirstPixelY(bitmap, estimatedCharHeight);
        int lastPixelY = getLastPixelY(bitmap, bitmap.getHeight() - 1);
        Log.i("AA", "firstPixelY = " + firstPixelY + ", " + lastPixelY);
        float percent = 1.0F - (float)firstPixelY / -fontMetrics.ascent;
        FontMeasurement result = new FontMeasurement();
        float textHeight = (float)(lastPixelY - firstPixelY);
        int tempTextSize = calculateTextSize(width, height, "69:88", paint, textHeight);
        result.textSize = tempTextSize;
        result.ascendPercentage = percent;
        result.textHeight = Math.round(textHeight);
        Log.i("AA", "textHeight = " + textHeight);
        paint.setTextSize((float)result.textSize);
        result.baseline = result.ascendPercentage * -paint.getFontMetrics().ascent;
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        drawText(testString, canvas, paint);
        firstPixelY = getFirstPixelY(bitmap, estimatedCharHeight);
        lastPixelY = getLastPixelY(bitmap, bitmap.getHeight() - 1);
        Log.i("AA", "firstPixelY = " + firstPixelY + ", " + lastPixelY);
        textHeight = (float)(lastPixelY - firstPixelY);
        result.textHeight = Math.round(textHeight * 1.04F);
        Log.i("AA", "textHeight = " + textHeight);
        Log.i("AA", "COST = " + (System.currentTimeMillis() - startTime) + ", result = " + result);
        BitmapUtils.recycle(bitmap);
        return result;
    }

    private static int calculateCharHeight(Paint.FontMetrics fontMetrics) {
        return Math.round(Math.max(-fontMetrics.ascent, -fontMetrics.top) + fontMetrics.bottom + 0.51F);
    }

    private static int calculateMaxCharWidth() {
        float maxCharWidth = 0.0F;

        for(int i = 0; i < testString.length; ++i) {
            float charWidth = paint.measureText(testString, i, 1);
            if(charWidth > maxCharWidth) {
                maxCharWidth = charWidth;
            }
        }

        return Math.round(maxCharWidth + 0.51F);
    }

    private static int calculateTextSize(int width, int height, String fatClock, Paint paint, float textHeight) {
        int tempTextSize = (int)((float)height * ((float)height / textHeight) * 0.97F);

        for(int measuredWidth = (int)paint.measureText(fatClock); measuredWidth > width; measuredWidth = (int)paint.measureText(fatClock)) {
            float scale = (float)width / (float)measuredWidth;
            tempTextSize = (int)((float)tempTextSize * scale);
            paint.setTextSize((float)tempTextSize);
        }

        return tempTextSize;
    }

    private static int getLastPixelY(Bitmap bitmap, int lastPixelY) {
        int width = bitmap.getWidth();
        int[] rowPixels = new int[width];

        for(int y = lastPixelY; y >= 0; --y) {
            bitmap.getPixels(rowPixels, 0, width, 0, y, width, 1);

            for(int x = 0; x < width; ++x) {
                int color = rowPixels[x];
                if(color != 0) {
                    lastPixelY = y;
                    return lastPixelY;
                }
            }
        }

        return lastPixelY;
    }

    private static int getFirstPixelY(Bitmap bitmap, int endPointY) {
        int firstPixelY = 0;
        int width = bitmap.getWidth();
        int[] rowPixels = new int[width];

        for(int y = 0; y < endPointY; ++y) {
            bitmap.getPixels(rowPixels, 0, width, 0, y, width, 1);

            for(int x = 0; x < width; ++x) {
                int color = rowPixels[x];
                if(color != 0) {
                    firstPixelY = y;
                    return firstPixelY;
                }
            }
        }

        return firstPixelY;
    }

    private static void drawText(char[] testString, Canvas canvas, Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textX = 0.0F;
        float textY = -fm.ascent;
        int length = testString.length;

        for(int i = 0; i < length; ++i) {
            canvas.drawText(testString, i, 1, textX, textY, paint);
        }

    }

    static {
        paint.setColor(-1);
        paint.setTextAlign(Paint.Align.LEFT);
    }
}
