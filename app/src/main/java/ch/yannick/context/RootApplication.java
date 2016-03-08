/**
 * 
 */
package ch.yannick.context;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

import ch.yannick.context.datamanagement.DataManager;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.personnage.Character;
import ch.yannick.intern.state.State;
import ch.yannick.intern.usables.Role;
import ch.yannick.intern.usables.Weapon;


/**
 * @author yannick
 *
 */
public class RootApplication extends Application {

	private static final String LOG="RootApplication";
    public Weapon mCurrentUsable;
	private DataManager myManager;
	private MyBaseActivity mCurrentActivity = null;
	private List<MyBaseActivity> mActivityList = new ArrayList<>();
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

	public DataManager getDataManager(){
		return myManager;
	}

	public void react(String res, int flag, long elementId){
		Log.d(LOG,"react to "+res);

		if(mCurrentActivity != null){
            switch (flag){
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

            if(flag == MyBaseActivity.UPDATEPERSONNAGE && states.get(elementId)!= null) {
                refreshState(elementId);
            }

            for(MyBaseActivity act:mActivityList){
                act.react(res,flag);
                Log.d(LOG," reacting activity "+act);
            }
		}
	}

	public void setCurrentActivity(MyBaseActivity mCurrentActivity){
		this.mCurrentActivity = mCurrentActivity;
	}

	public void addActivity(MyBaseActivity myBaseActivity) {
		mActivityList.add(myBaseActivity);
	}

	public State getState(Long id){
		if(states.get(id) == null){
			Character p = myManager.getCharacter(id);
			states.put(id,new State(p));
            states.get(id).getInventoryFromSQL(getDataManager());
		}
		mCurrentState = states.get(id);
		return mCurrentState;
	}

	public State getCurrentState(){
		return mCurrentState;
	}

	public void refreshState(Long id) {
		if(states.get(id) != null){
			states.get(id).setPersonnage(myManager.getCharacter(id));
            states.get(id).getInventoryFromSQL(getDataManager());
        }
	}

    // XML Parcing stuff

    public int getStringResource(String thingie) {
            String[] split = thingie.split("/");
            String pack = split[0].replace("@", "");
            String name = split[1];
            return getResources().getIdentifier(name, pack, getPackageName());
    }

	public void removeActivity(MyBaseActivity myBaseActivity) {
		mActivityList.remove(myBaseActivity);
		if(mCurrentActivity == myBaseActivity)
			mCurrentActivity = null;
	}

	private class AsyncInit extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                int[] ids = new int[]{R.raw.combat_actions, R.raw.artist_actions,R.raw.city_actions,R.raw.physique_actions,R.raw.ranger_actions,
                        R.raw.talents,R.raw.roles};
                for(int i:ids){
                    MyXmlParser myParser = new MyXmlParser(getResources().openRawResource(i),parser,RootApplication.this);
					switch (myParser.main.name){
                        case "Actions":
                            Action.parse(myParser);
                            Log.d(LOG,"actions now "+Action.values());
                            break;
						case "Roles":
							Role.parse(myParser);
							break;
                        case "Talents":
                            Talent.parse(myParser);
                    }
                }
            } catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
}
