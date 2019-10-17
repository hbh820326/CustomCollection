package com.doone.CustomCollection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

/**
 * 封屏加载控件
 *
 * @author 林建鹏 QQ83606260
 * @date 2019年9月18日
 */

public class DialogView extends View {
    private Dialog dialog;//寄主
    private float x, y;//中心坐标
    private String hint = "加载中,请稍候...";//提示文本
    private int width, height;//View的大小
    private Rect rect = new Rect();//测量文字
    private Handler handler = new Handler();
    private Paint mPaint = new Paint();//画笔
    private int skewing,n,start = 20, end = 60;//线条两端离中心的距离
    private double s_30 = Math.sin(Math.PI /6);//   30度转弧度 Math.PI /(180/30)
    private double s_60 = Math.sin(Math.PI /3);//正弦60等于余弦30
    private float s30_s = (float) (s_30 * start);
    private float s60_s = (float) (s_60 * start);
    private float s30_e = (float) (s_30 * end);
    private float s60_e = (float) (s_60 * end);
    private int[] colors = {0xffffffff, 0xffeeeeee, 0xffdddddd, 0xffcccccc, 0xffbbbbbb, 0xffaaaaaa, 0xff999999, 0xff888888, 0xff777777, 0xff666666, 0xff555555, 0xff444444};
    public DialogView(Context context) {
        super(context);
        View dv =((Activity) context).getWindow().getDecorView();
        width = dv.getWidth();
        height = dv.getHeight();
        x = width / 2f;
        y = height * 0.4f;
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStrokeWidth(6);//画笔宽度
        mPaint.setTextSize(45);//设置文字大小
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {//画12片花瓣 和 文字
        mPaint.setColor(colors[skewing++ % 12]);
        canvas.drawLine(x - start, y, x - end, y, mPaint);
        mPaint.setColor(colors[skewing++ % 12]);
        canvas.drawLine(x - s60_s, y + s30_s, x - s60_e, y + s30_e, mPaint);
        mPaint.setColor(colors[skewing++ % 12]);
        canvas.drawLine(x - s30_s, y + s60_s, x - s30_e, y + s60_e, mPaint);
        mPaint.setColor(colors[skewing++ % 12]);
        canvas.drawLine(x, y + start, x, y + end, mPaint);
        mPaint.setColor(colors[skewing++ % 12]);
        canvas.drawLine(x + s30_s, y + s60_s, x + s30_e, y + s60_e, mPaint);
        mPaint.setColor(colors[skewing++ % 12]);
        canvas.drawLine(x + s60_s, y + s30_s, x + s60_e, y + s30_e, mPaint);
        mPaint.setColor(colors[skewing++ % 12]);
        canvas.drawLine(x + start, y, x + end, y, mPaint);
        mPaint.setColor(colors[skewing++ % 12]);
        canvas.drawLine(x + s60_s, y - s30_s, x + s60_e, y - s30_e, mPaint);
        mPaint.setColor(colors[skewing++ % 12]);
        canvas.drawLine(x + s30_s, y - s60_s, x + s30_e, y - s60_e, mPaint);
        mPaint.setColor(colors[skewing++ % 12]);
        canvas.drawLine(x, y - start, x, y - end, mPaint);
        mPaint.setColor(colors[skewing++ % 12]);
        canvas.drawLine(x - s30_s, y - s60_s, x - s30_e, y - s60_e, mPaint);
        mPaint.setColor(colors[skewing++ % 12]);
        canvas.drawLine(x - s60_s, y - s30_s, x - s60_e, y - s30_e, mPaint);
        mPaint.setColor(colors[0]);
        canvas.drawText(hint, x - rect.width() / 2f, y + end + rect.height()+20, mPaint);//居中并和花间距20像素
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            invalidate();
            skewing++;
            handler.postDelayed(this, 99);
        }
    };

    public void start(String hint) {
        n++;//记录被打开几次
        if (!TextUtils.isEmpty(hint)) {
            this.hint = hint;
            mPaint.getTextBounds(hint, 0, hint.length(), rect);//测量文字
        }
        if (dialog == null) {
            dialog = new Dialog(getContext());
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(this);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    handler.removeCallbacks(run);
                    n=0;//手动关闭就不管打开次数了
                }
            });
        }
        if (!dialog.isShowing()) {
            dialog.show();
            handler.post(run);
        }
    }
    public void stop() {
        if(n>0)n--;//最后一次才会关闭
        if (dialog != null&&n==0)dialog.cancel();
    }
}
