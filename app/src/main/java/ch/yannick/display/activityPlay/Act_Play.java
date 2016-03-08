package ch.yannick.display.activityPlay;

import android.os.Bundle;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;

public class Act_Play extends MyBaseActivity {

	private String LOG="Act_Arsenal";
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_play);
	}
	
	
	public void setDisplayer(int value){
		((Frag_Displayer)getFragmentManager().findFragmentById(R.id.display)).setSkill(value);
	}

	@Override
	public void onStart(){
		super.onStart();
		react(null,0);
	}

	@Override
	public void react(String res, int Flag) {
		((Frag_PlayControl)getFragmentManager().findFragmentById(R.id.play)).refresh();
		((Frag_Actions)getFragmentManager().findFragmentById(R.id.actions)).refresh();
		if(getFragmentManager().findFragmentById(R.id.frag_modifs)!= null)
			((Frag_modif)getFragmentManager().findFragmentById(R.id.frag_modifs)).refresh();
	}
	
}