package ch.yannick.display.activityPlay;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.personnage.Limb;
import ch.yannick.intern.state.Resolver;
import ch.yannick.intern.state.State;

/**
 * Created by Yannick on 06.12.2015.
 */
public class Frag_modif extends Fragment {
    private State st;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_talent_boni, container, false);

        st =((RootApplication)getActivity().getApplication()).getState(getActivity().getIntent().getLongExtra("id", 0));

        refresh(v);
        return v;
    }

    public void refresh(){
        refresh(getView());
    }

    private void refresh(View v){
        ((TextView)v.findViewById(R.id.mouvement_skill)).setText(""+Resolver.getBaseSkill(st, Action.RUN, Limb.BAREHANDS));
        ((TextView)v.findViewById(R.id.mouvement_weigth)).setText(""+Resolver.getEquipementModification(st, Action.RUN));
        ((TextView)v.findViewById(R.id.mouvement_talent)).setText(""+Resolver.getAvatarEnhancer(st, Action.RUN));
        ((TextView)v.findViewById(R.id.mouvement_fatigue)).setText(""+Resolver.getFatigue(st, Action.RUN));

        ((TextView)v.findViewById(R.id.esquiv_skill)).setText(""+Resolver.getBaseSkill(st, Action.ESQUIV, Limb.BAREHANDS));
        ((TextView)v.findViewById(R.id.esquiv_weigth)).setText(""+Resolver.getEquipementModification(st, Action.ESQUIV));
        ((TextView)v.findViewById(R.id.esquiv_talent)).setText(""+Resolver.getAvatarEnhancer(st, Action.ESQUIV));
        ((TextView)v.findViewById(R.id.esquiv_fatigue)).setText(""+Resolver.getFatigue(st, Action.ESQUIV));

        //((TextView)findViewById(R.id.combat_skill)).setText(Resolver.getBaseSkill(st, Action.ATTACK, Limb.BAREHANDS));
        ((TextView)v.findViewById(R.id.combat_weigth)).setText(""+Resolver.getEquipementModification(st, Action.ATTACK));
        //((TextView)findViewById(R.id.combat_talent)).setText(""+Resolver.getAvatarEnhancer(st, Action.A));
        ((TextView)v.findViewById(R.id.combat_fatigue)).setText("x + "+Resolver.getFatigue(st, Action.ATTACK));
    }

}
