package ch.yannick.display.activityPlay;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;
import ch.yannick.intern.personnage.Limb;
import ch.yannick.intern.state.Resolver;
import ch.yannick.intern.state.State;
import ch.yannick.intern.usables.Usable;

/**
 * Created by Yannick on 06.12.2015.
 */
public class Frag_modif extends Fragment {
    private State st;
    private TalentModifAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_talent_boni, container, false);

        st =((RootApplication)getActivity().getApplication()).getCurrentState();
        adapter = new TalentModifAdapter(getActivity());
        refresh();
        return v;
    }

    public void refresh(){
        adapter.notifyDataSetChanged();
    }


    private class TalentModifAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private Context mContext;
        private List<Holder> mList = new ArrayList<>();

        public TalentModifAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public void notifyDataSetChanged() {
            mList.clear();
            for(Map.Entry<Limb,Usable> entry:st.getUsableMap().entrySet())
                for(Action action:entry.getValue().getActions())
                    mList.add(new Holder(
                            mContext.getResources().getString(entry.getKey().getStringId()),
                            entry.getValue().getName(),
                            mContext.getResources().getString(action.getStringId()),
                            entry.getValue().getData(action)));

            super.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = mList.get(position);
            if(convertView == null)
                convertView = mInflater.inflate(R.layout.list_item_talent_boni,parent,false);
            ((TextView)convertView.findViewById(R.id.action_name)).setText(holder.name);
            ((TextView)convertView.findViewById(R.id.action_skill)).setText(holder.getSkill());
            ((TextView)convertView.findViewById(R.id.action_modif)).setText(holder.getModif());
            ((TextView)convertView.findViewById(R.id.action_fatigue)).setText(holder.getFatigue());
            return convertView;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    private class Holder{
        String name;
        ActionData data;

        public Holder(String limb, String usable, String action,ActionData data ){
            name = limb+":"+usable+":"+action;
            this.data = data;
        }

        public android.text.Spanned getSkill(){
            return getSpanned(Resolver.getSkill(st,data.attributes),data.talentEnhancer,data.equipmentEnhancer);
        }

        public android.text.Spanned getModif(){
            return getSpanned(data.modifier, data.talentModifier, data.equipmentModifier);
        }

        public android.text.Spanned getFatigue() {
            return getSpanned(data.fatigue, data.talentFatigue, data.equipmeentFatigue);
        }

        private android.text.Spanned getSpanned(int base, int talent,int equip){
            return Html.fromHtml(" <font color=\"black\">"+ base+"</font> \n" +
                    (talent==0?"":"  <font color=\"green\">"+get(talent)+"</font> \n") +
                    (equip==0?"":"  <font color=\"red\">"+get(equip)+"</font> \n"));
        }

        private String get(int i){
            return i>0?"+"+i:""+i;
        }
    }
}
