package ch.yannick.display.activityHeroes;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.display.technical.EnumAdapter;
import ch.yannick.display.views.ValueChangeListener;
import ch.yannick.display.views.ValueControler;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.personnage.Attribute;
import ch.yannick.intern.personnage.Personnage;
import ch.yannick.intern.personnage.Race;

public class Frag_HeroesDetail extends Fragment{
	
	private static final String LOG="Frag:HeroesDetail";
	private Personnage p;
	private int index;
	private boolean exists;
    private EnumAdapter<Race> races;
    private TalentAdapter adapter;
	
	public static Frag_HeroesDetail newInstance(int position, Long id) {
		Frag_HeroesDetail f=new Frag_HeroesDetail();
		
		Bundle args = new Bundle();
		args.putInt("index", position);
		args.putLong("id", id);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		Log.d(LOG,"onCreateVIew with id"+getArguments().getLong("id",0)+" position "+getArguments().getInt("index",0));
		exists=true;
        setHasOptionsMenu(true);
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
        	exists=false;
            return null;
        }	
        
       
        index=getArguments().getInt("index",0);
        try{
        	p=((RootApplication) getActivity().getApplication()).getDataManager().getPersonnage(getArguments().getLong("id",0));
        }catch(Exception e){ e.printStackTrace();}
        
        Log.d(LOG,"onCreateVIew2");

        ArrayList<Race> raceArrayList= new ArrayList<>();
        for (Race rc : Race.values())
            raceArrayList.add(rc);
        races = new EnumAdapter<Race>(getActivity(), raceArrayList);

        View v = inflater.inflate(R.layout.frag_heroes_detail, container, false);

        final Button b = (Button) v.findViewById(R.id.race);

        b.setText(p.getRasse().getStringId());

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize the Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.race);
                builder.setSingleChoiceItems(races, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        Race race = races.getItem(position);
                        b.setText(race.getStringId());
                        p.setRasse(race);
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
        });

        adapter = new TalentAdapter(getActivity(),new ArrayList<Talent>(Talent.values.values()),p.getTalents());


        return v;
        	// Inflate the layout for this fragmen
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(LOG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_heroes, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.save:
                for(Attribute attr:Attribute.values())
                    p.setAttr(attr,((ValueControler)getView().findViewById(attr.getControler())).getValue());
                ((RootApplication) getActivity().getApplication()).getDataManager().pushPersonnage(p);
                ((RootApplication) getActivity().getApplication()).refreshState(p.getId());
                return true;
            case R.id.talents:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                ListView lv = new ListView(getActivity());
                lv.setAdapter(adapter);
                builder.setView(lv);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
        }
        return false;
    }
	
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		Log.d(LOG,"onActivityCreated");
		if(exists){
			final View v = getView();
			((TextView)v.findViewById(R.id.nom_du_personnage)).setText(p.toString());
			updateValues(v);
            ValueControler vc;
            for(final Attribute attr:Attribute.values()) {
                vc = ((ValueControler) v.findViewById(attr.getControler()));
                vc.setValue(p.getAttr(attr));
                vc.setListener(new ValueChangeListener() {
                    @Override
                    public void onChangeValue(int value) {
                        ((TextView)v.findViewById(attr.getId())).setText(""+value);
                    }
                });
            }
		}
	}
	
	private void updateValues(View v){
        for(Attribute attribute:Attribute.values())
		    ((TextView)v.findViewById(attribute.getId())).setText(String.valueOf(p.getAttr(attribute)));
	}


	public Personnage getPersonnage() {
		return p;
	}

	public int getShownIndex() {
		return index;
	}

    private class TalentAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private Map<Talent,Integer> mMap;
        private ArrayList<Talent> mList;

        public TalentAdapter(Context context,ArrayList<Talent> list, Map<Talent,Integer> map) {
            mInflater = LayoutInflater.from(context);
            mMap = map;
            mList = list;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            final ViewHolder holder;
            if(convertView == null) {
                view = mInflater.inflate(R.layout.list_item_talent, parent, false);
                holder = new ViewHolder();
                holder.name = (TextView)view.findViewById(R.id.talent_name);
                holder.controler = ((ValueControler)view.findViewById(R.id.level));
                view.setTag(holder);
                holder.controler.setListener(new ValueChangeListener() {
                    @Override
                    public void onChangeValue(int value) {
                        if (value != 0)
                            mMap.put(holder.talent, value);
                        else
                            mMap.remove(holder.talent);
                    }
                });

            } else {
                view = convertView;
                holder = (ViewHolder)view.getTag();
            }

            holder.talent= mList.get(position);
            holder.name.setText( holder.talent.getStringId());
            holder.controler.setBounds(0,holder.talent.getMax());

            if(p.getTalents().containsKey(holder.talent))
                holder.controler.setValue(p.getTalents().get(holder.talent));
            else
                holder.controler.setValue(0);
            return view;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            public TextView name;
            public ValueControler controler;
            public Talent talent;
        }
    }
}

