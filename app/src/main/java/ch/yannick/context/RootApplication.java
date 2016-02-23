/**
 * 
 */
package ch.yannick.context;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import ch.yannick.context.datamanagement.DataManager;
import ch.yannick.context.mysqlconnection.ConnectionTest;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.personnage.Personnage;
import ch.yannick.intern.state.State;
import ch.yannick.intern.usables.Role;
import ch.yannick.intern.usables.Weapon;


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
	private State mCurrentState;

	@Override
	public void onCreate(){
	    super.onCreate();
	    // Initialize the singletons so their instances
	    // are bound to the application process.
		myManager=new DataManager(this);
		states = new LongSparseArray<State>();

	    new AsyncInit().execute();
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
		Log.d(LOG, "finalizing App");
		try {
			super.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void testConnection(){
		new ConnectionTest().testConnection(this);
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
		mCurrentState = states.get(id);
		return mCurrentState;
	}

	public State getCurrentState(){
		return mCurrentState;
	}

	public void refreshState(Long id) {
		if(states.get(id) != null){
			states.get(id).setPersonnage(myManager.getPersonnage(id));
		}
	}

    // XML Parcing stuff

    public int getStringResource(String thingie) {
            String[] split = thingie.split("/");
            String pack = split[0].replace("@", "");
            String name = split[1];
            return getResources().getIdentifier(name, pack, getPackageName());
    }

	private class AsyncInit extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                Action.init(getResources().openRawResource(R.raw.actions), parser, RootApplication.this);
                Talent.init(getResources().openRawResource(R.raw.talents), parser, RootApplication.this);
                Role.init(getResources().openRawResource(R.raw.roles), parser, RootApplication.this);
            } catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
}
