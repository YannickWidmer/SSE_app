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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.display.technical.EnumAdapter;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.personnage.Attribute;
import ch.yannick.intern.personnage.Limb;
import ch.yannick.intern.state.State;
import ch.yannick.intern.usables.Weapon;

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
        HorizontalScrollView v = (HorizontalScrollView) inflater.inflate(R.layout.frag_actions, container, false);
        LinearLayout lin = (LinearLayout) v.findViewById(R.id.list);
        Holder holder;
        /*
       here I Use the same layout several times to produce all lists, I then hide them all and only make them visible when they become relevant
       since I inflate the same layout with the same ids I have to replace those Ids with new one, so that on onSavestate and onRestoreInstanceState
       work proprerly.
         */
        for(Limb l:Limb.values()) {
            if(l == Limb.ALL)
                continue;
            holder = new Holder();
            holder.main =  inflater.inflate(R.layout.view_usable_state, null, false);
            lin.addView(holder.main);
            holder.name = (TextView) holder.main.findViewById(R.id.name);
            holder.name.setId(View.generateViewId());
            holder.name.setText(l.getStringId());
            holder.loaded = (ImageView) holder.main.findViewById(R.id.loaded);
            holder.loaded.setId(View.generateViewId());
            holder.unloaded = (ImageView) holder.main.findViewById(R.id.unloaded);
            holder.unloaded.setId(View.generateViewId());
            holder.adapter = new EnumAdapter<>(getActivity(), holder.actions, R.layout.row_centered_text);
            ListView listView = ((ListView) holder.main.findViewById(R.id.actions));
            holder.main.findViewById(R.id.actions).setId(View.generateViewId());
            listView.setAdapter(holder.adapter);
            listView.setOnItemClickListener(this);
            listView.setTag(l);
            holderMap.put(l, holder);
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG, "onActivityCreated");

        mAttr = (Frag_PlayAttributes) getActivity().getFragmentManager().findFragmentById(R.id.attributes);
        mDisplayer = (Frag_Displayer) getActivity().getFragmentManager().findFragmentById(R.id.display);

        refresh();
    }

    protected void refresh(){
        Holder holder;
        Log.d(LOG,"refreshing");
        for(Map.Entry<Limb,Holder> entry:holderMap.entrySet()){
            if(st.hasUsable(entry.getKey())){
                holder = entry.getValue();
                holder.main.setVisibility(View.VISIBLE);
                holder.name.setText(st.getUsable(entry.getKey()).getName(getResources()));
                holder.actions.clear();
                holder.actions.addAll(st.getActions(entry.getKey()));
                holder.adapter.notifyDataSetChanged();
                holder.loaded.setVisibility(View.GONE);
                holder.unloaded.setVisibility(View.GONE);
                if((st.getUsable(entry.getKey()).getTyp()).isLoadable()) {
                    if (((Weapon) st.getUsable(entry.getKey())).getIsLoaded()) {
                        holder.loaded.setVisibility(View.VISIBLE);
                    } else {
                        holder.unloaded.setVisibility(View.VISIBLE);
                    }
                }
            }else
                entry.getValue().main.setVisibility(View.GONE);
        }
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
        Log.d(LOG," attributes "+st.getActionData(l,act).attributes.toString());
        for(Attribute attr:st.getActionData(l,act).attributes)
            Log.d(LOG,"- "+attr);
        mAttr.setSelection(st.getActionData(l, act).attributes);
        mDisplayer.setEnhancer(st.getActionData(l, act).getEnhancer());
        mDisplayer.setModif(st.getActionData(l, act).getModifier());
        if(act.hasResult())
            mDisplayer.setDegats(act.getResultNameId(),st.getActionData(l, act).resultDice,st.getActionData(l, act).resultValue,
                    st.getActionData(l, act).resultString);
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
