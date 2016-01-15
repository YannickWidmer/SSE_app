package ch.yannick.display.activityArsenal;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;
import ch.yannick.display.technical.EnumAdapter;
import ch.yannick.intern.usables.UsableType;


public class Dialog_newWeapon extends MyBaseActivity implements AdapterView.OnItemClickListener {
    private EnumAdapter<UsableType> adapter;
    private int mPosition =0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_new_weapon);


        ArrayList<UsableType> typeList=new ArrayList<>();

        for(UsableType wt: UsableType.values()) {
			if(wt != UsableType.GENERAL)
				typeList.add(wt);
		}

        final ListView listview = (ListView) findViewById(R.id.listView);

        adapter = new EnumAdapter<>(this, typeList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }

	
	public void confirmed(View v){
		final EditText input = (EditText) findViewById(R.id.nom);
		mRootApplication.getDataManager().newWeapon(input.getText().toString(),adapter.getItem(mPosition));
		super.finish();
	}
	
	public void cancel(View v){
		super.finish();
	}

	@Override
	public void react(String res, int Flag) {
		// does nothing
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPosition = position;
    }
}


