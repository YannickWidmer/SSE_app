package ch.yannick.display.activityMental;

import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.intern.state.MentalState;

public class Act_Mental extends MyBaseActivity implements VectorListener {

    private AsyncPathComputer task;
    private static String LOG = "Act_Mental";
    private static int pathLength = 80, maxSpeed = 1/10;
    private Vector position, touchPosition;
    private static double forceScale = 0.001f;
    private int processPosition = 0; // 0 initial, 1 speed touched Waiting 2 done show State

    @Override
    public void react(String res, int Flag) {
        // NOthing
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_mental);
        position = new Vector(0,0);
        ((GameView)findViewById(R.id.mental_display)).setListener(this);

        long id = getIntent().getLongExtra("State",-1);
        this.position = ((RootApplication) getApplication()).getState(id).getMentalPosition();
        ((GameView)findViewById(R.id.mental_display)).setPosition(position);
    }

    private void setPath(Vector[] path){
        ((GameView)findViewById(R.id.mental_display)).setPath(path, touchPosition);
        position = path[pathLength-1];

        long id = getIntent().getLongExtra("State",-1);
        ((RootApplication) getApplication()).getState(id).setMentalState(position);
    }

    @Override
    public void onGetingVector(Vector touch) {
        switch(processPosition) {
            case 0:
                processPosition = 1;
                touchPosition = touch;
                task = new AsyncPathComputer(position, touch);
                task.execute();
                break;
            case 1:// Waiting
                break;
            case 2:
                Intent returnIntent;
                returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                super.finish();
        }
    }

    @Override
    public String done(){
        processPosition = 2;
        return getResources().getString(MentalState.getState(position).getStringId());
    }

    @Override
    public boolean onTouchEvent ( MotionEvent event ) {
        // create a rect for storing the window rect
        Rect r = new Rect ( 0, 0, 0, 0 );
        // retrieve the windows rect
        this.getWindow ().getDecorView ().getHitRect ( r );
        // check if the event position is inside the window rect
        boolean intersects = r.contains ( (int) event.getX (), (int) event.getY () );
        // if the event is not inside then we can close the activity
        if ( !intersects ) {
            // notify that we consumed this event
            return true;
        }

        // let the system handle the event
        return super.onTouchEvent(event);
    }

    private class AsyncPathComputer extends AsyncTask<Void, Void, Void> {
        private Vector position, speed;
        private Vector[] path;

        public AsyncPathComputer(Vector p, Vector relativeSpeed) {
            super();
            position = p;
            relativeSpeed = p.subs((relativeSpeed));
            speed = relativeSpeed.multThis(100f / relativeSpeed.size() / relativeSpeed.size());
            path = new Vector[pathLength];
        }

        @Override
        protected Void doInBackground(Void... params) {
            for(int i =0;i<pathLength;++i){
                speed.addThis(MentalState.getForce(position),- forceScale);
                //Log.d(LOG, "speed " + speed.toString());
                double s = speed.size();
                if(s > maxSpeed)
                    speed.mult(maxSpeed/s);
                position.addThis(speed.mult(1/((float)(1+i))));
                if(Math.abs(position.getX())> MentalState.halfWidth)
                    position.setX(MentalState.halfWidth* Math.signum(position.getX()));
                if(Math.abs(position.getY())> MentalState.halfHeight)
                    position.setY(MentalState.halfHeight* Math.signum(position.getY()));

                path[i] = position.copy();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void id) {
           setPath(path);
        }
    }


}
