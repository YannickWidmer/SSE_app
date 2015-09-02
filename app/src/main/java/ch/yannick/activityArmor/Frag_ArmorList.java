package ch.yannick.activityArmor;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ch.yannick.activityArsenal.Dialog_newWeapon;
import ch.yannick.activityMain.Inter_ListCarrier;
import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.intern.Armor;
import ch.yannick.intern.Weapon;

/*
 * This fragment contains the -list of Weapons in the DB. It can be embedded
 *  in any activity implementing the inte_ListCarrier interface
 */

public class Frag_ArmorList extends Fragment implements OnItemClickListener, OnItemLongClickListener{
	//private boolean isDualPane;
	private int position=-1;
	private static final String LOG="Frag:ArmorList";
	
	private ArrayAdapter<Armor> adapter;
	private ArrayList<Armor> armorList = new ArrayList<>();
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		Log.d(LOG,"onCreateView");
		View v =inflater.inflate(R.layout.frag_armor_list, container, false);
		
		v.findViewById(R.id.create_new).setOnClickListener(new OnClickListener() {
		        @Override
		        public void onClick(final View v) {
					Intent intent = new Intent(getActivity().getApplication(), Dialog_NewArmor.class);
					startActivity(intent);
				}
			});
		
		v.findViewById(R.id.errase).setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(final View v) {
	        	if(position !=-1){
	        		try{
			        	Armor armor= armorList.get(position);
			        	armorList.remove(position);
			        	((RootApplication) getActivity().getApplication()).getDataManager().delete(armor);
			        	position=-1;
	        		}catch(Exception e){e.printStackTrace();}
		        	adapter.notifyDataSetChanged();
	        	}
	        }
		});
        // Inflate the layout for this fragment
        return v;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(LOG,"onActivityCreated");
		try{
			 armorList =((RootApplication) getActivity().getApplication()).getDataManager().getAllArmor();
		}catch(Exception e){e.printStackTrace();}
		
	
		final ListView listview = (ListView) getView().findViewById(R.id.listView);
		
		adapter =	new ArrayAdapter<Armor>(getActivity().getApplication(),android.R.layout.simple_list_item_1, armorList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		listview.setOnItemLongClickListener(this);
		
	/*	if (savedInstanceState != null) {
            // Restore last state for checked position.
           position = savedInstanceState.getInt("curChoice", 0);
           listview.setSelection(position);
        }   */
	}

	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG,"onSavedInstanceState");
        outState.putInt("curChoice", position);
    }

	

	@Override
	public void onItemClick(AdapterView<?> parent,final View view, int position, long id) {
		this.position=position;		
		((Inter_ListCarrier) getActivity()).showClick(position,adapter.getItem(position).getId());
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
			long id) {
			this.position=position;
			((Inter_ListCarrier) getActivity()).showLongClick(position,adapter.getItem(position).getId());
		return false;
	}
	
	
	//called from Root since SQL works asynchronously 
	public void upDate() {
		Log.d(LOG, "upDate");
		try{
			 armorList.clear();
			 armorList.addAll(((RootApplication) getActivity().getApplication()).getDataManager().getAllArmor());
		}catch(Exception e){e.printStackTrace();}
	    
		adapter.notifyDataSetChanged();
	}

	public boolean isShowing() {
		return position!=-1;
	}

	public int position() {
		return position;
	}

	public Long getItemId() {
		return adapter.getItem(position).getId();
	}
	
}


