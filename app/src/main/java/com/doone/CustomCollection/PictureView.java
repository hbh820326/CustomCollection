package com.doone.CustomCollection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 支持 GIF 和普通图片
 * 比系统的ImageView轻量很多，才150行不到的代码
 * 本来想写个类似phonView的控件，可惜矩阵没学好，这是我胸口永远的痛，
 */
public class PictureView extends View implements Drawable.Callback {
    Paint paint;
    Matrix matrix;
    float[] matrixValue;
    int width, height;
    GifDrawable gifDrawable;
    Bitmap bitmap;
    Drawable dr;
    GifImageDecoder gifDecoder;

    public PictureView(Context context) {
        super(context);
        init();
    }

    public PictureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PictureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        matrix = new Matrix();
        paint = new Paint();
        matrixValue = new float[9];

    }

    /**
     * 支持参数类型： Bitmap；GifDrawable；Drawable；InputStream；byte[]；
     * @param obj
     */
    public void setData(Object obj) {
        reset();//先回收防止重复利用时残留旧的数据
        if (obj instanceof GifDrawable) {
            gifDrawable = (GifDrawable) obj;
            gifDrawable.setCallback(this);//设置桢变化监听
            gifDrawable.start();//启动GIF播放
        } else {
            if (obj instanceof Drawable) {
                dr = (Drawable) obj;
                int w = dr.getIntrinsicWidth();
                int h = dr.getIntrinsicHeight();
                double ws = (double) width / w;
                double hs = (double) height / h;
                double t = hs > ws ? ws : hs;//顶格缩放比例
                w *= t;
                h *= t;
                int ofSetW = (width - w) / 2;//横向居中补偿
                int ofSetH = (height - h) / 2;//纵向居中补偿
                dr.setBounds(ofSetW, ofSetH, w + ofSetW, h + ofSetH);
                refresh();
            } else if (obj instanceof Bitmap) {
                setBitmap((Bitmap) obj);
                refresh();
            } else{
                gifDecoder=new GifImageDecoder(obj);
                gifDecoder.start(new GifImageDecoder.Callback(){
                    @Override
                    public void current(Bitmap bitmap) {
                        setBitmap(bitmap);
                        refresh();
                    }
                } );
            }
        }
    }

    public void reset() {//回收资源
        if (gifDrawable != null) {
            gifDrawable.stop();
            gifDrawable.setCallback(null);
            gifDrawable = null;
        }
        if (bitmap != null) bitmap = null;
        if (dr != null) dr = null;
        if(gifDecoder!=null){
            gifDecoder.stop();
            gifDecoder=null;
        }
    }

    /**
     * GIF回调
     * 用反射取Bitmap进行刷新
     * @param who
     */
    @Override
    public void invalidateDrawable(Drawable who) {
        try {
            Object obj = reflect(who.getClass(), who, "state");
            obj = reflect(obj.getClass(), obj, "frameLoader");
            Method repay = obj.getClass().getDeclaredMethod("getCurrentFrame");//得到方法对象,有参的方法需要指定参数类型
            repay.setAccessible(true);
            setBitmap((Bitmap) repay.invoke(obj));
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object reflect(Class clazz, Object obj, String name) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(name);//取得某字段
        field.setAccessible(true);
        return field.get(obj);
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public void setBitmap(Bitmap bit) {  //控制矩阵让图片缩放并居中显示
        bitmap = bit;
        float w = ((float) width) / bitmap.getWidth();
        float h = ((float) height) / bitmap.getHeight();
        matrixValue[0] = h > w ? w : h;//宽                    //原比例顶格填充
        matrixValue[4] = matrixValue[0];//高
        matrixValue[8] = 1;// Z轴 拉远和拉近，和图片大小有关    （原宽高除以这个值 ）
        matrixValue[2] = (width - bitmap.getWidth() * matrixValue[0]) / 2;//x轴       //居中
        matrixValue[5] = (height - bitmap.getHeight() * matrixValue[0]) / 2;//y轴
        matrix.setValues(matrixValue);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null) canvas.drawBitmap(bitmap, matrix, paint);
        if (dr != null) dr.draw(canvas);
    }

    private void refresh() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            invalidate();//UI线程调刷新
        } else postInvalidate();//非UI线程调刷新
    }
}


//  b_0.setText("宽乘"+matrixValue[0]);//宽拉压
//       b_4.setText("高乘"+matrixValue[4]);//高拉压
//
//       b_2.setText("X等"+matrixValue[2]);//x坐标
//       b_5.setText("Y等"+matrixValue[5]);//y坐标
//
//
//       b_1.setText("宽+高乘"+matrixValue[1]);//错切宽
//       b_3.setText("高+宽乘"+matrixValue[3]);//错切高
//
//       b_6.setText(matrixValue[6]+"");
//       b_7.setText(matrixValue[7]+"");
//       b_8.setText("宽高除"+matrixValue[8]);//等比缩放