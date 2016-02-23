package ch.yannick.display.activityPlay;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.yannick.context.R;
import ch.yannick.display.views.DiceAdapter;
import ch.yannick.display.views.DiceDisplayer;
import ch.yannick.display.views.ValueChangeListener;
import ch.yannick.display.views.ValueControler;
import ch.yannick.intern.dice.Choice;
import ch.yannick.intern.dice.Dice;
import ch.yannick.intern.dice.DicePossibilities;

public class Frag_Displayer extends Fragment{
	@SuppressWarnings("unused")
	private static String LOG = "frag:Displayer";
	private int  mSkill, mModif;

    private ValueControler mAlterControler, mSplitControler;
    private TextView mRestView;
	private DiceAdapter adap;
    private DiceComputer asyncDiceComputer;
    private DiceDisplayer degats;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mSkill = 0;
		adap = new DiceAdapter(getActivity(),new ArrayList<Choice>());

        setHasOptionsMenu(true);
		return inflater.inflate(R.layout.frag_displayer, container, false);
	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_display, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.silent:
                adap.changeSilent();
                adap.notifyDataSetChanged();
                return true;
        }
        return false;
    }


    @Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		View v = getView();
		ListView listView = (ListView) v.findViewById(R.id.listView);
		listView.setAdapter(adap);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.dice_displayer, parent, false);
                ((DiceDisplayer) v).changeSilent(adap.isSilent());
                ((DiceDisplayer) v).set(adap.getItem(position));
                Log.d(LOG, "diceDisokayer set");
                builder.setView(v);
                Log.d(LOG, "diceDisokayer in builder set");
                builder.create().show();
                //builder.setContentView(R.layout.dice_displayer);
                //Dialog d = builder.create();
                //((DiceDisplayer)d.findViewById(R.id.dices)).changeSilent(adap.isSilent());
                //((DiceDisplayer)d.findViewById(R.id.dices)).set(adap.getItem(position));
                //d.show();
            }
        });


        hideDegats();

        degats = (DiceDisplayer)getView().findViewById(R.id.damage_dice);

        mAlterControler = ((ValueControler)v.findViewById(R.id.alter));
        mAlterControler.setListener(new ValueChangeListener() {
            @Override
            public void onChangeValue(int value) {
                Frag_Displayer.this.refresh();
            }
        });


        mSplitControler = ((ValueControler)v.findViewById(R.id.split));
        mSplitControler.setListener(new ValueChangeListener() {
            @Override
            public void onChangeValue(int value) {
                Frag_Displayer.this.refresh();
            }
        });

        mRestView = (TextView) v.findViewById(R.id.rest);

        refresh();
	}

    public void hideDegats(){
        getView().findViewById(R.id.damage_layout).setVisibility(View.GONE);
    }

    public void setDegats(List<Dice> dices, int enhancer,int penetration,boolean direct){
        degats.setDices(dices);
        degats.setNumber(enhancer);
        ((TextView)getView().findViewById(R.id.degats_value)).setText("" + enhancer);
        if(direct) {
            getView().findViewById(R.id.damage_direct_text).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.damage_penetration).setVisibility(View.GONE);
        }
        else {
            ((TextView)getView().findViewById(R.id.damage_penetration)).setText(""+penetration);
            getView().findViewById(R.id.damage_direct_text).setVisibility(View.GONE);
            getView().findViewById(R.id.damage_penetration).setVisibility(View.VISIBLE);

        }
        getView().findViewById(R.id.damage_layout).setVisibility(View.VISIBLE);
    }
	
	public void setSkill(int value){
		this.mSkill = value;
        ((TextView)getView().findViewById(R.id.skill)).setText(mSkill + "");
	}

    public void setModif(int value){
        mModif = value;
        ((TextView)getView().findViewById(R.id.modif)).setText("(" + value + ")");
    }

    public void setSplit(int value){
        mSplitControler.setValue(value);
    }

    public void setEnhancer(int value){
        mAlterControler.setValue(value);
    }

	public void refresh(){
        int skill = (mSkill+ mAlterControler.getValue())/ mSplitControler.getValue();
        mRestView.setText(""+((mSkill+ mAlterControler.getValue()) % mSplitControler.getValue()));
        Log.d(LOG,"skill + alter / split"+(mSkill+ mAlterControler.getValue())/ mSplitControler.getValue());
	    if(asyncDiceComputer != null)
            asyncDiceComputer.cancel(true);
        asyncDiceComputer = new DiceComputer();
        asyncDiceComputer.execute(skill, mModif);
	}

    private class DiceComputer extends AsyncTask<Integer,Void,Void> {

        private DicePossibilities mComputer;

        @Override
        protected Void doInBackground(Integer... params) {
            int skill = 0, modif = 0;
            if(params.length>0)
                skill = params[0];
            if(params.length>1)
                modif = params[1];
            mComputer = new DicePossibilities();
            mComputer.set(skill, modif);

            while(!isCancelled() && mComputer.next());
            return null;
        }


        @Override
        protected void onPostExecute(Void  d){
            adap.clear();
            adap.addAll(mComputer.getResult());
            adap.notifyDataSetChanged();
        }
    }
}
