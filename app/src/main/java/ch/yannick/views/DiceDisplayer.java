package ch.yannick.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.yannick.context.R;
import ch.yannick.enums.Dice;
import ch.yannick.intern.Choice;

public class DiceDisplayer extends View{
    private static float textShift = 1.3f;
	private boolean showingNumber = true;
	private Paint paint,redPaint;
	private String text;
	private int height,width, textSize;
    private List<Dice> dices;
	private Map<Dice,Bitmap> diceBitMapsScaled;
	private static Map<Dice,Bitmap> diceBitMaps;
    private static String LOG = "DiceDisplayer";
    private boolean silent = true;

	public DiceDisplayer(Context c, AttributeSet attr){
		super(c,attr);
        Log.d(LOG,"DiceDisplayer Constructor(c,attr)");
		TypedArray a = c.getTheme().obtainStyledAttributes(
		        attr,
		        R.styleable.DiceDisplayer,0,0);
        init(a);
	}

    public DiceDisplayer(Context c){
        super(c);
        Log.d(LOG,"DiceDisplayer Constructor(c)");
        TypedArray a = c.getTheme().obtainStyledAttributes(R.styleable.DiceDisplayer);
        init(a);
    }

	private void init(TypedArray tp){
        showingNumber =false;
        dices = new ArrayList<>();
        try {
            showingNumber = tp.getBoolean(R.styleable.DiceDisplayer_showNumber, false);
            textSize = tp.getDimensionPixelOffset(R.styleable.DiceDisplayer_android_textSize,10);
            Log.d(LOG, "resolved values");
            Log.d(LOG, showingNumber?"is ":"not "+"showing Number ");
            Log.d(LOG, "textSize "+textSize);
        }catch(Exception e) {
            Log.d(LOG, "wasn't able to resolve xml values");
        }finally{
            tp.recycle();
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.DKGRAY);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.RIGHT);

        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setColor(Color.BLACK);
        redPaint.setTextSize(textSize);
        redPaint.setTextAlign(Paint.Align.CENTER);

        text="";
        diceBitMapsScaled = new HashMap<>();
        if(diceBitMaps == null){
			diceBitMaps = new HashMap<>();
            for(Dice d:Dice.values())
                diceBitMaps.put(d,BitmapFactory.decodeResource(getResources(),d.getDrawableId()));
		}
	}

    public void changeSilent(boolean silent){
        this.silent = silent;
    }

    public void setTextSize(int textSize){
        this.textSize = textSize;
        paint.setTextSize(textSize);
        invalidate();
        requestLayout();
    }

    @Override
	protected void onDraw(Canvas canv){
        int h = height -getPaddingTop()-getPaddingBottom();
        h = Math.min(h,textSize);
        int top = getPaddingTop();
        int bottom = height - getPaddingBottom();
        int right = width-getPaddingRight();
		if(showingNumber){
			canv.drawText(text, right,bottom-4, paint);
            right -= textShift*h
            ;
		}

		for(Dice d:dices){
            right -= h;
			canv.drawBitmap(diceBitMapsScaled.get(d),right,top,null);
            if(!silent)
                canv.drawText(d.getEyes()+"", right+h*0.5f,bottom-4,redPaint);
		}
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int calculatedHeight = textSize+ getPaddingBottom()+getPaddingTop();
        float squares =(dices.size());
        if (showingNumber)
            squares+=textShift;
        int calculatedWidth = (int) (textSize * squares +getPaddingLeft()+getPaddingRight());

        Log.d(LOG,"onMeasure calculated width "+ calculatedWidth+" height "+calculatedHeight);
        calculatedHeight = resolveMeasureSpec(heightMeasureSpec, calculatedHeight);
        calculatedWidth = resolveMeasureSpec(widthMeasureSpec, calculatedWidth);
        Log.d(LOG,"onMeasure resolved width "+ calculatedWidth+" height "+calculatedHeight);

        setMeasuredDimension(calculatedWidth,calculatedHeight);
    }

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(LOG,"onSizeChanged width "+w+" height "+ h);
		height=h; width = w;
        h = Math.min(h-getPaddingTop()-getPaddingBottom(),textSize);
        h = Math.max(h,1);
		for(Dice d:Dice.values()){
			//Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
			diceBitMapsScaled.put(d,Bitmap.createScaledBitmap(diceBitMaps.get(d),h,h, false));
		}
	}
	
	public void set(Choice c){
		setNumber(c.getRest());
		setDices(c.getDices());
	}
	
	public void setNumber(int n){
		if(n<0) 
			text = "- "+(-n); 
		else
			text = "+ "+n;
		if(n==0)
			text ="";
		invalidate();
		requestLayout();
	}
	
	public void showNumber(boolean show){
		showingNumber=show;
		invalidate();
		requestLayout();
	}

    public void setDices(List<Dice> dices){
        this.dices = new ArrayList<Dice>();
        for(Dice d:dices)
            this.dices.add(d);
        invalidate();
        requestLayout();
    }

    public void setDice(Dice d){
        this.dices = new ArrayList<>();
        dices.add(d);
        invalidate();
        requestLayout();
    }
	
	public void setDices(Map<Dice,Integer> diceMap){
        dices = new ArrayList<>();
        for(Dice d:Dice.values()){
            if(diceMap.containsKey(d)){
                for(int i =0; i<diceMap.get(d);++i)
                    dices.add(d);
            }
        }
		invalidate();
		requestLayout();
	}

    public List<Dice> getDices() {
        return dices;
    }
}
