package com.doone.CustomCollection;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.doone.CustomCollection.R;

/**
 * 自定义划动开关
 *
 * @author 林建鹏 QQ83606260
 * @date 2019年9月18日
 */
public class SwichView extends View {
    int width;
    int height;
    int space;//按钮可移动的空间
    float radius;//外框半径
    float br;//按钮圆角半径
    float thickness;//外框壁厚   D81B60
    int openColor=Color.argb(0xff,0xd8,0x1b,0x60);//打开的颜色
    int closeColor=Color.argb(0xff,0x88,0x88,0x88);;//关闭颜色
    int ca ;//透明色的色差
    int oa;//打开的透明色值
    int cr ;//红色的色差
    int or ;//打开的红色值
    int cg ;//绿色的色差
    int og ;//打开的绿色值
    int cb ;//蓝色的色差
    int ob ;//打开的蓝色值
    private Paint mPaint = new Paint();//画笔
    RectF r2 = new RectF();//
    RectF r1 = new RectF();//

    OnOpenListener listener;//选中监听

    boolean isOpen;//是否打开

    public void setOpen(boolean open) {
        if (isOpen != open) {
            isOpen = open;
            invalidate();
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOnOpenListener(OnOpenListener listener) {
        this.listener = listener;
    }

    public interface OnOpenListener {
        void isOnOrOff(boolean isOn);
    }

    public SwichView(Context context) {
        super(context);
        initView(context, null);
    }

    public SwichView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);

    }

    public SwichView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            width = getWidth();
            height = getHeight();
            space = width - height;
            r1.set(0, 0, width, height);
            if (isOpen) {
                r2.set(thickness, thickness, height - thickness, height - thickness);
            } else {
                r2.set(width - height + thickness, thickness, width - thickness, height - thickness);
            }
        }
    }

    private void initView(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwichView);
            radius = ta.getDimension(R.styleable.SwichView_svich_radius, 16);
            thickness = ta.getDimension(R.styleable.SwichView_thickness, 16);
            openColor = ta.getColor(R.styleable.SwichView_openColor, 0);
            closeColor = ta.getColor(R.styleable.SwichView_closeColor, 0);
            ta.recycle();
        }
        mPaint.setAntiAlias(true);
        br = radius < thickness ? 0 : radius - thickness;

        oa = openColor >> 24 & 0xff;
        ca = (closeColor >> 24 & 0xff)-oa;

        or = openColor >> 16 & 0xff;
        cr = (closeColor >> 16 & 0xff)-or;

        og = openColor >> 8 & 0xff;
        cg = (closeColor >> 8 & 0xff)-og;

        ob = openColor & 0xff;
        cb = (closeColor & 0xff)-ob;
        //色值在2进中是每色8位按argb顺序排列
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(getClose());
        canvas.drawRoundRect(r1, radius, radius, mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(r2, br, br, mPaint);
    }

    /**
     * 打开和关闭的ARGB色差*按钮位移的百分比
     * @return
     */
    public int getClose() {
        float bfb = (r2.left - thickness) /space;
        return Color.argb((int) (ca  * bfb + oa), (int) (cr  * bfb + or), (int) (cg * bfb + og), (int) (cb  * bfb + ob));
    }


    float fx,fy;
    boolean click;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y=event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(x > r2.left && x < r2.right){//必需要点中按钮才接管
                    fx=x;
                    fy=y;
                    click=true;
                    handler.removeCallbacksAndMessages(null);//清理动画
                    return true;
                }else{
                    return false;
                }
            case MotionEvent.ACTION_MOVE:
                if(click){//判断是否为单击
                    float tempX = x - fx;
                    float tempY=y-fy;
                   if(Math.sqrt(tempX * tempX + tempY * tempY)>10) click=false;//移动超过10个像素就不为单击
                }
                if (x < height / 2f) {//控制左边极限
                    x = height / 2f;
                } else if (x > width - height / 2f) {//控制右边极限
                    x = width - height / 2f;
                }
                float temp = x - height / 2f + thickness;
                if (r2.left != temp) {
                    r2.left = temp;
                    r2.right = r2.left + height - thickness - thickness;
                    invalidate();
                }
                return true;
            default:
                if(r2.left < width / 2f - height / 2f + thickness){//判断中位线 再动画复位
                    if(click) handler.post(close);//单击成立
                    else handler.post(open);//单击不成立
                }else{
                    if(click) handler.post(open);//单击成立
                    else handler.post(close);//单击不成立
                }
                return true;
        }
    }
    Handler handler = new Handler();
    Runnable open = new Runnable() {
        @Override
        public void run() {
            r2.left -=  10;
            if (r2.left < thickness) r2.left = thickness;
            r2.right = r2.left + height - thickness - thickness;
            invalidate();
            if (r2.left > thickness) handler.postDelayed(this, 20);
            else if(!isOpen){
                    isOpen = true;
                    if (listener != null) listener.isOnOrOff(isOpen);
                }
        }
    };
    Runnable close = new Runnable() {
        @Override
        public void run() {
            r2.left +=  10;
            if (r2.left > space + thickness)r2.left= space + thickness;
            r2.right = r2.left+ height - thickness - thickness;
            invalidate();
            if (r2.left < space + thickness) handler.postDelayed(this, 20);//这个值不要低于16毫秒
            else if(isOpen){
                    isOpen = false;
                    if (listener != null) listener.isOnOrOff(isOpen);
                }
        }
    };
}
