package com.doone.CustomCollection;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.doone.CustomCollection.R;

/**
 * 自定义导航头控件
 *
 * @author 林建鹏 QQ83606260
 * @date 2019年9月18日
 */
public class HeadView extends View {
    private Paint mPaint = new Paint();//画笔
    Rect rect = new Rect();//测量文字容器
    Drawable centreIcon, leftIcon, rightIcon;//左中右图片
    String centreString, leftString, rightString;//左中右文字
    float iconSize, space;
    int width, height;
    int centreWidth, centreHeight, leftWidth, leftHeight, rightWidth, rightHeight;//左中右文字串的宽高
    int iconTop, iconBottom, textBottom;//图片垂直居中，离上下的距离
    OnClickListener listener;

    public interface OnClickListener {
        void onClick(int what);
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setCentreString(String centreString) {
        if (centreString != null) {
            this.centreString = centreString;
            mPaint.getTextBounds(centreString, 0, centreString.length(), rect);//测量文字
            centreWidth = rect.width();
            centreHeight = rect.height();
        }
    }

    public void setRightString(String rightString) {
        if (rightIcon == null && rightString != null) {
            this.rightString = rightString;
            mPaint.getTextBounds(rightString, 0, rightString.length(), rect);//测量文字
            rightWidth = rect.width();
            rightHeight = rect.height();
        }
    }

    public void setLeftString(String leftString) {
        if (leftIcon == null && leftString != null) {
            this.leftString = leftString;
            mPaint.getTextBounds(leftString, 0, leftString.length(), rect);//测量文字
            leftWidth = rect.width();
            leftHeight = rect.height();
        }
    }

    public void setLeftIcon(Drawable leftIcon) {
        this.leftIcon = leftIcon;
    }

    public void setRightIcon(Drawable rightIcon) {
        this.rightIcon = rightIcon;
    }

    public void setCentreIcon(Drawable centreIcon) {
        this.centreIcon = centreIcon;
    }

    public HeadView(Context context) {
        this(context, null);
    }


    public HeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HeadView);
            mPaint.setColor(ta.getColor(R.styleable.HeadView_textColor, Color.BLACK));//文本颜色
            mPaint.setTextSize(ta.getDimension(R.styleable.HeadView_HVtextSize, 54));//文本尺寸
            iconSize = ta.getDimension(R.styleable.HeadView_HViconSize, 90);//图标尺寸
            space = ta.getDimension(R.styleable.HeadView_space, 30);//间距
            setCentreIcon(ta.getDrawable(R.styleable.HeadView_centreIcon));//中心图标
            setCentreString(ta.getString(R.styleable.HeadView_centreString));//中心文字
            setLeftIcon(ta.getDrawable(R.styleable.HeadView_leftIcon));//左边图标
            setLeftString(ta.getString(R.styleable.HeadView_leftString));//左边文字
            setRightIcon(ta.getDrawable(R.styleable.HeadView_rightIcon));//右边图标
            setRightString(ta.getString(R.styleable.HeadView_rightString));//右边文字
            ta.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            width = getWidth();
            height = getHeight();
            iconTop = (int) ((height - iconSize) / 2);
            iconBottom = (int) (iconTop + iconSize);
            int textHeight = centreHeight > 0 ? centreHeight : leftHeight > 0 ? leftHeight : rightHeight;
            textBottom = (int) ((height - textHeight) / 2f + textHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setTypeface(Typeface.DEFAULT);//旁边不加粗
        if (leftIcon == null) {
            if (leftString != null) canvas.drawText(leftString, space, textBottom, mPaint);
        } else {
            leftIcon.setBounds((int) space, iconTop, (int) (iconSize + space), iconBottom);
            leftIcon.draw(canvas);
        }
        if (rightIcon == null) {
            if (rightString != null)
                canvas.drawText(rightString, width - space - rightWidth, textBottom, mPaint);
        } else {
            int right = (int) (width - space);
            rightIcon.setBounds((int) (right - iconSize), iconTop, right, iconBottom);
            rightIcon.draw(canvas);
        }
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);//中间加粗
        if (centreIcon != null && centreString != null) {
            int left = (int) ((width - centreWidth - iconSize - space) / 2);
            centreIcon.setBounds(left, iconTop, (int) iconSize + left, iconBottom);
            centreIcon.draw(canvas);
            canvas.drawText(centreString, left + iconSize + space, textBottom, mPaint);
        } else {
            if (centreIcon == null && centreString == null) {
            } else {
                if (centreIcon == null) {
                    canvas.drawText(centreString, (width - centreWidth) / 2f, textBottom, mPaint);
                } else {
                    int left = (int) ((width - iconSize) / 2);
                    centreIcon.setBounds(left, iconTop, (int) iconSize + left, iconBottom);
                    centreIcon.draw(canvas);
                }
            }
        }
    }

    float fx, fy;
    boolean click;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x > width - 120 || x < 120) {//必需要点中按钮才接管
                    fx = x;
                    fy = y;
                    click = true;
                    return true;
                } else return false;
            case MotionEvent.ACTION_MOVE:
                if (click) {//判断是否为单击
                    float tempX = x - fx;
                    float tempY = y - fy;
                    if (Math.sqrt(tempX * tempX + tempY * tempY) > 15)
                        click = false;//移动超过10个像素就不为单击
                    return true;
                } else return false;
            default:
                if (click && listener != null) {//单击成立
                    if (x < 120 && (leftIcon != null || leftString != null)) {
                        listener.onClick(0);
                    } else if (x > width - 120 && (rightIcon != null || rightString != null)) {
                        listener.onClick(1);
                    }
                }
                return true;
        }
    }
}