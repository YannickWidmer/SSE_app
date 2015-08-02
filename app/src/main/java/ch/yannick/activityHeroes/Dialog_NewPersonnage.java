package ch.yannick.activityHeroes;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;
import ch.yannick.context.RootApplication;

public class Dialog_NewPersonnage extends MyBaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_new_heroes);
	}
	
	public void confirmed(View v){
		final EditText input = (EditText) findViewById(R.id.nom);
		((RootApplication) getApplication()).getDataManager().newPersonnage(input.getText().toString());
		super.finish();
	}
	
	public void cancel(View v){
		super.finish();
	}

	@Override
	public void react(String res, int Flag) {
		// does nothing
	}
}


