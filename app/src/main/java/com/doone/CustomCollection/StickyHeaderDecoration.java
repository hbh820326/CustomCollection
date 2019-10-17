package com.doone.CustomCollection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.Map;

/**
 * 自定义悬浮分区头，条件每一个itemView 必需设置Tag,为对应的数据
 * @author 林建鹏 QQ83606260
 * @date 2019年9月18日
 */
public class StickyHeaderDecoration extends RecyclerView.ItemDecoration {
    PinYin py = new PinYin();
    Paint paint;//画笔
    float height;
    Rect rect = new Rect();
    float paddingLeft;

    /**
     * @param decorationHeight 分区头高度
     * @param textSize         文字尺寸
     * @param paddingLeft      文字离左边尺寸
     */
    public StickyHeaderDecoration(float decorationHeight, float textSize, float paddingLeft) {
        this.height = decorationHeight;
        this.paddingLeft = paddingLeft;
        paint = new Paint();//画笔
        paint.setAntiAlias(true);// 设置画笔无锯齿    （ 耗资源较大，要求不是很高的话可以注释掉）
        paint.setTextSize(textSize);
    }

    /**
     * 预留分区头空间
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int index = parent.getChildAdapterPosition(view);
        Map<String, Object> map = state.get(index);
        if(map==null){
            state.put(index, view.getTag());
            map = state.get(index);
        }
        if (index == 0||hasHeader(map,state.get(index - 1))) {
            outRect.top = (int)height;
        }
    }

    /**
     * 绘制所有分区头
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {//这里画出的东西是复盖在item之上的
        for (int layoutPos = 0; layoutPos < parent.getChildCount(); layoutPos++) {
            if (layoutPos == 0 || hasHeader(parent.getChildAt(layoutPos).getTag(),parent.getChildAt(layoutPos - 1).getTag())) {
                drawHeader(c, getFlag((Map<String, Object>) parent.getChildAt(layoutPos).getTag()), getHeaderTop(parent, layoutPos));
            }
        }
    }

    /**
     * 画分区头
     * @param canvas 画布
     * @param text   文字
     * @param Y      起点坐标Y
     */
    public void drawHeader(Canvas canvas, String text, int Y) {
        paint.setColor(Color.argb(0xff, 0xcc, 0xcc, 0xcc));//
        canvas.drawRect(0, Y, canvas.getWidth(), height + Y, paint);//画方形
        paint.setColor(Color.WHITE);
        paint.getTextBounds(text, 0, 1, rect);//测量文字
        canvas.drawText(text, 0, text.length(), paddingLeft, (height - rect.height()) / 2 + rect.height() + Y, paint);
    }

    /**
     * @param parent    RecyclerView
     * @param layoutPos 布局中的下标
     * @return 分区头的  Y 坐标值
     */

    private int getHeaderTop(RecyclerView parent, int layoutPos) {
        View child = parent.getChildAt(layoutPos);
        if (layoutPos == 0) {
            View next = parent.getChildAt(1);//第二个View
            if (hasHeader(child.getTag(),next.getTag())) {//必需有头部
                int y = (int) (next.getY() - height - height);//头部必需顶到悬浮头部
                if (y < 0) return y;           //做推移动画
            }
            return 0;
        }
        return (int) (child.getY() - height);
    }

    /**
     * 是否有分区头
     *
     * @param map1
     * @param map2
     * @return
     */

    private boolean hasHeader(Object map1, Object map2) {
        return !getFlag((Map<String, Object>) map1).equals(getFlag((Map<String, Object>) map2));
    }

    /**
     * 分区标记
     *
     * @param map
     * @return
     */

    public String getFlag(Map<String, Object> map) {
        return py.convert(map.get("name") + "");
    }
}