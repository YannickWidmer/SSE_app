package ch.yannick.enums;

import ch.yannick.context.R;
import ch.yannick.technical.AdapterUsable;

/**
 * Created by Yannick on 02.03.2015.
 */

// This list must agree with the llist weapon_types in strings
public enum WaffenTyp implements AdapterUsable {
    SWORD(R.string.sword,false, new Action[]{Action.ATTACK,Action.DEFEND}),
    BISWORD(R.string.twohanded,true, new Action[]{Action.ATTACK,Action.DEFEND}),
    ONEAHALFSWORD(R.string.one_a_half_sword,false,new Action[]{Action.ATTACK,Action.TWOHANDEDATTACK ,Action.DEFEND}),
    RANGEWEAPON(R.string.range_weapon,true, new Action[]{Action.ATTACK,Action.LOAD}),
    SHIELD(R.string.shield,false, new Action[]{Action.DEFEND});
    private int id;
    private boolean isTwohanded;
    private Action[] actions;


    private WaffenTyp(int id, boolean isTwohanded, Action[] actions){
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

    public Action[] getActions(){
        return actions;
    }
}
