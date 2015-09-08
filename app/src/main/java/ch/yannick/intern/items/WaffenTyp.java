package ch.yannick.intern.items;

import ch.yannick.context.R;
import ch.yannick.display.technical.AdapterUsable;
import ch.yannick.intern.action_talent.Action;

/**
 * Created by Yannick on 02.03.2015.
 */

// This list must agree with the llist weapon_types in strings
public enum WaffenTyp implements AdapterUsable {
    BAREHANDS(R.string.bare_hands,false,new Action[]{Action.ESQUIV,Action.RUN,Action.WEATHERTEST,Action.OTHER}),
    SWORD(R.string.sword,false, new Action[]{Action.ATTACK,Action.DEFEND}),
    BISWORD(R.string.twohanded,true, new Action[]{Action.ATTACK,Action.DEFEND}),
    ONEAHALFSWORD(R.string.one_a_half_sword,false,new Action[]{Action.ATTACK,Action.TWOHANDEDATTACK ,Action.DEFEND}),
    RANGEWEAPON(R.string.range_weapon,true, new Action[]{Action.SHOOT,Action.LOAD}),
    SHIELD(R.string.shield,false, new Action[]{Action.DEFEND}),
    MANAWEAPON(R.string.mana_weapon,false, new Action[]{Action.FILLMANA,Action.USEMANA,Action.MANAATTACK});
    private int id;
    private boolean isTwohanded;
    private Action[] actions;

    WaffenTyp(int id, boolean isTwohanded, Action[] actions){
        this.id = id;
        this.isTwohanded = isTwohanded;
        this.actions = actions;
    }

    public int getStringId(){
        return id;
    }

    public boolean isTwohanded(){
        return isTwohanded;
    }

    public boolean isManaCaring(){
        for(Action a :actions){
            if(a == Action.FILLMANA)
                return true;
        }
        return false;
    }

    public boolean isLoadable(){
        for(Action a :actions){
            if(a == Action.LOAD)
                return true;
        }
        return false;
    }

    public Action[] getActions(){
        return actions;
    }
}
