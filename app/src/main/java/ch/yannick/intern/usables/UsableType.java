package ch.yannick.intern.usables;

import java.util.ArrayList;
import java.util.List;

import ch.yannick.context.R;
import ch.yannick.intern.action_talent.Action;

/**
 * Created by Yannick on 15.01.2016.
 */
public enum UsableType {
    SWORD(R.string.sword,1, new String[]{"ATTACK","DEFEND"},new String[]{}),
    BISWORD (R.string.twohanded,2, new String[]{"TWOHANDEDATTACK","DEFEND"},new String[]{}),
    ONEAHALFSWORD(R.string.one_a_half_sword,1,new String[]{"ATTACK","TWOHANDEDATTACK" ,"DEFEND"},new String[]{}),
    TWOSWORDS(R.string.two_swords,2,new String[]{"ATTACK","TWOHANDEDATTACK","DEFEND"},new String[]{}),
    POLEWEAPON(R.string.pole_weapon,2,new String[]{"ATTACK","DEFEND","KEEPDISTANCE"},new String[]{}),
    RANGEWEAPON(R.string.range_weapon,2, new String[]{"SHOOT","LOAD"},new String[]{}),
    SHIELD(R.string.shield,1, new String[]{"DEFEND"},new String[]{"CHARGE"}),
    ARMSHIELD(R.string.arm_shield,0,new String[]{"DEFEND"},new String[]{}),
    SPELL(R.string.spell,0,new String[0],new String[]{}),
    ROLE(R.string.role,0,new String[0],new String[]{});
    private int stringId;
    private int hands;
    private List<Action> actions = new ArrayList<>();
    private List<Action> supplementaryActions = new ArrayList<>();

    UsableType(int stringId, int hands, String[] actionNames, String[] supplementaryActionNames){
        this.stringId = stringId;
        this.hands = hands;
        for(int i = 0; i< actionNames.length;++i)
            actions.add(Action.valueOf(actionNames[i]));
        for(int i = 0; i< supplementaryActionNames.length;++i)
            supplementaryActions.add(Action.valueOf(supplementaryActionNames[i]));

    }


    public List<Action> getActions(){
        return actions;
    }

    public List<Action> getSupplementaryActions() {
        return supplementaryActions;
    }

    public int getHands(){
        return hands;
    }

    public boolean isLoadable(){
        for(Action a :actions){
            if(a.is("LOADING"))
                return true;
        }
        return false;
    }

    public int getStringId(){
        return stringId;
    }
}
