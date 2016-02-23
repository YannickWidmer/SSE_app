package ch.yannick.display.activityPlay;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.display.technical.EnumAdapter;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.personnage.Limb;
import ch.yannick.intern.state.State;
import ch.yannick.intern.usables.Weapon;
import ch.yannick.intern.usables.WeaponTyp;

/**
 * Created by Yannick on 21.02.2016.
 */
public class Frag_Actions extends Fragment implements AdapterView.OnItemClickListener {

    private static final String LOG = "frag:actions";
    private State st;
    private Frag_PlayAttributes mAttr;
    private Frag_Displayer mDisplayer;
    private Map<Limb,Holder> holderMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, "onCreateView");
        st = ((RootApplication)getActivity().getApplication()).getCurrentState();
        HorizontalScrollView v = (HorizontalScrollView) inflater.inflate(R.layout.frag_play_control, container, false);
        View view;
        Holder holder;
        for(Limb l:Limb.values()) {
            view =  inflater.inflate(R.layout.view_usable_state, v, true);
            holder = new Holder();
            holder.main = view;
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.name.setText(l.getStringId());
            holder.loaded = (ImageView) view.findViewById(R.id.loaded);
            holder.unloaded = (ImageView) view.findViewById(R.id.unloaded);
            holder.adapter = new EnumAdapter<>(getActivity(), holder.actions, R.layout.row_centered_text);
            ListView listView = ((ListView) view.findViewById(R.id.actions));
            listView.setAdapter(holder.adapter);
            ((ListView)view.findViewById(R.id.actions)).setOnItemClickListener(this);
            listView.setTag(l);
            holderMap.put(l, holder);
            v.addView(view);
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG, "onActivityCreated");

        mAttr = (Frag_PlayAttributes) getActivity().getFragmentManager().findFragmentById(R.id.attributes);
        mDisplayer = (Frag_Displayer) getActivity().getFragmentManager().findFragmentById(R.id.display);

        View v = getView();

        refresh();
    }

    protected void refresh(){
        Holder holder;
        for(Map.Entry<Limb,Holder> entry:holderMap.entrySet()){
            if(st.hasUsable(entry.getKey())){
                holder = entry.getValue();
                holder.name.setText(st.getUsable(entry.getKey()).getName());
                holder.actions.clear();
                holder.actions.addAll(st.getActions(entry.getKey()));
                holder.loaded.setVisibility(View.GONE);
                holder.unloaded.setVisibility(View.GONE);
                if(((WeaponTyp)st.getUsable(entry.getKey()).getTyp()).isLoadable()){
                    if(((Weapon)st.getUsable(entry.getKey())).getIsLoaded()){
                        holder.loaded.setVisibility(View.VISIBLE);
                    }else{
                        holder.unloaded.setVisibility(View.VISIBLE);
                    }
                }
            }else
                entry.getValue().main.setVisibility(View.GONE);
        }

        ((TextView) getView().findViewById(R.id.mental_state)).setText(st.getMentalState().getStringId());
        ((TextView) getView().findViewById(R.id.race)).setText(st.getRace().getStringId());
    }

    // Actions in listviews
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mDisplayer.setSplit(1);
        mDisplayer.setEnhancer(0);
        mDisplayer.hideDegats();
        Limb l = (Limb) parent.getTag();
        Holder h = holderMap.get(l);
        Action act = h.actions.get(position);

        mAttr.setSelection(Arrays.asList(st.getActionData(l,act).attributes));
        mDisplayer.setEnhancer(st.getActionData(l, act).getEnhancer());
        mDisplayer.setModif(st.getActionData(l, act).getModifier());
        if(act.is("ATTACK"))
            mDisplayer.setDegats(st.getActionData(l, act).resultDice,st.getActionData(l, act).resultValue,
                    st.getActionData(l, act).penetration,st.getActionData(l, act).isDirect);
        mDisplayer.refresh();
    }

    private class Holder{
        public View main;
        public TextView name;
        public ImageView loaded,unloaded;
        public ArrayList<Action>  actions = new ArrayList<>();
        public EnumAdapter<Action> adapter;

    }
}
