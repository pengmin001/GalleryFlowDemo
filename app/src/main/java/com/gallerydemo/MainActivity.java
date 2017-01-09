package com.gallerydemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /* 数据段begin */
    private final String TAG = "GalleryMainActivity";
    private Context mContext;

    //图片缩放倍率
    public static final int SCALE_FACTOR = 11;

    //图片间距（控制各图片之间的距离）
    private final int GALLERY_SPACING = -10;

    //控件
    private GalleryFlow mGalleryFlow;
    /* 数据段end */

    /* 函数段begin */


    private int mIndex = Integer.MAX_VALUE / 2;

    private Button mLeftBtn;
    private Button mRightBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        setContentView(R.layout.activity_main);
        initGallery();
        setControlBtn();
    }

    private void setControlBtn() {
        mLeftBtn = (Button) findViewById(R.id.btn_left);
        mRightBtn = (Button) findViewById(R.id.btn_right);

        mLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGalleryFlow.setSelection(--mIndex);
            }
        });

        mRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGalleryFlow.setSelection(++mIndex);

            }
        });
    }

    private void initGallery() {
        //图片ID
        int[] images = {
                R.drawable.image01,
                R.drawable.image02,
                R.drawable.image03,
                R.drawable.image04,
                R.drawable.image05,

        };
        ImageAdapter adapter = new ImageAdapter(mContext, images);
        //计算图片的宽高
        int[] dimension =
                BitmapScaleDownUtil.getScreenDimension(getWindowManager().getDefaultDisplay());
        int imageWidth = dimension[0] / SCALE_FACTOR;
        int imageHeight = dimension[1] / SCALE_FACTOR;
        //初始化图片
        adapter.creatImages(imageWidth, imageHeight);

        //设置Adapter，显示位置位于控件中间，这样使得左右均可“无限”滑动
        mGalleryFlow = (GalleryFlow) findViewById(R.id.gallery_flow);

        Bitmap originalImage = BitmapScaleDownUtil.decodeSampleBitmapFromResource(mContext.getResources(),
                R.drawable.image01, imageWidth, imageHeight);
        int width = originalImage.getWidth();

        mGalleryFlow.setSpacing(-(width/5));
//        mGalleryFlow.setSpacing(GALLERY_SPACING);
        mGalleryFlow.setAdapter(adapter);
        mGalleryFlow.setSelection(mIndex);//这个方法是选择显示的页面
        mGalleryFlow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }
}
