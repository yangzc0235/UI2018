package com.zinc.class4_xfermode.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zinc.class4_xfermode.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/26
 * @description
 */
public class ItemXFerModeView extends View {

    private float mWidth;
    private boolean isInit = false;

    private Bitmap dstBitmap;
    private Bitmap srcBitmap;

    // 背景
    private Shader itemBackground;

    private Paint mBitmapPaint;

    private Paint mBgPaint;

    private float offsetX;
    private float offsetY;

    private RectF rectF;

    private Xfermode mCurXfermode;

    public ItemXFerModeView(Context context) {
        super(context);
        init(context);
    }

    public ItemXFerModeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ItemXFerModeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setCurXfermode(Xfermode curXfermode) {
        this.mCurXfermode = curXfermode;
        invalidate();
    }

    public void init(Context context) {
        Bitmap bm = Bitmap.createBitmap(new int[]{0xFFFFFFFF, 0xFFCCCCCC, 0xFFCCCCCC, 0xFFFFFFFF},
                2, 2, Bitmap.Config.RGB_565);
        itemBackground = new BitmapShader(bm,
                Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT);
        Matrix m = new Matrix();
        m.setScale(6, 6);
        itemBackground.setLocalMatrix(m);

        dstBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.dst);
        srcBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.src);

        mBgPaint = new Paint();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!isInit) {
            isInit = true;
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());

            offsetX = (getMeasuredWidth() - mWidth) / 2;
            offsetY = (getMeasuredHeight() - mWidth) / 2;

            rectF = new RectF(0, 0, mWidth, mWidth);

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mCurXfermode == null) {
            return;
        }

        canvas.translate(offsetX, offsetY);

        mBgPaint.setShader(itemBackground);
        mBgPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, mWidth, mWidth, mBgPaint);

        int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
                Canvas.ALL_SAVE_FLAG);

        mBitmapPaint.setXfermode(null);
        canvas.drawBitmap(dstBitmap, null, rectF, mBitmapPaint);
        mBitmapPaint.setXfermode(mCurXfermode);
        canvas.drawBitmap(srcBitmap, null, rectF, mBitmapPaint);

        canvas.restoreToCount(layer);

    }
}
