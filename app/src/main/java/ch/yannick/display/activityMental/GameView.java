package ch.yannick.display.activityMental;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.TimeUnit;

import ch.yannick.context.R;
import ch.yannick.intern.state.MentalState;


/**
 * Created by Vincent on 1/18/2015.
 *
 * This class pretty much initiates the game and handles
 * pausing and resuming of the game
 * It also contains the game loop and game thread
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    private static final String LOG = "GameView";
    private final static int mentalSize = 1;
    private SurfaceHolder holder;
    private VectorListener listener;
    private Bitmap background;
    private Bitmap[] brightStates;

    private Vector[] path;
    private int currentPosition =0;
    private Vector currentPositionVector, touchPosition;
    private double eachDuration = 1;
    private double totalDuration = 0;
    private double currentTime = 0;
    private double scale = 0;
    private Paint black,red,blue,withe, title;

    private String message="";

    private int backgroundWith,stateSize, width,height;

    private volatile boolean running = false;
    private Thread renderThread;

    public  GameView(Context con,AttributeSet attrs, int style){
        super(con,attrs,style);
        init();
    }

    public GameView(Context context,AttributeSet attrs){
        super(context,attrs);
        init();
    }

    public GameView(Context context) {
        super(context);
        init();
    }

    private void init(){
        Log.d(LOG,"init");
        holder = getHolder();
        holder.addCallback(this);
        currentPositionVector = new Vector(0,0);
        touchPosition = new Vector(0,0);
        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        black.setStyle(Paint.Style.STROKE);
        black.setColor(Color.BLACK);
        withe = new Paint(Paint.ANTI_ALIAS_FLAG);
        withe.setStyle(Paint.Style.STROKE);
        withe.setColor(Color.argb(255,255,255,255));

        red = new Paint();
        red.setColor(Color.RED);
        red.setStrokeWidth(10);
        blue = new Paint(Paint.ANTI_ALIAS_FLAG);
        blue.setTextSize(getResources().getDimension(R.dimen.normal_text));
        blue.setColor(Color.DKGRAY);
        //blue.setStyle(Paint.Style.FILL);
        blue.setTextAlign(Paint.Align.CENTER);
        title = new Paint(Paint.ANTI_ALIAS_FLAG);
        title.setTextSize(getResources().getDimension(R.dimen.big_text));
        title.setStyle(Paint.Style.FILL);
        title.setStrokeWidth(2);
        title.setTextAlign(Paint.Align.CENTER);
        title.setColor(Color.CYAN);


        /*BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),R.drawable.mentalmap,options);
        backgroundWith = options.outWidth;
        BitmapFactory.decodeResource(getResources(),R.drawable.mentalstate,options);
        stateSize = options.outWidth;*/

    }

    private void resume() {
        //BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inSampleSize = (backgroundWith)/width;
        background = BitmapFactory.decodeResource(getResources(),R.drawable.mentalmap);
        Matrix m = new Matrix();
        Log.d(LOG,"scaling "+((float) width) / background.getWidth());
        Log.d(LOG, "scaling " +  ((float) width) / ((float) backgroundWith));

        m.setScale(((float) width) / (float) background.getWidth(), ((float) height) / background.getHeight());
        background = Bitmap.createBitmap(background, 0, 0,background.getWidth(), background.getHeight(),m,true);
        //brightStates = new Bitmap[MentalState.values().length];

        //Bitmap temp = BitmapFactory.decodeResource(getResources(),R.drawable.mentalstate);
        /*for(MentalState ms:MentalState.values()){
            m.reset();
            m.postScale((float) scale * ms.getRadius() / stateSize, (float) scale * ms.getRadius()/stateSize);
            brightStates[i] = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), m, false);
            ++i;
        }*/
        //temp.recycle();
        scale = width/MentalState.halfWidth/2;
        Canvas canvas = holder.lockCanvas();
        if(canvas != null){
            dDraw(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setListener(VectorListener listener){
        this.listener = listener;
    }

    @SuppressLint("WrongCall")
    public void setPosition(Vector position){
        this.currentPositionVector = position;
        Canvas canvas = holder.lockCanvas();
        if(canvas != null){
            dDraw(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setPath(Vector[] path, Vector touchPosition){
        this.touchPosition = touchPosition;
        running = false;
        if(renderThread != null) {
            while (renderThread.isAlive()) {
                try {
                    renderThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        this.path = path;
        running = true;
        currentTime = 0;
        totalDuration =path.length * eachDuration;
        renderThread = new Render(holder);
        renderThread.start();
    }

    @Override
    public void onDraw(Canvas canv){
        //super.onDraw(canv);
        dDraw(canv);
    }

    private void dDraw(Canvas canv){
        canv.drawBitmap(background, 0, 0, null);
        //canv.drawARGB(200,200,200,1);
        //canv.drawColor(Color.TRANSPARENT);
        canv.translate(width / 2, height / 2);

        for(MentalState ms:MentalState.values()){
            drawVector(canv,ms.getPosition(),withe,ms.getRadius(),getResources().getString(ms.getStringId()));
        }
        drawVector(canv, currentPositionVector, red, mentalSize,"");
        drawVector(canv, touchPosition, withe, (int) currentTime * 10, "");

        canv.drawText(message, 0, 0, title);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            float x = (float) (event.getX()/scale-MentalState.halfWidth);
            float y = (float) (-event.getY()/scale+ MentalState.halfHeight);
            Log.d(LOG,"x " +x+ "  y "+y);
            Vector v = new Vector(x,y);
            listener.onGetingVector(v);
            return true;
        }
        return false;
    }

    private void drawVector(Canvas canv, Vector vec, Paint paint, int radius,String name){
        int x = (int) ((vec.getX())*scale);
        int y = (int) ((vec.getY())*scale);
        radius = (int) ((radius)*scale);
        canv.drawCircle(x, -y , mentalSize, paint);
        canv.drawText(name, x, -y, blue);
        canv.drawCircle(x, -y, radius, paint);
    }

    private synchronized void pause() {
        running = false;
        if(renderThread != null) {
            while (renderThread.isAlive()) {
                try {
                    renderThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        background.recycle();
        //for(Bitmap btm:brightStates)
        //    btm.recycle();
    }

    private boolean update(long delta) {
        //Log.d(LOG,"update");
        if(path == null)
            return false;
        currentTime += delta;
        if(currentTime<totalDuration)
            currentPosition = (int) (currentTime/eachDuration);
        else {
            currentPosition = path.length - 1;
        }
        if(currentTime>totalDuration*1.5){
            message = listener.done();
            return false;
        }
        currentPositionVector = path[currentPosition];
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        Log.d(LOG,"onMeasure");

        int borderW = getPaddingLeft()+getPaddingRight();
        int borderH = getPaddingBottom()+getPaddingTop();
        int desiredWidth = solveMeasureSpec(widthMeasureSpec,backgroundWith+borderW);
        int desiredHeight = solveMeasureSpec(heightMeasureSpec,desiredWidth*MentalState.halfHeight/MentalState.halfWidth+borderH);
        if(desiredHeight < desiredWidth*MentalState.halfHeight/MentalState.halfWidth+borderH)
            desiredWidth = desiredHeight*MentalState.halfWidth/MentalState.halfHeight+borderW;

        Log.d(LOG,"onMeasure Width "+width+" height " + height);
        setMeasuredDimension(desiredWidth, desiredHeight);
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(LOG, "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(LOG,"surfaceChanged width "+width+" height "+ height);
        this.width = width;
        this.height = height;
        resume();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(LOG,"destroyed");
        pause();
    }

    class Render extends Thread{
        private SurfaceHolder holder;
        private Canvas canvas;
        public Render(SurfaceHolder handler){
            holder= handler;
        }

        @SuppressLint("WrongCall")
        public void run() {

            long startTime = System.nanoTime();
            while(running) {
                if(!holder.getSurface().isValid())
                    continue;


                long deltaTime  = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)*6/100; // 60eme de seconde
                startTime = System.nanoTime();

                if (deltaTime > 3){
                    deltaTime = 3;
                }

                running = update(deltaTime);

                canvas = holder.lockCanvas();
                if(canvas != null){
                    dDraw(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}