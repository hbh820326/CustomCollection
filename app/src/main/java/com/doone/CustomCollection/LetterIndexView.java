package com.doone.CustomCollection;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.doone.CustomCollection.R;

/**
 * 自定义字母索引控件
 *
 * @author 林建鹏 QQ83606260
 * @date 2019年9月18日
 */
public class LetterIndexView extends View {
    private String[] arrays = {"@", "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private int width;//控件宽度
    private int height;//控件高度
    private int cellHeight;//单字母高度
    private int cellWidth;//单字母宽度
    private int markWidth;//提示框宽度
    private float markTextSize;//提示框字体大小
    private float textSize;//字母字体大小
    private boolean isDown = false;// 标识是否按下，用作改变控件背景色
    private int select = 0;//选中项索引
    private Rect rect = new Rect();//测量字体宽高
    RectF  r2 = new RectF();//画提示框
    Paint paint = new Paint();


    ILetterIndexer indexer;//选中监听

    public void setILetterIndexer(ILetterIndexer indexer) {
        this.indexer = indexer;
    }

    public interface ILetterIndexer {
        void getPositionForSection(String section);
    }

    public LetterIndexView(Context context) {
        super(context);
        initIndexView(context, null, 0);
    }

    public LetterIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initIndexView(context, attrs, 0);
    }

    public LetterIndexView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initIndexView(context, attrs, defStyle);
    }
    
    private void initIndexView(Context context, AttributeSet attrs, int defStyle) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterIndexView, defStyle, 0);
        if (typedArray != null) {
            cellWidth = (int) typedArray.getDimension(R.styleable.LetterIndexView_cellwidth, 30);
            markWidth = (int) typedArray.getDimension(R.styleable.LetterIndexView_markwidth, 80);
            textSize = typedArray.getDimension(R.styleable.LetterIndexView_textsize, 10);
            markTextSize = typedArray.getDimension(R.styleable.LetterIndexView_mark_text_size, 80);
            paint.setAntiAlias(true);
            typedArray.recycle();
        }
    }
    private void setSelection(float y) {

            int index = (int) (y / cellHeight);
            if(index<arrays.length&&index>-1&&select != index){
                    if (indexer != null) indexer.getPositionForSection(arrays[index]);
                    isDown = true;
                    invalidate();
                    select = index;
            }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            width = getWidth();
            height = getHeight();
            cellHeight = height / arrays.length;
            r2.left = (width - markWidth) / 2f;
            r2.top = (height - markWidth) / 2f;
            r2.right = r2.left + markWidth;
            r2.bottom = r2.top + markWidth;
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(-1728053248);
        paint.setTextSize(textSize);
        for (int i = 0; i < arrays.length; i++) {
            paint.getTextBounds(arrays[i], 0, 1, rect);//测量文字
            canvas.drawText(arrays[i], width - cellWidth + (cellWidth - rect.width()) / 2f, i * cellHeight + (cellHeight - rect.height()) / 2f + rect.height(), paint);
        }
        if (isDown) {
            canvas.drawRoundRect(r2, 9, 9, paint);
            paint.setColor(Color.WHITE);
            paint.setTextSize(markTextSize);
            paint.getTextBounds(arrays[select], 0, 1, rect);//测量文字
            canvas.drawText(arrays[select], r2.left + (markWidth - rect.width()) / 2f, r2.bottom - (markWidth - rect.height()) / 2f, paint);
            paint.setColor(570425344);
            rect.set(width - cellWidth, 0, width, height);
            canvas.drawRect(rect, paint);
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() > width - cellWidth) {
                    setSelection(event.getY());
                    return true;
                } else {
                    return false;
                }
            case MotionEvent.ACTION_MOVE:
                setSelection(event.getY());
                return true;
            default:
                isDown = false;
                invalidate();
                return true;
        }
    }
}  