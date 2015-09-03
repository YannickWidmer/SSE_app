package ch.yannick.display.activityArsenal;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ch.yannick.display.activityMain.Inter_ListCarrier;
import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;


public class Act_Arsenal extends MyBaseActivity implements Inter_ListCarrier {

	private boolean isDualPane;
	private String LOG="Act_Arsenal";
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_arsenal);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		// Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = findViewById(R.id.Frame);
        isDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
	}
	
	@Override
	protected void onStop(){

        Log.d(LOG,"onStop");
		//Check if Details are showed
		Frag_WeaponDetail details = (Frag_WeaponDetail)getFragmentManager().findFragmentById(R.id.Frame);
		if (details != null) {
			// Remove it in that case, this is for changeOfOrientation since then it should not be shown again
			getFragmentManager().beginTransaction().remove(details).commitAllowingStateLoss();
		}
        super.onStop();
    }
	
	@Override
	public void showLongClick(int position,Long id) {		
		Log.d(LOG,"Show Details");
		
		if (isDualPane) {
			// We can display everything in-place with fragments, so update
			// Check what fragment is currently shown, replace if needed.
			Frag_WeaponDetail detailsOld = (Frag_WeaponDetail)getFragmentManager().findFragmentById(R.id.Frame);
			
			if(detailsOld != null && position == -1){
				getFragmentManager().beginTransaction().remove(detailsOld).commit();
				return;
			}
			
			if (detailsOld == null || detailsOld.getShownIndex() != position) {
				// Make new fragment to show this selection.
				Frag_WeaponDetail weaponsDetail = Frag_WeaponDetail.newInstance(position,id);
				// Execute a transaction, replacing any existing fragment
				// with this one inside the frame.
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.Frame, weaponsDetail);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	            ft.commitAllowingStateLoss();
			}
	      
		} else {
	            // Otherwise we need to launch a new activity to display
	            // the dialog fragment with selected text.
	            Intent intent = new Intent();
	            intent.setClass(this, Act_WeaponsDetail.class);
	            intent.putExtra("index",position);
	            intent.putExtra("id",id);
	            startActivity(intent);
	        }
		
	}

	@Override
	public void showClick(int position, Long id){
		if(isDualPane){
			showLongClick(position,id);
		}
	}

	@Override
	public void react(String res, int Flag) {
		Log.d(LOG, "Reacting");
		Frag_WeaponsList list = (Frag_WeaponsList)getFragmentManager().findFragmentById(R.id.List);
		if(list!=null && Flag == MyBaseActivity.INSERTWEAPON){
			list.upDate();
		}
	}
	
}
