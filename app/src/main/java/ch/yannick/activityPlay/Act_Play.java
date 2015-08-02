package ch.yannick.activityPlay;

import android.os.Bundle;
import android.util.Log;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.intern.State;

public class Act_Play extends MyBaseActivity {

	private String LOG="Act_Arsenal";
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_play);
		long id = getIntent().getLongExtra("id",-1);
        State state = ((RootApplication) getApplication()).getState(id);

		((Frag_PlayControl)getFragmentManager().findFragmentById(R.id.play)).setState(state);
        ((Frag_PlayAttributes)getFragmentManager().findFragmentById(R.id.attributes)).setState(state);
	}
	
	
	public void setDisplayer(int value){
		((Frag_Displayer)getFragmentManager().findFragmentById(R.id.display)).setSkill(value);
	}

	@Override
	public void react(String res, int Flag) {
		Log.d(LOG, "Does nothing onReact");	
	}
	
}