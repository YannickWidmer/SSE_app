package ch.yannick.display.activityPlay;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.personnage.Limb;
import ch.yannick.intern.state.State;
import ch.yannick.display.views.ValueControler;

public class Dialog_fatigue extends MyBaseActivity {
    private static String LOG = "Dialog fatigue";
	private State st;
	private int stamina;
    private Limb which;
    private Action action;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        st = mRootApplication.getState(getIntent().getLongExtra("id", 0));
        setContentView(R.layout.dialog_fatigue);


        if (getIntent().hasExtra("stamina"))
            stamina = getIntent().getIntExtra("stamina", 0);
        else {
            which = Limb.valueOf(getIntent().getStringExtra("which"));
            action = Action.valueOf(getIntent().getStringExtra("action"));
            stamina = st.getFatigue(action, which);
        }
        ((ValueControler) findViewById(R.id.stamina_used_value)).setValue(stamina);
    }

        @Override
	public void react(String res, int Flag) {
	}


    public void cancel(View v){
        Intent returnIntent;
        returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        super.finish();
    }

    public void confirmed(View v){
        Intent returnIntent;
        returnIntent = new Intent();
        returnIntent.putExtra("fatigue",
                ((ValueControler)findViewById(R.id.stamina_used_value)).getValue());
        setResult(RESULT_OK, returnIntent);
        super.finish();
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
        return super.onTouchEvent ( event );
    }
}
