package ch.yannick.intern.usables;

import ch.yannick.context.R;
import ch.yannick.display.technical.AdapterUsable;
import ch.yannick.intern.action_talent.Action;

/**
 * Created by Yannick on 02.03.2015.
 */

// This list must agree with the llist weapon_types in strings
public enum UsableType implements AdapterUsable {
    GENERAL(R.string.bare_hands,false,new String[]{"ESQUIV","RUN","WEATHERTEST","OTHER"}),
    SWORD(R.string.sword,false, new String[]{"ATTACK","DEFEND"}),
    BISWORD(R.string.twohanded,true, new String[]{"TWOHANDEDATTACK","DEFEND"}),
    ONEAHALFSWORD(R.string.one_a_half_sword,false,new String[]{"ATTACK","TWOHANDEDATTACK" ,"DEFEND"}),
    TWOSWORDS(R.string.two_swords,true,new String[]{"ATTACK","TWOHANDEDATTACK","DEFEND"}),
    POLEWEAPON(R.string.pole_weapon,true,new String[]{"ATTACK","DEFEND","KEEPDISTANCE"}),
    RANGEWEAPON(R.string.range_weapon,true, new String[]{"SHOOT","LOAD"}),
    SHIELD(R.string.shield,false, new String[]{"DEFEND"});
    private int stringId;
    private boolean isTwohanded;
    private Action[] actions;

    UsableType(int stringId, boolean isTwohanded, String[] actionNames){
        this.stringId = stringId;
        this.isTwohanded = isTwohanded;
        actions = new Action[actionNames.length];
        for(int i = 0; i< actionNames.length;++i)
            actions[i] = Action.valueOf(actionNames[i]);
    }

    public int getStringId(){
        return stringId;
    }

    public boolean isTwohanded(){
        return isTwohanded;
    }

    public boolean isLoadable(){
        for(Action a :actions){
            if(a.is("LOADING"))
                return true;
        }
        return false;
    }
}
