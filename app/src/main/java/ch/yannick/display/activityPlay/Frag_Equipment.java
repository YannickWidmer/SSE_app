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
import ch.yannick.intern.items.Clothe;
import ch.yannick.intern.personnage.HitZone;
import ch.yannick.intern.personnage.Limb;
import ch.yannick.intern.state.State;
import ch.yannick.intern.usables.Weapon;

public class Frag_Equipment extends Fragment {
	private State st;
    private Map<HitZone,ArrayList<Clothe>> mEquippedMap;
    private Map<HitZone,ArrayAdapter<Clothe>> mEquippedAdapterMap;
    private ArrayList<Clothe> mArsenal;
    private ArrayAdapter<Clothe> mArsenalAdapter;
    private RootApplication mAppication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View v =  inflater.inflate(R.layout.frag_equipment, container, false);

        mAppication = (RootApplication) getActivity().getApplication();

		st = mAppication.getCurrentState();

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

        mArsenal = st.getAllArmor();
        mArsenalAdapter = new ArrayAdapter<>(getActivity(), R.layout.row_dialog, mArsenal);
        ((ListView)v.findViewById(R.id.arsenal)).setAdapter(mArsenalAdapter);
        ((ListView)v.findViewById(R.id.arsenal)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Clothe armor = mArsenalAdapter.getItem(position);
                mEquippedMap.get(armor.getPart()).add(armor);
                st.putOn(armor);
                mAppication.react("", MyBaseActivity.JUST_REFRESH, -1);
            }
        });

        mEquippedMap = new HashMap<>();
        for(HitZone bp: HitZone.values())
            mEquippedMap.put(bp,new ArrayList<Clothe>());

		ArrayList<Clothe> equippedArray = st.getAllArmor();
        for(Clothe armor:equippedArray)
            mEquippedMap.get(armor.getPart()).add(armor);


        mEquippedAdapterMap = new HashMap<>();
        for(HitZone bp: HitZone.values()) {
            mEquippedAdapterMap.put(bp, new ArrayAdapter<>(getActivity(), R.layout.row_dialog, mEquippedMap.get(bp)));
        }

        for(final HitZone bp: HitZone.values()) {
            ((ListView) v.findViewById(bp.getLayoutId())).setAdapter(mEquippedAdapterMap.get(bp));
            ((ListView) v.findViewById(bp.getLayoutId())).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Clothe armor = mEquippedAdapterMap.get(bp).getItem(position);
                    mEquippedMap.get(bp).remove(armor);
                    st.removeArmor(armor);
                    mAppication.react("", MyBaseActivity.JUST_REFRESH, -1);
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
        final ArrayAdapter<Weapon> adapter = new ArrayAdapter<>(getActivity(),R.layout.row_dialog,android.R.id.text1,
                st.getInventory().getAllWeapon());
        //Initialize the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.chose_weapon);
        builder.setSingleChoiceItems(adapter, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                Weapon weapon = adapter.getItem(position);
                st.setUsable(weapon, which);
                dialog.dismiss();
                mAppication.react("",MyBaseActivity.JUST_REFRESH,-1);
            }
        });

        builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                st.removeUsable(which);
                dialog.dismiss();
                mAppication.react("", MyBaseActivity.JUST_REFRESH, -1);
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
        if(st.hasUsable(Limb.LEFTHAND) && st.hasUsable(Limb.RIGHTHAND)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.main_weapon);
            builder.setNegativeButton(st.getUsable(Limb.LEFTHAND).getName(null), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    st.combine(Limb.LEFTHAND);
                    dialog.dismiss();
                    mAppication.react("", MyBaseActivity.JUST_REFRESH, -1);
                }
            });
            builder.setPositiveButton(st.getUsable(Limb.RIGHTHAND).getName(null), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    st.combine(Limb.RIGHTHAND);
                    dialog.dismiss();
                    mAppication.react("", MyBaseActivity.JUST_REFRESH, -1);
                }
            });

            builder.create().show();
        }
        if(st.hasUsable(Limb.BOTHHANDS)){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.remove);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    st.removeUsable(Limb.BOTHHANDS);
                    dialog.dismiss();
                    mAppication.react("", MyBaseActivity.JUST_REFRESH, -1);
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

    public void refresh(){

        for(HitZone bp : HitZone.values())
            mEquippedAdapterMap.get(bp).notifyDataSetChanged();

        View v = getView();

        ((TextView)v.findViewById(R.id.protection_head)).setText("" + st.getProtection(HitZone.HEAD));
        ((TextView)v.findViewById(R.id.protection_chest)).setText("" + st.getProtection(HitZone.CHEST));
        ((TextView)v.findViewById(R.id.protection_arms)).setText("" + st.getProtection(HitZone.ARMS));
        ((TextView)v.findViewById(R.id.protection_legs)).setText("" + st.getProtection(HitZone.LEGS));
        ((TextView)v.findViewById(R.id.weight)).setText("" + st.getWeight());
        ((Button)v.findViewById(R.id.weapon_left)).setText(R.string.left_hand);
        ((Button)v.findViewById(R.id.weapon_right)).setText(R.string.right_hand);
        ((Button)v.findViewById(R.id.weapon_both)).setText("");
        if(st.hasUsable(Limb.LEFTHAND))
            ((Button)v.findViewById(R.id.weapon_left)).setText(""+st.getUsable(Limb.LEFTHAND).getName(null));

        if(st.hasUsable(Limb.RIGHTHAND))
            ((Button)v.findViewById(R.id.weapon_right)).setText(""+st.getUsable(Limb.RIGHTHAND).getName(null));

        if(st.hasUsable(Limb.BOTHHANDS))
            ((Button)v.findViewById(R.id.weapon_both)).setText(""+st.getUsable(Limb.BOTHHANDS));

        if(st.hasUsable(Limb.LEFTHAND)&& st.hasUsable(Limb.RIGHTHAND))
            ((Button)v.findViewById(R.id.weapon_both)).setText(R.string.combine);

    }
}
