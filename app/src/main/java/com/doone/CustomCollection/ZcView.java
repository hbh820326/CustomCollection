package com.doone.CustomCollection;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.doone.CustomCollection.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自定义底部导航控件
 *
 * @author 林建鹏 QQ83606260
 * @date 2019年9月18日
 */
public class ZcView extends View {
    private Paint mPaint = new Paint();//画笔
    SelectListener selectListener;//选中监听器
    List<Map> data = new ArrayList<>();//按钮数据，第条代表一个按钮
    int textColor1;//选中颜色
    int textColor2;//非选中颜色
    int iconSize;//正方形图片的一条边
    int textTop;//文字离图片的距离
    int cellWidth;//单个按钮的宽度
    int width;//View的宽度
    int height;//View的高度
    int radius;//半径
    RectF r2 = new RectF();//画角标用

    int flag=-1;//上次选中的下标

    /**
     * 设置角标
     *
     * @param index 按钮位置
     * @param cont  角标数字
     */
    public void refreshBegNumber(int index, int cont) {
        Map map = data.get(index);
        map.put("cont", cont);
        refresh();
    }

    public int getAllCont(){
        int cont=0;
        for(Map map:data){
            cont+=(int)map.get("cont");
        }
        return cont;
    }

    /**
     * 可代码模拟单击
     *
     * @param index
     */
    public void callOnClick(int index) {
        if (data.size() > index) {
            if (index != flag) {
                Map cell = data.get(index);
                cell.put("select", true);
               if(flag!=-1){
                   cell = data.get(flag);
                   cell.put("select", false);
               }
                flag = index;
                refresh();
                if (selectListener != null) selectListener.selectOn(index);//选中监听
            }
        }
    }

    public ZcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ZcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public interface SelectListener {
        void selectOn(int index);
    }

    public void setOnSelectListener(SelectListener selectListener) {
        this.selectListener = selectListener;
    }

    private void initView(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZcView);
            textTop = (int) ta.getDimension(R.styleable.ZcView_textTop, 4);
            iconSize = (int) ta.getDimension(R.styleable.ZcView_iconSize, 46);
            mPaint.setTextSize(ta.getDimension(R.styleable.ZcView_textSize, 24));
            textColor1 = ta.getColor(R.styleable.ZcView_textColor1, 0);
            textColor2 = ta.getColor(R.styleable.ZcView_textColor2, 0);
            radius = (int) ta.getDimension(R.styleable.ZcView_radius, 16);
            ta.recycle();
            Paint.FontMetrics fm = mPaint.getFontMetrics();
            textHeight = Math.ceil(fm.descent - fm.ascent);

        }
    }

    /**
     * 设置时做数据检查，只允许一个为true
     *
     * @param data
     */
    public void setData(List<Map> data) {
        boolean b = true;
        for (int i = 0; i < data.size(); i++) {
            Map map = data.get(i);
            if ((boolean) map.get("select"))
                if (b) {
                    b = false;
                    callOnClick(i);
                } else map.put("select", false);
            this.data.add(map);
        }
        if (b) {
            this.data.get(0).put("select", true);
            callOnClick(0);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        if (cellWidth==0){
            cellWidth = width / data.size();

        }
    }//测量View的宽高
    private Rect  rect1=  new Rect();
    private Rect rect2 = new Rect();
    double textHeight;
    float textWidth;
    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(textColor2);
        canvas.drawLine(0, 0, width, 0, mPaint);
        if (data.size() > 0) {
            int n = (cellWidth - iconSize) / 2;//左右边距
            int m = (int) ((height - iconSize - textHeight - textTop) / 2);//上下边距
            int icon;
            String name;
            for (int i = 0; i < data.size(); i++) {
                Map cell = data.get(i);
                if ((boolean) cell.get("select")) {
                    icon = (int) cell.get("icon0");
                    mPaint.setColor(textColor1);
                } else {
                    icon = (int) cell.get("icon1");
                    mPaint.setColor(textColor2);
                }
                name = cell.get("name")+"";
                textWidth = mPaint.measureText(name);
                canvas.drawText(name, cellWidth * i + (cellWidth - textWidth) / 2, (float) (iconSize + textHeight + textTop + m), mPaint);
                Bitmap bitmap= BitmapFactory.decodeResource(getResources(), icon);
                rect1.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
                rect2.set(cellWidth * i + n, m, cellWidth + cellWidth * i - n, (int) (height - textHeight - textTop - m));
                canvas.drawBitmap(bitmap,rect1, rect2, mPaint);
                setBegNumber(canvas, (int) cell.get("cont"), i, n, m);
            }
        }
    }//视图的绘制工作


    /**
     * @param canvas 画布
     * @param cont   角标数字
     * @param index  第几个视图
     * @param left   离视图的左边缘距离
     * @param top    离视图的上边缘距离
     */
    private void setBegNumber(Canvas canvas, int cont, int index, int left, int top) {
        if (cont > 0) {
            String str = cont > 99 ? "99+" : cont + "";
            mPaint.getTextBounds(str, 0, str.length(), rect2);//测量文字
            r2.left = cellWidth * index + left + iconSize - radius;
            r2.top = top - radius;
            r2.right = cellWidth * index + left + iconSize + radius * str.length();
            r2.bottom = top + radius;
            mPaint.setColor(-246210);
            mPaint.setAntiAlias(true);
            canvas.drawRoundRect(r2, radius, radius, mPaint);
            mPaint.setColor(Color.WHITE);
            canvas.drawText(str, r2.left + (r2.right - r2.left - rect2.width()) / 2, r2.top + rect2.height() + (r2.bottom - r2.top - rect2.height()) / 2, mPaint);
        }
    }

    private void refresh() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            invalidate();//UI线程调刷新
        } else postInvalidate();//非UI线程调刷新
    }

    float x, y;
    int indexBt;
    boolean isOnclick;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isOnclick = true;
                indexBt = (int) (event.getX() / cellWidth);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isOnclick) {
                    x = event.getX();
                    y = event.getY();
                    int start = cellWidth * indexBt;
                    int end = start + cellWidth;
                    if (y < 0 || y > getHeight() || x < start || x > end) isOnclick = false;
                }
                break;
            case MotionEvent.ACTION_UP: //完成单击条件：按下后只允许在按钮范围内移动后抬起，或直接抬起
                if (isOnclick) callOnClick(indexBt);
                break;
        }
        return true;
    }
}
//        event.getY(0);//触点离View的上边距离
//        event.getRawX();//触点离屏幕左边距离
//        event.getRawY();//触点离屏幕上边距离
//        getRight();//View右边离ViewGroup的左边距离
//        getLeft();//View左边离ViewGroup的左边距离
//        getBottom();//View下边离ViewGroup的上边距离
//        getTop();//View上边离ViewGroup的上边距离
//        getWidth();//View的宽度   等效：getRight() - getLeft()
//        getHeight();//View的高度  等效：getBottom() - getTop()


//    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), icon);
//                        src[0] = 0;                               src[2] = bitmap.getWidth();
//                        src[1] = 0;                               src[3] = src[1];
//
//                        src[4] = src[0];                           src[6] = src[2];
//                        src[5] = bitmap.getHeight();              src[7] = src[5];
//
//
//                        dst[0] = n + cellWidth * i;                      dst[2] = dst[0] + iconSize;
//                        dst[1] = m;                                      dst[3] = dst[1];
//
//                        dst[4] = dst[0];                                 dst[6] = dst[2];
//                        dst[5] = dst[1] + iconSize;                      dst[7] = dst[5];
//
//                        matrix.setPolyToPoly(src, 0, dst, 0, 4);
//                        canvas.drawBitmap(bitmap, matrix, mPaint);