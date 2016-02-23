package ch.yannick.display.activityMain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.display.activityArmor.Act_Armures;
import ch.yannick.display.activityArsenal.Act_Arsenal;
import ch.yannick.display.activityHeroes.Act_HeroesGestion;
import ch.yannick.display.activityPlay.Act_Play;
import ch.yannick.intern.personnage.Personnage;

public class Act_Configurations extends MyBaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_configurations);
		if(getActionBar() != null){
        	getActionBar().setDisplayHomeAsUpEnabled(false);
        }
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
        ArrayList<Personnage> personnageList=((RootApplication)getApplication()).getDataManager().getAllPersonnage();
        final ArrayAdapter<Personnage> personnageAdapter = new ArrayAdapter<Personnage>(this,R.layout.row_dialog,android.R.id.text1,personnageList);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_choose_hero);
        builder.setSingleChoiceItems(personnageAdapter, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                Intent intent = new Intent(Act_Configurations.this.getApplication(), Act_Play.class);
                intent.putExtra("id",personnageAdapter.getItem(position).getId());
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

    public void testConnection(View v){
        mRootApplication.testConnection();
    }

	@Override
	public void react(String res, int Flag) {
		// does nothing		
	}
}
