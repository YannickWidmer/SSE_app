package ch.yannick.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import ch.yannick.context.R;

/**
 * Created by Yannick on 21.07.2015.
 */
public class JaugeView extends View {

    private static String LOG = "View Jauge";
    private int textSize, mHeight, mWidth;
    private int firstValue=1,secondValue=1, thirdValue=5;
    private Paint firstColor,secondColor,thirdColor, textPaint;

    public JaugeView(Context c, AttributeSet attr){
        super(c,attr);
        Log.d(LOG, "Constructor(c,attr)");
        TypedArray a = c.getTheme().obtainStyledAttributes(
                attr,
                R.styleable.JaugeView,0,0);
        init(a);
    }

    public JaugeView(Context c){
        super(c);
        Log.d(LOG,"Constructor(c)");
        TypedArray a = c.getTheme().obtainStyledAttributes(R.styleable.JaugeView);
        init(a);
    }

    private void init(TypedArray tp){
        firstColor = new Paint();
        firstColor.setStyle(Paint.Style.FILL);
        secondColor = new Paint();
        secondColor.setStyle(Paint.Style.FILL);
        thirdColor = new Paint();
        thirdColor.setStyle(Paint.Style.FILL);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.DKGRAY);

        try {
            textSize = tp.getDimensionPixelOffset(R.styleable.JaugeView_android_textSize,10);
            textPaint.setTextSize(textSize);
            textPaint.setColor(getResources().getColor(R.color.white));
            LinearGradient shader = new LinearGradient(0,0,0,40,
                    Color.WHITE,tp.getColor(R.styleable.JaugeView_first_color, Color.WHITE),
                    Shader.TileMode.MIRROR);
            firstColor.setShader(shader);
            shader = new LinearGradient(0,0,0,40,
                    Color.WHITE,tp.getColor(R.styleable.JaugeView_second_color, Color.BLACK),
                    Shader.TileMode.CLAMP);
            secondColor.setShader(shader);

            shader = new LinearGradient(0,0,0,40,
                    Color.WHITE,tp.getColor(R.styleable.JaugeView_third_color, Color.WHITE),
                    Shader.TileMode.CLAMP);
            thirdColor.setShader(shader);

            Log.d(LOG, "resolved values");
            Log.d(LOG, "textSize "+textSize);
        }catch(Exception e) {
            Log.d(LOG, "wasn't able to resolve xml values");
        }finally{
            tp.recycle();
        }
    }

    public void setValues(int first, int second, int third){
        firstValue = first;
        secondValue = second;
        thirdValue = third;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canv){
        int top = getPaddingTop();
        int left = getPaddingLeft()+mHeight/2;
        int sum = firstValue+secondValue+thirdValue;
        if(sum == 0)
            sum=1;

        canv.drawCircle(left, top + mHeight / 2, mHeight/2,firstValue!=0?firstColor:thirdColor);
        canv.drawCircle(left + mWidth, top + mHeight / 2, mHeight / 2, firstValue!=sum?thirdColor:firstColor);

        canv.drawRect(left, top, left + mWidth * firstValue / sum, top + mHeight, firstColor);
        canv.drawRect(left + mWidth * firstValue / sum,
                top, left + mWidth * (firstValue+secondValue) / sum, top + mHeight, secondColor);
        canv.drawRect(left + mWidth * (firstValue + secondValue) / sum, top, left + mWidth, top + mHeight, thirdColor);
        //canv.drawCircle(left + mWidth * (firstValue + secondValue) / sum, top + mHeight / 2, mHeight / 2, thirdColor);
        //canv.drawCircle(left + mWidth * firstValue / sum, top + mHeight / 2, mHeight / 2, firstColor);

        canv.drawText("" + firstValue, left, top + mHeight - 5, textPaint);
        canv.drawText(""+sum,left+mWidth-textSize/(sum<10?3:1),top+mHeight-5,textPaint);
        if(secondValue != 0)
            canv.drawText(""+secondValue,left+mWidth * firstValue / sum-textSize/(secondValue<10?3:1),top+mHeight-5,textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = resolveMeasureSpec(widthMeasureSpec,100);
        mHeight = resolveMeasureSpec(heightMeasureSpec,textSize + getPaddingTop()+getPaddingBottom());
        setMeasuredDimension(mWidth,mHeight);
        mWidth -= getPaddingLeft()+getPaddingRight();
        mHeight -= getPaddingBottom()+getPaddingRight();
        mWidth -= mHeight;
    }

    private int resolveMeasureSpec(int measureSpec, int desiredSize){
        int given = MeasureSpec.getSize(measureSpec);
        switch(MeasureSpec.getMode(measureSpec)){
            case MeasureSpec.AT_MOST:
                return Math.min(given,desiredSize);
            case MeasureSpec.EXACTLY:
                return given;
            default: return desiredSize;
        }
    }
}
