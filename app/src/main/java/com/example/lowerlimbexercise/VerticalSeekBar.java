package com.example.lowerlimbexercise;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;


public class VerticalSeekBar  extends AppCompatSeekBar{
    public VerticalSeekBar(@NonNull Context context) {
        super(context);
    }
    public VerticalSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public VerticalSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** 自身の幅と高さでSizeChangedを呼び出す */
    protected void onSizeChanged(){
        onSizeChanged(getWidth(), getHeight(), 0, 0); //old値は使用されないため指定しない
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw); //幅と高さを入れ替える
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        onSizeChanged();
    }

    /** スライドボタンのDrawableを設定する */
    @Override
    public void setThumb(Drawable thumb) {
        //onDrawで全ての描画を回転させるため、スライドボタンのDrawableをRotateDrawableでラップして相殺
        if(thumb!=null){
            thumb = new RotateDrawable();
            ((RotateDrawable) thumb).setFromDegrees(90);
        }
        super.setThumb(thumb);
        onSizeChanged(); //これを呼ばないと何故か描画が消える
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //noinspection SuspiciousNameCombination
        super.onMeasure(heightMeasureSpec, widthMeasureSpec); //縦横スペックを入れ替える
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth()); //幅と高さを入れ替える
    }

    @Override
    protected void onDraw(Canvas c) {
        final int canvasSaveCount = c.save(); //必ずカウントを保存

        c.rotate(-90);
        c.translate(-getHeight(),0);

        super.onDraw(c);

        c.restoreToCount(canvasSaveCount); //保存したカウントでキャンバス設定を復元
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                int i = getMax() - (int) (getMax() * event.getY() / getHeight());
                setProgress(i);
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }
}
