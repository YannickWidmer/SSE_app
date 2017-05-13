package ch.yannick.intern.usables;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.yannick.context.R;
import ch.yannick.display.technical.AdapterUsable;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;
import ch.yannick.intern.personnage.Attribute;

/**
 * Created by Yannick on 15.01.2016.
 */
public enum UsableType implements AdapterUsable{
    //   public ActionData(Action action,int fatigue,int enhancer, int modifier,int ticks, String result) {

    SWORD(R.string.sword,1, new ActionData[]{new ActionData(Action.valueOf("ATTACK"),0,4,0,3, Attribute.AGILITY.name()+","+Attribute.FORCE.name(),"Damage,D6,D6,D6"),
            new ActionData(Action.valueOf("DEFEND"),0,4,0,2,Attribute.AGILITY.name()+","+Attribute.SPEED.name(),"")}),
    LONGSWORD(R.string.longsword,1, new ActionData[]{new ActionData(Action.valueOf("ATTACK"),0,6,0,6,Attribute.AGILITY.name()+","+Attribute.FORCE.name(),"Damage,D6,D6,D6,D6,D6,D6"),
            new ActionData(Action.valueOf("DEFEND"),0,7,0,2,Attribute.AGILITY.name()+","+Attribute.SPEED.name(),"")}),
    BISWORD (R.string.twohanded,2, new ActionData[]{new ActionData(Action.valueOf("ATTACK"),0,11,0,8,Attribute.AGILITY.name()+","+Attribute.FORCE.name(),"Damage,D8,D8,D8,D8,D8,D8,D8"),
            new ActionData(Action.valueOf("DEFEND"),0,10,0,3,Attribute.AGILITY.name()+","+Attribute.SPEED.name(),"")}),
    DAGUE(R.string.dague,1, new ActionData[]{new ActionData(Action.valueOf("ATTACK"),0,2,0,2,Attribute.AGILITY.name()+","+Attribute.ACUITY.name(),"Damage,D12,D12,D12"),
            new ActionData(Action.valueOf("DEFEND"),0,1,0,1,Attribute.AGILITY.name()+","+Attribute.SPEED.name(),"")}),
    SHIELD(R.string.shield,1, new ActionData[]{new ActionData(Action.valueOf("CHARGE"),0,2,0,5,Attribute.FORCE.name()+","+Attribute.SPEED.name(),"Damage,D4,D4,D4"),
            new ActionData(Action.valueOf("DEFEND"),0,13,0,1,Attribute.AGILITY.name()+","+Attribute.SPEED.name(),"")}),
    AXE(R.string.axe,2, new ActionData[]{new ActionData(Action.valueOf("ATTACK"),0,11,0,9,Attribute.SPEED.name()+","+Attribute.FORCE.name(),"Damage,D12,D12,D12,D12,D12,D12,D12,D12"),
            new ActionData(Action.valueOf("DEFEND"),0,4,0,5,Attribute.AGILITY.name()+","+Attribute.FORCE.name(),"")}),
    MASSE(R.string.masse,2, new ActionData[]{new ActionData(Action.valueOf("CHARGE"),0,8,0,10,Attribute.SPEED.name()+","+Attribute.FORCE.name(),"Damage,D12,D12,D12,D12,D12,D12,D12,D12,D12,D12"),
            new ActionData(Action.valueOf("DEFEND"),0,1,0,6,Attribute.FORCE.name()+","+Attribute.ACUITY.name(),"")}),
    CROSSBOW(R.string.crossbow,2, new ActionData[]{new ActionData(Action.valueOf("SHOOT"),0,11,0,2,Attribute.AGILITY.name()+","+Attribute.ACUITY.name(),"Damage,D8,D8,D8,D8,D8,D8,D8,D8,D8,D8"),
            new ActionData(Action.valueOf("LOAD"),0,0,0,7,Attribute.ASTUTENESS.name()+","+Attribute.FORCE.name(),"")}),
    BOW(R.string.bow,2, new ActionData[]{new ActionData(Action.valueOf("SHOOT"),0,8,0,4,Attribute.AGILITY.name()+","+Attribute.ACUITY.name(),"Damage,D6,D6,D6,D6,D6,D6,D6")}),

    ROLE(R.string.role,0,new ActionData[0]);
    private int stringId;
    private int hands;
private List<Action> actions = new ArrayList<>();
    private Map<Action,ActionData> data = new HashMap<>();
//    private List<Action> supplementaryActions = new ArrayList<>();

    UsableType(int stringId, int hands, ActionData[] actionsdata){
        this.stringId = stringId;
        this.hands = hands;
        for(ActionData actionData:actionsdata) {
            Log.d("Usable",actionData+" ");
            actions.add(actionData.action);
            data.put(actionData.action, actionData);
        }
    }


    public List<Action> getActions(){
        return actions;
    }

    public ActionData getData(Action action){
        return data.get(action);
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
