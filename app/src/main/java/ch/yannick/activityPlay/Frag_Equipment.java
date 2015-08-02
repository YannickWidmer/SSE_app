package ch.yannick.activityPlay;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceActivity;
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

import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.enums.BodyPart;
import ch.yannick.enums.WaffenTyp;
import ch.yannick.intern.Armor;
import ch.yannick.intern.State;
import ch.yannick.intern.Weapon;

public class Frag_Equipment extends Fragment {
	private State st;
    private Map<BodyPart,ArrayList<Armor>> mEquippedMap;
    private Map<BodyPart,ArrayAdapter<Armor>> mEquippedAdapterMap;
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
                arm(true);
            }
        });

        ((Button)v.findViewById(R.id.weapon_right)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arm(false);
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
        for(BodyPart bp:BodyPart.values())
            mEquippedMap.put(bp,new ArrayList<Armor>());

		ArrayList<Armor> equippedArray = st.getAllArmor();
        for(Armor armor:equippedArray){
            mEquippedMap.get(armor.getPart()).add(armor);
        }

        mEquippedAdapterMap = new HashMap<>();
        for(BodyPart bp:BodyPart.values()) {
            mEquippedAdapterMap.put(bp, new ArrayAdapter<>(getActivity(), R.layout.row_dialog, mEquippedMap.get(bp)));
        }

        for(final BodyPart bp:BodyPart.values()) {
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

    private void arm(final boolean leftArm) {
        final ArrayList<Weapon> waffenListe = ((RootApplication)getActivity().getApplication()).getDataManager().getAllWeapon();
        waffenListe.add(new Weapon(0l,getResources().getString(R.string.remove), WaffenTyp.SWORD,false,0,0));
        final ArrayAdapter<Weapon> adapter = new ArrayAdapter<Weapon>(getActivity(),R.layout.row_dialog,android.R.id.text1,waffenListe);
        //Initialize the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.chose_weapon);
        builder.setSingleChoiceItems(adapter, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                Weapon waffe = adapter.getItem(position);
                if (waffe.getName().equals(getResources().getString(R.string.remove))) {
                    st.removeWeapon(leftArm);
                } else {
                    try {
                        waffe = ((RootApplication) getActivity().getApplication()).getDataManager().getWeapon(waffe.getId());
                        st.setWeapon(waffe, leftArm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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

    private void refresh(){
        for(BodyPart bp :BodyPart.values()){
            mEquippedAdapterMap.get(bp).notifyDataSetChanged();
        }
        View v = getView();

        ((TextView)v.findViewById(R.id.protection_head)).setText("" + st.getProtection(BodyPart.HEAD));
        ((TextView)v.findViewById(R.id.protection_chest)).setText("" + st.getProtection(BodyPart.CHEST));
        ((TextView)v.findViewById(R.id.protection_arms)).setText("" + st.getProtection(BodyPart.ARMS));
        ((TextView)v.findViewById(R.id.protection_legs)).setText("" + st.getProtection(BodyPart.LEGS));
        ((TextView)v.findViewById(R.id.weight)).setText("" + st.getWeight());
        ((TextView)v.findViewById(R.id.base_fatigue)).setText("" + st.getFatigueBase());
        ((TextView)v.findViewById(R.id.mouvement_fatigue)).setText("" + st.getFatigueMouvememnt());
        ((TextView)v.findViewById(R.id.esquiv)).setText("" + st.getAvatarEsquivTalent());
        ((TextView)v.findViewById(R.id.run)).setText("" + st.getAvatarRunTalent());
        ((TextView)v.findViewById(R.id.fight)).setText("" + st.getAvatarFightTalent());
        if(st.hasWeaponLeft()){
            ((Button)v.findViewById(R.id.weapon_left)).setText(""+st.getLeftWeaponName());
        }else{
            ((Button)v.findViewById(R.id.weapon_left)).setText(R.string.left_arm);
        }
        if(st.hasWeaponRight()){
            ((Button)v.findViewById(R.id.weapon_right)).setText(""+st.getRightWeaponName());
        }else{
            ((Button)v.findViewById(R.id.weapon_right)).setText(R.string.right_arm);
        }
    }

}
