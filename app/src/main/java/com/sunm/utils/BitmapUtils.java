package com.sunm.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

/**
 * Created by sunmeng on 2016/12/13.
 */

public class BitmapUtils {
    private static final String TAG = "fw.utils.bitmap";

    public BitmapUtils() {
    }

    public static void recycle(Bitmap tempBitmap) {
        if(tempBitmap != null && !tempBitmap.isRecycled()) {
            tempBitmap.recycle();
        }

    }


    public static int calculateInSampleSize(int width, int height, int reqWidth, int reqHeight) {
        boolean DEFAULT_SIZE = true;
        int inSampleSize = 1;
        if(reqHeight != 0 && reqWidth != 0) {
            if(height > reqHeight || width > reqWidth) {
                if(Math.min((float)height / (float)reqHeight, (float)width / (float)reqWidth) < 2.0F) {
                    return 1;
                }

                if(width > height) {
                    inSampleSize = Math.round((float)height / (float)reqHeight);
                } else {
                    inSampleSize = Math.round((float)width / (float)reqWidth);
                }

                float totalPixels = (float)(width * height);

                for(float totalReqPixelsCap = (float)(reqWidth * reqHeight * 2); totalPixels / (float)(inSampleSize * inSampleSize) > totalReqPixelsCap; ++inSampleSize) {
                    ;
                }
            }

            return inSampleSize;
        } else {
            return 1;
        }
    }

    public static ColorFilter createColorFilter(int destinationColor) {
        Log.i("fw.utils.bitmap", "createColorFilter() called with destinationColor = [0x" + Integer.toHexString(destinationColor) + "]");
        float lr = 0.2126F;
        float lg = 0.7152F;
        float lb = 0.0722F;
        ColorMatrix grayScaleMatrix = new ColorMatrix(new float[]{0.2126F, 0.7152F, 0.0722F, 0.0F, 0.0F, 0.2126F, 0.7152F, 0.0722F, 0.0F, 0.0F, 0.2126F, 0.7152F, 0.0722F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 255.0F});
        int dr = Color.red(destinationColor);
        int dg = Color.green(destinationColor);
        int db = Color.blue(destinationColor);
        float drf = (float)dr / 255.0F;
        float dgf = (float)dg / 255.0F;
        float dbf = (float)db / 255.0F;
        ColorMatrix tintMatrix = new ColorMatrix(new float[]{drf, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, dgf, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, dbf, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F});
        tintMatrix.preConcat(grayScaleMatrix);
        return new ColorMatrixColorFilter(tintMatrix);
    }
}
