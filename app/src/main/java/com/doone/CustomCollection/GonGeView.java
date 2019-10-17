package com.doone.CustomCollection;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.doone.CustomCollection.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自定义网格布局
 *
 * @author 林建鹏 QQ83606260
 * @date 2019年9月18日
 */
public class GonGeView extends View {
    private Paint mPaint = new Paint();//画笔
    RequestManager glide;

    public GonGeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        glide = Glide.with(context);
        initView(context.obtainStyledAttributes(attrs, R.styleable.GonGeView));
    }

    private void initView(TypedArray ta) {
        iconSize = (int) ta.getDimension(R.styleable.GonGeView_GViconSize, 46);
        textTop = (int) ta.getDimension(R.styleable.GonGeView_GVtextTop, 4);
        mPaint.setTextSize(ta.getDimension(R.styleable.GonGeView_GVtextSize, 24));
        radius = (int) ta.getDimension(R.styleable.GonGeView_GVradius, 16);
        column = ta.getInteger(R.styleable.GonGeView_GVcolumn, 4);
        cellHeight = (int) ta.getDimension(R.styleable.GonGeView_GVcellHeight, 50);
        ta.recycle();

    }

    /**
     * 设置时做数据检查，只允许一个为true
     *
     * @param data
     */
    public void setData( List<Map<String, Object>> data) {

        if (this.data.isEmpty()) {
            this.data = data;
        } else {
            this.data = data;
            int temp = data.size() % column > 0 ? data.size() / column + 1 : data.size() / column;
            if (row == temp) {
                refresh();
            } else {
                row = temp;
                height = row * cellHeight;
                requestLayout();
            }
        }
    }

    List<Map<String, Object>>data = new ArrayList<>();//按钮数据，第条代表一个按钮
    int width;//视图宽
    int height;//视图高           自动算出
    int cellWidth;//子视图宽       自动算出
    int cellHeight;//子视图高
    int iconSize;//正方形图片的一条边
    int textTop;//离图片的距离
    int radius;//角标半径
    Rect rect1 = new Rect();
    ;//子视图框
    private Rect rect2 = new Rect();
    int row;//横                   自动算出
    int column;//纵
    RectF r2 = new RectF();//画角标用

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        if (cellWidth == 0) {
            cellWidth = width / column;
            row = (int) Math.ceil(data.size() / (double) column);
            height = row * cellHeight;
        }
        setMeasuredDimension(width, height);

    }//测量V

    CustomTarget ct;

    @Override
    protected void onDraw(Canvas canvas) {
        int n = (cellWidth - iconSize) / 2;//左右边距
        int m;//上下边距
        String name;
        Map map;

        for (int i = 0; i < row; i++)
            for (int j = 0, k = i * column; j < column && k < data.size(); j++, k++) {
                map = data.get(k);
                name = (String) map.get("text");
                mPaint.getTextBounds(name, 0, name.length(), rect2);//测量文字
                m = (cellHeight - iconSize - rect2.height() - textTop) / 2;//上下边距
                canvas.drawText(name, (float) (cellWidth * j + ((double) cellWidth - rect2.width()) / 2), (float) (cellHeight * i + iconSize + rect2.height() + textTop + m), mPaint);
                Bitmap bitmpa = (Bitmap) map.get("bitmap");
                if (bitmpa == null) {
                    qureBitmap(map);
                } else {
                    rect1.set(0, 0, bitmpa.getWidth(), bitmpa.getHeight());
                    rect2.set(cellWidth * j + n, cellHeight * i + m, cellWidth + cellWidth * j - n, cellHeight * i + cellHeight - rect2.height() - textTop - m);
                    canvas.drawBitmap(bitmpa, rect1, rect2, mPaint);
                }
               setBegNumber(canvas, ((Double)map.get("cont")).intValue(), j, n, m + cellHeight * i);
                if (flag == k) {//按住效果
                    mPaint.setColor(570425344);
                    rect2.set(cellWidth * j, cellHeight * i, cellWidth + cellWidth * j, cellHeight * i + cellHeight);
                    canvas.drawRect(rect2, mPaint);
                    mPaint.setColor(Color.BLACK);
                }
            }
    }//视图的绘制工作

    public void qureBitmap(final Map map) {
        ct = new CustomTarget<BitmapDrawable>(iconSize, iconSize) {

            @Override
            public void onResourceReady(BitmapDrawable resource, Transition<? super BitmapDrawable> transition) {
                transition.transition(resource,new Transition.ViewAdapter(){

                    @Override
                    public View getView() {
                        return null;
                    }

                    @Override
                    public Drawable getCurrentDrawable() {
                        return null;
                    }

                    @Override
                    public void setDrawable(Drawable drawable) {

                    }
                });
                    map.put("bitmap", resource.getBitmap());
                    refresh();
            }
            @Override
            public void onLoadCleared(Drawable placeholder) {
            }
        };
        glide.load(map.get("icon")).into(ct);
    }


    int flag = -1;

    private void onTach(int flag) {
        if (this.flag != flag) {
            this.flag = flag;
            refresh();
        }
    }


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
            mPaint.setColor(Color.BLACK);
        }
    }

    boolean isOnclick;
    int columnBt;//第几列
    int rowBt;//第几行
    int startY;//实际在View中的位置
    int py;//偏移的位置
    int maxPy;//超出屏幕的部分


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) event.getY() + py;
                isOnclick = true;
                columnBt = (int) (event.getX() / cellWidth);
                rowBt = startY / cellHeight;
                onTach(column * rowBt + columnBt);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isOnclick) {
                    int x = (int) event.getX(), y = (int) (event.getY() + py);
                    int startX = cellWidth * columnBt;
                    int endX = startX + cellWidth;
                    int startY = cellHeight * rowBt;
                    int endY = startY + cellHeight;
                    if (y < startY || y > endY || x < startX || x > endX) {
                        isOnclick = false;
                        onTach(-1);
                    }
                } else {
                    if (maxPy > 0) scrollTo(0, (int) event.getY());//当高度超过了可见高度时，触发上下滚动，
                }
                break;
            case MotionEvent.ACTION_UP: //完成单击条件：按下后只允许在按钮范围内移动后抬起，或直接抬起
                onTach(-1);
                if (isOnclick) {
                    int index = column * rowBt + columnBt;
                    if (index < data.size()) callOnClick(data.get(index));
                }
                break;
            default:
                onTach(-1);
                break;

        }
        return true;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        maxPy = height - getHeight();//计算超出部分高度
    }

    @Override
    public void scrollTo(int x, int y) {
        y = startY - y;
        py = y > 0 ? maxPy < y ? maxPy : y : 0;
        super.scrollTo(x, py);

    }


    private void refresh() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            invalidate();//UI线程调刷新
        } else postInvalidate();//非UI线程调刷新
    }

    SelectListener selectListener;

    public interface SelectListener {
        void selectOn(Map map);
    }

    public void setOnSelectListener(SelectListener selectListener) {
        this.selectListener = selectListener;
    }

    /**
     * //     * 可代码模拟单击
     * //     *
     * //     * @param index
     * //
     */
    public void callOnClick(Map map) {
        if (selectListener != null) selectListener.selectOn(map);//选中监听
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