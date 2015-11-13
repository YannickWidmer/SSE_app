package ch.yannick.context;

import android.app.Activity;
import android.os.Bundle;

public abstract class MyBaseActivity extends Activity {
	/*Copy paste from 
	http://stackoverflow.com/questions/11411395/how-to-get-current-foreground-activity-context-in-android
	*/
	  // Flags
    public static final int JUST_REFRESH = -1,INSERTPERSONNAGE=0, INSERTWEAPON=1,
    						UPDATEPERSONNAGE=3, UPDATEWEAPON = 4,
                            INSERTARMOR = 5, UPDATEARMOR = 6;//Flags

    // Added by me
	public abstract void react(String res, int Flag);
	
    protected RootApplication mRootApplication;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootApplication = (RootApplication)this.getApplicationContext();
        if(getActionBar() != null){
        	getActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }
    protected void onResume() {
        super.onResume();
        mRootApplication.setCurrentActivity(this);
    }
    protected void onPause() {
        clearReferences();
        super.onPause();
    }
    protected void onDestroy() {        
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences(){
        Activity currActivity = mRootApplication.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this))
            mRootApplication.setCurrentActivity(null);
    }
}