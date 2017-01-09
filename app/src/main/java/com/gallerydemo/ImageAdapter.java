package com.gallerydemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/1/3.
 */

public class ImageAdapter extends BaseAdapter {
    /*数据段begin*/
    private final String TAG = "ImageAdapter";
    private Context mContext;
    //图片数组
    private int[] mImageIds;
    //图片控件数组
    private ImageView[] mImages;
    //图片控件LayoutParams
    private GalleryFlow.LayoutParams mImagesLayoutParms;
    /*数据段end*/

    /*方法begin*/
    public ImageAdapter(Context context, int[] imageIds) {
        mContext = context;
        mImageIds = imageIds;
        mImages = new ImageView[mImageIds.length];
        mImagesLayoutParms = new GalleryFlow.LayoutParams
                (Gallery.LayoutParams.WRAP_CONTENT, Gallery.LayoutParams.WRAP_CONTENT);
    }

    /**
     * @param imageWidth
     * @param imageHeight
     * @return void
     * @function 根据指定宽高粗昂见待绘制的Bitmap，并绘制到ImageView控件上
     */
    public void creatImages(int imageWidth, int imageHeight) {
        //原图与倒影间距
        final int gapHeight = 5;

        int index = 0;
        for (int imageId : mImageIds) {
            /* step1 采样方式解析原图并生成倒影 */
            // 解析原图,生成原图Bitmap对象
            Bitmap originalImage = BitmapScaleDownUtil.decodeSampleBitmapFromResource(mContext.getResources(),
                    imageId, imageWidth, imageHeight);
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            //Y轴反方向，实质就是x轴翻转
            Matrix matrix = new Matrix();
            matrix.setScale(1, -1);
            //且仅取原图下半部分创建倒影Bitmap对象
            Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 4, width, width / 4, matrix, false);
            /*step2 绘制*/
            //创建一个可包含原图+间距+倒影的新图Bitmap对象
            Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + gapHeight + height / 2), Bitmap.Config.ARGB_8888);
            //在新图Bitmap对象之上创建画布
            Canvas canvas = new Canvas(bitmapWithReflection);
            //抗锯齿效果
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG));
            //绘制原图
            canvas.drawBitmap(originalImage, 0, 0, null);
            //绘制间距
            Paint gapPaint = new Paint();
            gapPaint.setColor(0xFFCCCCCC);
            canvas.drawRect(0, height, width, height + gapHeight, gapPaint);
            //绘制阴影
            canvas.drawBitmap(reflectionImage, 0, height + gapHeight, null);

            /* step3 渲染 */
            //创建一个线性渐变的渲染器用于渲染倒影
            Paint paint = new Paint();
            LinearGradient shader = new LinearGradient(0, height, 0, (height + gapHeight + height / 2), 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
            //设置画笔渲染器
            paint.setShader(shader);
            //设置图片混合模式
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            //渲染倒影+间距
            canvas.drawRect(0, height, width, (height + gapHeight + height / 2), paint);

            /* step4 在ImageView控件上绘制 */
            ImageView imageView = new ImageView(mContext);
            imageView.setImageBitmap(bitmapWithReflection);
            imageView.setLayoutParams(mImagesLayoutParms);
            //Log
            imageView.setTag(index);

            /* 释放内存heap */
            originalImage.recycle();
            reflectionImage.recycle();

            mImages[index++] = imageView;
        }
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int current = position % mImages.length;
        mImages[current].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"onClick=" + current,Toast.LENGTH_LONG).show();
            }
        });
        return mImages[current];
    }


}
/* 函数段end */

