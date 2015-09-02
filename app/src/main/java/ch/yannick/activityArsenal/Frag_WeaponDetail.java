package ch.yannick.activityArsenal;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.enums.Action;
import ch.yannick.enums.Attribute;
import ch.yannick.enums.WaffenTyp;
import ch.yannick.intern.Weapon;
import ch.yannick.technical.EnumAdapter;
import ch.yannick.views.DiceDisplayer;
import ch.yannick.views.FlowLayout;
import ch.yannick.views.ValueChangeListener;
import ch.yannick.views.ValueControler;

public class Frag_WeaponDetail extends Fragment implements  OnClickListener{
	
	private static final String LOG="Frag:WeaponDetail";
	private Weapon w;
	private int index;
    private LinearLayout  mDamage,mActions;
    private EnumAdapter<Attribute> attributeArrayAdapter;
	
	public static Frag_WeaponDetail newInstance(int position, Long id) {
		Frag_WeaponDetail f=new Frag_WeaponDetail();
		
		Bundle args = new Bundle();
		args.putInt("index", position);
		args.putLong("id", id);
		f.setArguments(args);

		return f;
	}

    @Override
    public void onCreate(Bundle sv){
        super.onCreate(sv);
        setHasOptionsMenu(true);

        ArrayList<Attribute> attributeArrayList = new ArrayList<>();
        for(Attribute attribute: Attribute.values())
            attributeArrayList.add(attribute);
        attributeArrayAdapter = new EnumAdapter<>(getActivity(),attributeArrayList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		Log.d(LOG,"onCreateVIew");

        index = getArguments().getInt("index",0);
        try{
        	w = ((RootApplication) getActivity().getApplication()).getDataManager().getWeapon(getArguments().getLong("id",0));
            ((RootApplication) getActivity().getApplication()).currentWeapon = w;
        }catch(Exception e){ e.printStackTrace();}

        View v = inflater.inflate(R.layout.frag_weapons_detail, container, false);
        ((TextView)v.findViewById(R.id.name_weapon)).setText(w.getName());
        ((TextView)v.findViewById(R.id.name_weapon_type)).setText(w.getType().getStringId());
        mActions = (LinearLayout) v.findViewById(R.id.InsideFrame);
        mDamage = (LinearLayout) v.findViewById(R.id.frame_damage);

        // dice
        v.findViewById(R.id.degats_dice).setOnClickListener(this);
        return v;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		Log.d(LOG, "onActivityCreated");
		Log.d(LOG, " in Layout " + this.isInLayout());
        int ounces = w.getWeight();
        int libras = ounces / 12;
        ounces = ounces % 12;
        ((ValueControler)getView().findViewById(R.id.ounces)).setValue(ounces);
        ((ValueControler)getView().findViewById(R.id.libras)).setValue(libras);

        getView().findViewById(R.id.weight).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.weight_explanation);
                builder.create().show();
            }
        });

        createLayout();
        refresh();
    }

    @Override
    public void onResume(){
        Log.d(LOG,"onResume wschaden "+w.getSchaden());
        super.onResume();
        refresh();
    }

    private void refresh(){
        ((ToggleButton)getView().findViewById(R.id.direct)).setChecked(w.isDirect());
        ((DiceDisplayer)getView().findViewById(R.id.degats_dice)).setDices(w.schadenW());
        ((TextView)getView().findViewById(R.id.degats)).setText(""+w.getSchaden());
        ((TextView)getView().findViewById(R.id.penetration)).setText(""+w.getPenetration());
        getView().findViewById(R.id.degats_dice).getParent().requestLayout();
        getView().invalidate();
        getView().requestLayout();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(LOG,"onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_weapon, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
        case R.id.save:
             w.setWeight(((ValueControler) getView().findViewById(R.id.libras)).getValue() * 12 + ((ValueControler) getView().findViewById(R.id.ounces)).getValue());
            ((RootApplication) getActivity().getApplication()).getDataManager().pushWeapon(w);
            return true;
        case R.id.change_type:
            final ArrayList<WaffenTyp> liste = new ArrayList<>();
            for(WaffenTyp wt: WaffenTyp.values())
                liste.add(wt);
            final EnumAdapter<WaffenTyp> adapter = new EnumAdapter<>(getActivity(),liste);
            //Initialize the Alert Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.change_type);
            builder.setSingleChoiceItems(adapter, 1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position) {
                    WaffenTyp waffenTyp = liste.get(position);
                    dialog.dismiss();
                    changeWeaponKind(waffenTyp);
                    createLayout();
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
            return true;
        }
        return false;
    }

	public int getShownIndex() {
		return index;
	}

	private void changeWeaponKind(WaffenTyp type) {
		w.changeType(type);
		Log.d(LOG,"Weapon set to "+w.getType().name()+ " since type is "+type);
	}
			
	private void createLayout() {
        mDamage.setVisibility(w.getType() == WaffenTyp.SHIELD ? View.INVISIBLE : View.VISIBLE);
        ((ToggleButton)getView().findViewById(R.id.direct)).setClickable(false);
        mActions.removeAllViews();

        TextView title, plus, fatigue;
        LinearLayout attributeLayout, fatigueLayout;
        FlowLayout flow;
        ValueControler controler;

        for (Action action : w.getType().getActions()) {
            final Action finalAction = action;

            title = new TextView(getActivity());
            title.setText(action.getStringId());
            title.setTextAppearance(getActivity(), R.style.BaseTextWhite);
            title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            title.setPadding(0,0,getResources().getDimensionPixelOffset(R.dimen.view_padding)
                    ,getResources().getDimensionPixelOffset(R.dimen.view_padding));
            mActions.addView(title);

            flow = new FlowLayout(getActivity());
            flow.setPadding(getResources().getDimensionPixelOffset(R.dimen.view_padding));
            final Button attrOne = new Button(getActivity());
            attrOne.setTextAppearance(getActivity(), R.style.BaseButton);
            attrOne.setText(w.getAttributes(action).get(0).getStringId());
            attrOne.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Initialize the Alert Dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.attribute);
                    builder.setSingleChoiceItems(attributeArrayAdapter, 1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            Attribute attr = attributeArrayAdapter.getItem(position);
                            dialog.dismiss();
                            w.setActionAttributes(finalAction, attr, w.getAttributes(finalAction).get(1));
                            attrOne.setText(attr.getStringId());
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
            flow.addView(attrOne);

            final Button attrTwo = new Button(getActivity());
            attrTwo.setTextAppearance(getActivity(), R.style.BaseButton);
            attrTwo.setText(w.getAttributes(action).get(1).getStringId());
            attrTwo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Initialize the Alert Dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.attribute);
                    builder.setSingleChoiceItems(attributeArrayAdapter, 1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            Attribute attr = attributeArrayAdapter.getItem(position);
                            dialog.dismiss();
                            w.setActionAttributes(finalAction, w.getAttributes(finalAction).get(0), attr);
                            attrTwo.setText(attr.getStringId());
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
            flow.addView(attrTwo);

            plus = new TextView(getActivity());
            plus.setText("+");
            plus.setTextAppearance(getActivity(), R.style.BaseButton);
            flow.addView(plus);


            controler = new ValueControler(getActivity());
            controler.setListener(new ValueChangeListener() {
                @Override
                public void onChangeValue(int value) {
                    w.setEnhancer(finalAction, value);
                }
            });
            controler.setTextAppearance(getActivity(), R.style.BaseButton);
            controler.setValue(w.getEnhancer(finalAction));
            flow.addView(controler);

            fatigueLayout = new LinearLayout(getActivity());
            fatigueLayout.setOrientation(LinearLayout.HORIZONTAL);

            fatigue = new TextView(getActivity());
            fatigue.setText(R.string.fatigue);
            fatigue.setTextAppearance(getActivity(), R.style.BaseButton);
            fatigueLayout.addView(fatigue);

            controler = new ValueControler(getActivity());
            controler.setTextAppearance(getActivity(), R.style.BaseButton);
            controler.setValue(w.getFatigue(finalAction));
            controler.setBounds(0,-1);
            controler.setListener(new ValueChangeListener() {
                @Override
                public void onChangeValue(int value) {
                    w.setFatigue(finalAction, value);
                }
            });
            fatigueLayout.addView(controler);
            flow.addView(fatigueLayout);
            flow.invalidate();
            flow.requestLayout();
            mActions.addView(flow);
        }
        mActions.invalidate();
        mActions.requestLayout();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity().getApplication(), Dialog_Schaden.class);
        intent.putExtra("id",w.getId());
        startActivityForResult(intent,R.id.degats);
    }
}