package ch.yannick.display.activityArsenal;


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

import java.util.ArrayList;

import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.display.technical.EnumAdapter;
import ch.yannick.display.views.DiceDisplayer;
import ch.yannick.display.views.FlowLayout;
import ch.yannick.display.views.ValueChangeListener;
import ch.yannick.display.views.ValueControler;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;
import ch.yannick.intern.items.Item;
import ch.yannick.intern.personnage.Attribute;
import ch.yannick.intern.usables.Weapon;

public class Frag_WeaponDetail extends Fragment{
	
	private static final String LOG="Frag:WeaponDetail";
	private Weapon w;
	private int index, mTextPadding = 0;
    private LinearLayout  mActions;
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
        mTextPadding = getResources().getDimensionPixelOffset(R.dimen.view_padding);

        ArrayList<Attribute> attributeArrayList = new ArrayList<>();
        for(Attribute attribute: Attribute.values())
            attributeArrayList.add(attribute);
        attributeArrayAdapter = new EnumAdapter<>(getActivity(),attributeArrayList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		Log.d(LOG, "onCreateVIew");

        index = getArguments().getInt("index",0);
        try{
        	Item item = ((RootApplication) getActivity().getApplication()).getDataManager().getItem(getArguments().getLong("id",0));
            if(item instanceof Weapon) {
                w = (Weapon)item;
                ((RootApplication) getActivity().getApplication()).mCurrentUsable = w;
            }else{
                getActivity().finish();
            }
        }catch(Exception e){ e.printStackTrace();}

        View v = inflater.inflate(R.layout.frag_weapons_detail, container, false);
        ((TextView)v.findViewById(R.id.name_weapon)).setText(w.getName());
        ((TextView)v.findViewById(R.id.name_weapon_type)).setText(w.getTyp().getStringId());
        mActions = (LinearLayout) v.findViewById(R.id.InsideFrame);

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
    }

    @Override
    public void onResume(){
        super.onResume();
        createLayout();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(LOG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_weapon, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
        case R.id.save:
             w.setWeight(((ValueControler) getView().findViewById(R.id.libras)).getValue() * 12 + ((ValueControler) getView().findViewById(R.id.ounces)).getValue());
            ((RootApplication) getActivity().getApplication()).getDataManager().pushItem(w);
            return true;
        }
        return false;
    }

	public int getShownIndex() {
		return index;
	}

	private void createLayout() {
        mActions.removeAllViews();

        TextView title;
        FlowLayout flow;
        LinearLayout attributeLayout, damageLayout, penetrationLayout, fatigueLayout;

        for (final Action finalAction : w.getTyp().getActions()) {
            final ActionData actionData = w.getData(finalAction);
            title = new TextView(getActivity());
            title.setText(finalAction.getStringId());
            title.setTextAppearance(getActivity(), R.style.BaseTextWhite);
            title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            title.setPadding(0, 0, getResources().getDimensionPixelOffset(R.dimen.view_padding), getResources().getDimensionPixelOffset(R.dimen.view_padding));
            mActions.addView(title);

            flow = new FlowLayout(getActivity());
            flow.setPadding(getResources().getDimensionPixelOffset(R.dimen.view_padding));

            attributeLayout = makeLayout();
            final Button attrOne = makeButton(actionData.attributes.get(0).getStringId());
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
                                actionData.attributes.add(0, attr);
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
                attrOne.setPadding(mTextPadding,0,mTextPadding,0);
                attributeLayout.addView(attrOne);
            final Button attrTwo = makeButton(actionData.attributes.get(1).getStringId());
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
                                actionData.attributes.add(1, attr);
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
                attrTwo.setPadding(mTextPadding,0,mTextPadding,0);
                attributeLayout.addView(attrTwo);
            attributeLayout.addView(makeTextView("+"));
            attributeLayout.addView(makeControler(actionData.enhancer,-1,-1,new ValueChangeListener() {
                @Override
                public void onChangeValue(int value) {
                    actionData.enhancer =  value;
                }}));
            flow.addView(attributeLayout);

            fatigueLayout = makeLayout();
            fatigueLayout.addView(makeTextView(R.string.fatigue));
            fatigueLayout.addView(makeControler(actionData.fatigue, 0, -1, new ValueChangeListener() {
                @Override
                public void onChangeValue(int value) {
                    actionData.fatigue =  value;
                }
            }));
            flow.addView(fatigueLayout);

            if(finalAction.is("Attack")){
                penetrationLayout = makeLayout();
                penetrationLayout.addView(makeTextView(R.string.penetration));
                penetrationLayout.addView(makeTextView(actionData.resultString));
                flow.addView(penetrationLayout);

                damageLayout = makeLayout();
                damageLayout.addView(makeTextView(R.string.degats));
                DiceDisplayer dc = new DiceDisplayer(getActivity());
                dc.showNumber(false);
                dc.setDices(actionData.resultDice);
                dc.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity().getApplication(), Dialog_Schaden.class);
                        intent.putExtra("id",w.getId());
                        intent.putExtra("action",finalAction.getName());
                        startActivity(intent);
                    }
                });
                damageLayout.addView(dc);
                damageLayout.addView(makeTextView("+"));

        flow.addView(damageLayout);
            }


            flow.invalidate();
            flow.requestLayout();
            mActions.addView(flow);
        }
        mActions.invalidate();
        mActions.requestLayout();
    }

    private TextView makeTextView(int string){
        TextView tx = new TextView(getActivity());
        tx.setText(string);
        tx.setPadding(mTextPadding,0,mTextPadding,0);
        tx.setTextAppearance(getActivity(), R.style.BaseButton);
        return tx;
    }

    private TextView makeTextView(String string){
        TextView tx = new TextView(getActivity());
        tx.setText(string);
        tx.setPadding(mTextPadding,0,mTextPadding,0);
        tx.setTextAppearance(getActivity(),R.style.BaseButton);
        return tx;
    }

    private Button makeButton(int text){
        Button bt = new Button(getActivity());
        bt.setTextAppearance(getActivity(), R.style.BaseButton);
        bt.setText(text);
        return bt;
    }

    private ValueControler makeControler(int value,int lowerBound,int upperBound, ValueChangeListener listener){
        ValueControler controler = new ValueControler(getActivity());
        controler.setListener(listener);
        controler.setTextAppearance(getActivity(), R.style.BaseButton);
        controler.setValue(value);
        controler.setBounds(lowerBound, upperBound);
        return controler;
    }

    private LinearLayout makeLayout(){
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        return linearLayout;
    }
}