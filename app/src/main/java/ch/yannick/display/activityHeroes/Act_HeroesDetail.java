package ch.yannick.display.activityHeroes;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;

/*
 * This activity is only used when in portrait mode, the Heroes List in 
 * Act_HeroesGestion  send the intent to start this activity, 
 * if the orientation changes this will be
 * finalized and the app returns to Act_HeroesGestion.
 */

public class Act_HeroesDetail extends MyBaseActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(R.id.Frame);
        setContentView(frame, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));


        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
            finish();
            return;
        }

        
        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            Frag_HeroesDetail details = new Frag_HeroesDetail();
            details.setArguments(getIntent().getExtras());
        	// Execute a transaction, replacing any existing fragment
			// with this one inside the frame.
            getFragmentManager().beginTransaction().add(R.id.Frame, details).commit();
        } 
    }

	@Override
	public void react(String res, int Flag) {
		// does nothing
	}
}
