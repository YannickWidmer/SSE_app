/**
 * 
 */
package ch.yannick.context;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.util.LongSparseArray;
import android.widget.Toast;

import ch.yannick.context.datamanagement.DataManager;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.items.Weapon;
import ch.yannick.intern.personnage.Personnage;
import ch.yannick.intern.state.State;


/**
 * @author yannick
 *
 */
public class RootApplication extends Application {

	private static final String LOG="RootApplication";
    public Weapon currentWeapon;
	private DataManager myManager;
	private MyBaseActivity mCurrentActivity = null;
	private LongSparseArray<State> states;

	@Override
	public void onCreate(){
	    super.onCreate();
	    // Initialize the singletons so their instances
	    // are bound to the application process.
		myManager=new DataManager(this);
		states = new LongSparseArray<State>();

		Talent.init(getResources().openRawResource(R.raw.talents));
	    //new AsyncInit(this).execute();
	    Log.d(LOG,"App created");
	}
	
 	@Override
	public void onTerminate()
	{
		super.onTerminate();
		Log.d(LOG,"App Terminated");
		// only in Emulator
	}
	 
	@Override
	public void finalize()
	{
		Log.d(LOG,"finalizing App");
		try {
			super.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public DataManager getDataManager(){
		return myManager;
	}

	public void react(String res, int Flag, long elementId){
		Log.d(LOG,"react to "+res);

		if(mCurrentActivity != null){
            switch (Flag){
                case MyBaseActivity.INSERTWEAPON:
                    Toast.makeText(this,R.string.weapon_created,Toast.LENGTH_SHORT).show();
					break;
                case MyBaseActivity.UPDATEWEAPON:
                    Toast.makeText(this,R.string.weapon_saved,Toast.LENGTH_SHORT).show();
                    break;
                case MyBaseActivity.INSERTARMOR:
                    Toast.makeText(this,R.string.armor_created,Toast.LENGTH_SHORT).show();
                    break;
                case MyBaseActivity.UPDATEARMOR:
                    Toast.makeText(this,R.string.armor_saved,Toast.LENGTH_SHORT).show();
                    break;
                case MyBaseActivity.INSERTPERSONNAGE:
                    Toast.makeText(this,R.string.hero_created,Toast.LENGTH_SHORT).show();
                    break;
                case MyBaseActivity.UPDATEPERSONNAGE:
                    Toast.makeText(this,R.string.hero_saved,Toast.LENGTH_SHORT).show();
                    break;
            }

            if(Flag == MyBaseActivity.UPDATEPERSONNAGE && states.get(elementId)!= null) {
                refreshState(elementId);
            }

            mCurrentActivity.react(res, Flag);
		}
	}
	
	public Activity getCurrentActivity(){
        return mCurrentActivity;
	}


	public void setCurrentActivity(MyBaseActivity mCurrentActivity){
		this.mCurrentActivity = mCurrentActivity;
	}
	
	public State getState(Long id){
		if(states.get(id) == null){
			Personnage p = myManager.getPersonnage(id);
			states.put(id,new State(p));
		}
		return states.get(id);
	}

	public void refreshState(Long id) {
		if(states.get(id) != null){
			states.get(id).setPersonnage(myManager.getPersonnage(id));
		}
	}
	
}
