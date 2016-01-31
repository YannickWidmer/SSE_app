package ch.yannick.intern.usables;

import java.util.ArrayList;

import ch.yannick.context.R;
import ch.yannick.intern.action_talent.Action;

/**
 * Created by Yannick on 15.01.2016.
 */
public class WeaponTyp extends UsableTyp {
    public static final WeaponTyp
    SWORD = new WeaponTyp("SWORD",R.string.sword,false, new String[]{"ATTACK","DEFEND"}),
    BISWORD = new WeaponTyp("BISWORD",R.string.twohanded,true, new String[]{"TWOHANDEDATTACK","DEFEND"}),
    ONEAHALFSWORD = new WeaponTyp("ONEHALFSWORD",R.string.one_a_half_sword,false,new String[]{"ATTACK","TWOHANDEDATTACK" ,"DEFEND"}),
    TWOSWORDS = new WeaponTyp("TWOSWORDS",R.string.two_swords,true,new String[]{"ATTACK","TWOHANDEDATTACK","DEFEND"}),
    POLEWEAPON = new WeaponTyp("POLEWEAPON",R.string.pole_weapon,true,new String[]{"ATTACK","DEFEND","KEEPDISTANCE"}),
    RANGEWEAPON = new WeaponTyp("RANGEWEAPON",R.string.range_weapon,true, new String[]{"SHOOT","LOAD"}),
    SHIELD = new WeaponTyp("SHIELD",R.string.shield,false, new String[]{"DEFEND"});

    private boolean isTwohanded;
    private Action[] actions;
    private static ArrayList<WeaponTyp> values = new ArrayList<>();

    protected WeaponTyp(String name, int stringId, boolean isTwohanded, String[] actionNames){
        super(name,stringId);
        this.isTwohanded = isTwohanded;
        actions = new Action[actionNames.length];
        values.add(this);
        for(int i = 0; i< actionNames.length;++i)
            actions[i] = Action.valueOf(actionNames[i]);
    }

    public static ArrayList<WeaponTyp> getValues(){
        return  values;
    }

    public Action[] getActions(){
        return actions;
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
