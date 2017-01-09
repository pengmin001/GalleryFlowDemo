package com.gallerydemo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;

/**
 * Created by Administrator on 2017/1/4.
 */

public class BitmapScaleDownUtil {
    //数据
    private final String TAG = "BitmapScaleDownUtil";

    //函数段
    /*
     * @function 获取屏幕大小
     * @param display
     * @return 屏幕宽高
     */

    public static int[] getScreenDimension(Display display) {
        int[] dimension = new int[2];
        dimension[0] = display.getWidth();
        dimension[1] = display.getHeight();

        return dimension;

    }

    /**
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return 取样后的Bitmap
     * @function 以取样方式加载Bitmap
     */

    public static Bitmap decodeSampleBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        //step1,将inJustDecodeBounds置为true，已解析Bitmap真实尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        //step2,计算Bitmap取样比例
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        //step3,将inJustDecodeBounds置为false，以取样比例解析Bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return 取样比例
     * @function 计算Bitmap取样比例
     */

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //默认取样比例为1:1
        int inSamplesize = 1;

        //Bitmap原始尺寸
        final int width = options.outWidth;
        final int height = options.outHeight;

        //取最大取样比例
        if (height > reqHeight || width < reqWidth) {
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            final int heightRation = Math.round((float) height / (float) reqHeight);

            //取样比例为x:1，其中x>=1
            inSamplesize = Math.max(widthRatio, heightRation);

        }
        return inSamplesize;

    }
}
