package com.savysoft.esther.journalapp.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;


public class NoteEditText extends AppCompatEditText {
    private Rect mRect;
    private Paint mPaint;

    public NoteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRect = new Rect();
        mPaint = new Paint();
        //The Line style is declared
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //set the Color for the line to black
        mPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //The height of the line
        int height = getHeight();
        int lineHeight = getLineHeight();
        // The line count or rather number of line per page
        int count = height / lineHeight;
        if(getLineCount() > count){
            //for long text with scrolling
            count = getLineCount();
        }
        Rect rect = mRect;
        Paint paint = mPaint;
        //the first baseline
        int baseline = getLineBounds(0, rect);
        //now draw the remaining line
        for(int i = 0; i < count; i++){
            canvas.drawLine(rect.left, baseline + 1, rect.right, baseline + 1, paint);
            //finally next line
            baseline += getLineHeight();
        }
        super.onDraw(canvas);
    }
}
