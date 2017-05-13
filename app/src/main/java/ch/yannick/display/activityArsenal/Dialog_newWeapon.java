package ch.yannick.display.activityArsenal;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;
import ch.yannick.display.technical.EnumAdapter;
import ch.yannick.intern.usables.UsableType;


public class Dialog_newWeapon extends MyBaseActivity implements AdapterView.OnItemClickListener {
    private EnumAdapter adapter;
    private int mPosition =0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_new_weapon);

        final ListView listview = (ListView) findViewById(R.id.listView);
		ArrayList<UsableType> list = new ArrayList(Arrays.asList(UsableType.values()));
		list.remove(UsableType.ROLE);
        adapter = new EnumAdapter(getApplication(),list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }

	
	public void confirmed(View v){
		final EditText input = (EditText) findViewById(R.id.nom);
		mRootApplication.getDataManager().newUsable(input.getText().toString(), (UsableType) adapter.getItem(mPosition));
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


