package ch.yannick.display.activityPlay;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.items.Armor;
import ch.yannick.intern.personnage.HitZone;
import ch.yannick.intern.personnage.Limb;
import ch.yannick.intern.state.State;
import ch.yannick.intern.usables.Weapon;

public class Frag_Equipment extends Fragment {
	private State st;
    private Map<HitZone,ArrayList<Armor>> mEquippedMap;
    private Map<HitZone,ArrayAdapter<Armor>> mEquippedAdapterMap;
    private ArrayList<Armor> mArsenal;
    private ArrayAdapter<Armor> mArsenalAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View v =  inflater.inflate(R.layout.frag_equipment, container, false);

        RootApplication application = (RootApplication) getActivity().getApplication();

		st = application.getState(getActivity().getIntent().getLongExtra("id", -1));

        ((Button)v.findViewById(R.id.weapon_left)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arm(Limb.LEFTHAND);
            }
        });

        ((Button)v.findViewById(R.id.weapon_right)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arm(Limb.RIGHTHAND);
            }
        });

        ((Button)v.findViewById(R.id.weapon_both)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bothButton();
            }
        });

        mArsenal = application.getDataManager().getAllArmor();
        mArsenalAdapter = new ArrayAdapter<>(getActivity(), R.layout.row_dialog, mArsenal);
        ((ListView)v.findViewById(R.id.arsenal)).setAdapter(mArsenalAdapter);
        ((ListView)v.findViewById(R.id.arsenal)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Armor armor = mArsenalAdapter.getItem(position);
                mEquippedMap.get(armor.getPart()).add(armor);
                st.putOn(armor);
                refresh();
            }
        });

        mEquippedMap = new HashMap<>();
        for(HitZone bp: HitZone.values())
            mEquippedMap.put(bp,new ArrayList<Armor>());

		ArrayList<Armor> equippedArray = st.getAllArmor();
        for(Armor armor:equippedArray){
            mEquippedMap.get(armor.getPart()).add(armor);
        }

        mEquippedAdapterMap = new HashMap<>();
        for(HitZone bp: HitZone.values()) {
            mEquippedAdapterMap.put(bp, new ArrayAdapter<>(getActivity(), R.layout.row_dialog, mEquippedMap.get(bp)));
        }

        for(final HitZone bp: HitZone.values()) {
            ((ListView) v.findViewById(bp.getLayoutId())).setAdapter(mEquippedAdapterMap.get(bp));
            ((ListView) v.findViewById(bp.getLayoutId())).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Armor armor = mEquippedAdapterMap.get(bp).getItem(position);
                    mEquippedMap.get(bp).remove(armor);
                    st.removeArmor(armor);
                    refresh();
                }
            });
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refresh();
    }

    private void arm(final Limb which) {
        final ArrayList<Weapon> waffenListe = ((RootApplication)getActivity().getApplication()).getDataManager().getAllWeapon();
        final ArrayAdapter<Weapon> adapter = new ArrayAdapter<Weapon>(getActivity(),R.layout.row_dialog,android.R.id.text1,waffenListe);
        //Initialize the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.chose_weapon);
        builder.setSingleChoiceItems(adapter, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                Weapon weapon = adapter.getItem(position);
                try {
                    weapon = ((RootApplication) getActivity().getApplication()).getDataManager().getWeapon(weapon.getId());
                    st.setUsable(weapon, which);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                refresh();
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                st.removeWeapon(which);
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

    private void bothButton(){
        if(st.hasWeapon(Limb.LEFTHAND) && st.hasWeapon(Limb.RIGHTHAND)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.main_weapon);
            builder.setNegativeButton(st.getWeapon(Limb.LEFTHAND).getName(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    st.combine(Limb.LEFTHAND);
                    refresh();
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(st.getWeapon(Limb.RIGHTHAND).getName(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    st.combine(Limb.RIGHTHAND);
                    refresh();
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }
        if(st.hasWeapon(Limb.BOTHHANDS)){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.remove);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    st.removeWeapon(Limb.BOTHHANDS);
                    refresh();
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
    }

    private void refresh(){
        for(HitZone bp : HitZone.values()){
            mEquippedAdapterMap.get(bp).notifyDataSetChanged();
        }
        View v = getView();

        ((TextView)v.findViewById(R.id.protection_head)).setText("" + st.getProtection(HitZone.HEAD));
        ((TextView)v.findViewById(R.id.protection_chest)).setText("" + st.getProtection(HitZone.CHEST));
        ((TextView)v.findViewById(R.id.protection_arms)).setText("" + st.getProtection(HitZone.ARMS));
        ((TextView)v.findViewById(R.id.protection_legs)).setText("" + st.getProtection(HitZone.LEGS));
        ((TextView)v.findViewById(R.id.weight)).setText("" + st.getWeight());
        ((TextView)v.findViewById(R.id.base_fatigue)).setText("" + st.getFatigue(Action.valueOf("ATTACK"), Limb.ROLE));
        ((TextView)v.findViewById(R.id.mouvement_fatigue)).setText("" + st.getFatigue(Action.valueOf("RUN"),Limb.ROLE));
        ((TextView)v.findViewById(R.id.esquiv)).setText("" + st.getSkillEnhancer(Action.valueOf("ESQUIV"), Limb.ROLE));
        ((TextView)v.findViewById(R.id.run)).setText("" + st.getSkillEnhancer(Action.valueOf("RUN"),Limb.ROLE));
        ((TextView)v.findViewById(R.id.fight)).setText("" + st.getSkillEnhancer(Action.valueOf("ATTACK"),Limb.ROLE));
        ((Button)v.findViewById(R.id.weapon_left)).setText(R.string.left_hand);
        ((Button)v.findViewById(R.id.weapon_right)).setText(R.string.right_hand);
        ((Button)v.findViewById(R.id.weapon_both)).setText("");
        if(st.hasWeapon(Limb.LEFTHAND)){
            ((Button)v.findViewById(R.id.weapon_left)).setText(""+st.getWeapon(Limb.LEFTHAND).getName());
        }
        if(st.hasWeapon(Limb.RIGHTHAND)){
            ((Button)v.findViewById(R.id.weapon_right)).setText(""+st.getWeapon(Limb.RIGHTHAND));
        }
        if(st.hasWeapon(Limb.BOTHHANDS)){
            ((Button)v.findViewById(R.id.weapon_both)).setText(""+st.getWeapon(Limb.BOTHHANDS));
        }
        if(st.hasWeapon(Limb.LEFTHAND)&& st.hasWeapon(Limb.RIGHTHAND)){
            ((Button)v.findViewById(R.id.weapon_both)).setText(R.string.combine);
        }
        ((RootApplication)getActivity().getApplication()).react("nothing special", MyBaseActivity.JUST_REFRESH,0); }

}
