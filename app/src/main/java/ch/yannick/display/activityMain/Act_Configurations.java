package ch.yannick.display.activityMain;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.QuickstartPreferences;
import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.context.services.MyRegistrationIntentService;
import ch.yannick.display.activityArmor.Act_Armures;
import ch.yannick.display.activityArsenal.Act_Arsenal;
import ch.yannick.display.activityHeroes.Act_HeroesGestion;
import ch.yannick.display.activityPlay.Act_Play;
import ch.yannick.intern.personnage.Character;

public class Act_Configurations extends MyBaseActivity {

    private static final String LOG = "Act:Configurations";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_configurations);
		if(getActionBar() != null){
        	getActionBar().setDisplayHomeAsUpEnabled(false);
        }
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    // TODO
                } else {
                        //TODO
                }
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, MyRegistrationIntentService.class);
            startService(intent);
        }
	}

    @Override
    public void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode == ConnectionResult.SUCCESS)
            return true;

        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
        } else {
            Toast.makeText(Act_Configurations.this, "This device doesnt support Google Play Services", Toast.LENGTH_SHORT).show();
            Log.i(LOG, "This device is not supported.");
            finish();
        }
        return false;
    }



    /*
    The actions performed when buttons are pressed
     */

    public void testConnection(View v){

    }

    public void startHeroes(View v){
		Intent intent = new Intent(this, Act_HeroesGestion.class);
		startActivity(intent);
	}
	
	public void startArsenal(View v){
		Intent intent = new Intent(this, Act_Arsenal.class);
		startActivity(intent);
	}

    public void startArmors(View v){
        Intent intent = new Intent(this, Act_Armures.class);
        startActivity(intent);
    }

	public void startGame(View v){
        ArrayList<Character> personnageList=((RootApplication)getApplication()).getDataManager().getAllCharacter();
        final ArrayAdapter<Character> personnageAdapter = new ArrayAdapter<Character>(this,R.layout.row_dialog,android.R.id.text1,personnageList);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_choose_hero);
        builder.setSingleChoiceItems(personnageAdapter, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                Intent intent = new Intent(Act_Configurations.this.getApplication(), Act_Play.class);
                mRootApplication.getState(personnageAdapter.getItem(position).getId());
                startActivity(intent);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


	@Override
	public void react(String res, int Flag) {
		// does nothing		
	}
}
