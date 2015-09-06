package ch.yannick.display.activityArmor;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import android.widget.TextView;

import java.util.ArrayList;

import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.display.technical.EnumAdapter;
import ch.yannick.display.views.ValueControler;
import ch.yannick.intern.items.Armor;
import ch.yannick.intern.personnage.HitZone;

public class Frag_ArmorDetail extends Fragment {

    private static final String LOG = "Frag:ArmorDetail";
    private Armor armor;
    private HitZone mPart;
    private int index;
    private EnumAdapter<HitZone> mBodyParts;

    public static Frag_ArmorDetail newInstance(int position, Long id) {
        Frag_ArmorDetail f = new Frag_ArmorDetail();

        Bundle args = new Bundle();
        args.putInt("index", position);
        args.putLong("id", id);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle sv) {
        super.onCreate(sv);
        setHasOptionsMenu(true);

        ArrayList<HitZone> hitZones = new ArrayList<>();
        for (HitZone bp : HitZone.values())
            hitZones.add(bp);
        mBodyParts = new EnumAdapter<>(getActivity(), hitZones);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, "onCreateVIew");

        index = getArguments().getInt("index", 0);
        try {
            armor = ((RootApplication) getActivity().getApplication()).getDataManager().getArmor(getArguments().getLong("id", 0));
        } catch (Exception e) {
            e.printStackTrace();
        }

        View v = inflater.inflate(R.layout.frag_armor_detail, container, false);
        ((TextView)v.findViewById(R.id.nom_armor)).setText(armor.getName());

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG, "onActivityCreated");
        Log.d(LOG, " in Layout " + this.isInLayout());

        // Weight
        int ounces = armor.getWeight();
        int libras = ounces / 12;
        ounces = ounces % 12;
        ((ValueControler) getView().findViewById(R.id.ounces)).setValue(ounces);
        ((ValueControler) getView().findViewById(R.id.libras)).setValue(libras);

        getView().findViewById(R.id.weight).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.weight_explanation);
                builder.create().show();
            }
        });

        // Protection
        ((ValueControler) getView().findViewById(R.id.protection)).setValue(armor.getProtection());
        ((ValueControler) getView().findViewById(R.id.wheater_protection)).setValue(armor.getWeatherProtection());

        // BodyPart
        mPart = armor.getPart();
        final Button partButton = ((Button) getView().findViewById(R.id.bodypart));
        partButton.setText(mPart.getStringId());
        partButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize the Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.attribute);
                builder.setSingleChoiceItems(mBodyParts, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        mPart = mBodyParts.getItem(position);
                        dialog.dismiss();
                        partButton.setText(mPart.getStringId());
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

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(LOG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_armor, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                armor.setWeight(((ValueControler) getView().findViewById(R.id.libras)).getValue() * 12 + ((ValueControler) getView().findViewById(R.id.ounces)).getValue());
                armor.setProtection(((ValueControler) getView().findViewById(R.id.protection)).getValue());
                armor.setWeatherProtection(((ValueControler) getView().findViewById(R.id.wheater_protection)).getValue());
                armor.setPart(mPart);
                ((RootApplication) getActivity().getApplication()).getDataManager().pushArmor(armor);
                return true;
        }
        return false;
    }

    public int getShownIndex() {
        return index;
    }
}