package ch.yannick.display.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import ch.yannick.context.R;

/**
 * Created by Yannick on 05.03.2015.
 */
public class ValueControler extends Button {
    private int mValue, mWidth,mHeight;
    private int lowerBound, upperBound;
    private boolean showNumber=true;
    private static String LOG="ValueController";



    public ValueControler(Context c, AttributeSet attr){
        super(c, attr);
        TypedArray a = c.getTheme().obtainStyledAttributes(
                attr,
                R.styleable.ValueControler,0,0);
        init(a);
    }

    public ValueControler(Context c){
        super(c);
        TypedArray a = c.getTheme().obtainStyledAttributes(R.styleable.ValueControler);
        init(a);
    }

    private void init(TypedArray tp){
        this.setBackground(getResources().getDrawable(R.drawable.gauchedroite));
        try {
            showNumber = tp.getBoolean(R.styleable.ValueControler_show_number, true);
            upperBound = tp.getInt(R.styleable.ValueControler_upper_bound, -1);
            lowerBound= tp.getInt(R.styleable.ValueControler_lower_bound,-1);
            Log.d(LOG," shownumber "+showNumber +" lowerbound "+lowerBound+" upper bound "+upperBound);
        }catch(Exception e) {
            Log.d(LOG, "wasn't able to resolve xml values");
            e.printStackTrace();
        }finally{
            tp.recycle();
        }
        if(lowerBound != -1)
            mValue = lowerBound;
        setValue(mValue);
        setListener(null);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float textSize = getTextSize();

        mWidth = (int)(4* textSize)+getPaddingLeft()+getPaddingRight();
        mHeight = (int)textSize+getPaddingBottom()+getPaddingTop()+6;

        mWidth = solveMeasureSpec(widthMeasureSpec,mWidth);
        mHeight = solveMeasureSpec(heightMeasureSpec,mHeight);

        setMeasuredDimension(mWidth,mHeight);
    }

    private int solveMeasureSpec(int measureSpec, int desired){
        int given = MeasureSpec.getSize(measureSpec);
        switch(MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.EXACTLY:
                desired = given;
                break;
            case MeasureSpec.AT_MOST:
                desired = Math.min(desired, given);
                break;
        }
        return desired;
    }

    public void setListener(final ValueChangeListener listener){
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(event.getX()>mWidth/2){
                        if(upperBound ==-1 || mValue < upperBound)
                            ++mValue;
                    }else {
                        if(lowerBound ==-1 || mValue> lowerBound)
                            --mValue;
                    }

                    if(listener != null)
                        listener.onChangeValue(mValue);
                    setValue(mValue);
                    return true;
                }
                return false;
            }
        });
    }

    public void setValue(int value){
        Log.d(LOG,"setVaue ");
        mValue = value;
        if(showNumber)
            setText(mValue+"");
    }

    public void setShowNumber(boolean showNumber){
        this.showNumber = showNumber;
        if(!showNumber)
            setText("");
    }

    public int getValue(){
        return mValue;
    }

    public void setBounds(int lower, int upper){
        upperBound = upper;
        lowerBound = lower;
    }
}
