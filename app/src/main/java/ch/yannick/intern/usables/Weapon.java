package ch.yannick.intern.usables;

import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.dice.Dice;
import ch.yannick.intern.items.Item;
import ch.yannick.intern.personnage.Attribute;
import ch.yannick.intern.state.MentalState;
import ch.yannick.intern.state.Resolver;
import ch.yannick.intern.state.State;

/**
 * Created by Yannick on 08.01.2016.
 * This class represents all groups of actions, actions can be grouped by roles which are actually just another name for group of actions,
 * then also mainly weapons which also influence the values of the action and then all kind of tools or vehicles can be described.
 * This class also takes care of influence of talents on actions.
 * The Usable has it's own defined actions with defined properties independently of the Hero wearing it those are the BAse Actions.
 * The resolved actions are those which the Hero really uses.
 * There might be more actions than the base actions depending on the Heroes talents.
 */

public class Weapon extends Item implements UsableInterface{
    private static final String LOG ="Weapon";
    protected UsableType mType, mCombinedType;
    protected ArrayList<Action> allowed_action = new ArrayList<>();
    protected Map<Action, ActionData> actions = new HashMap<>();
    private boolean isLoaded = true;

    private void set(UsableType type){
        mType = type;
        actions.clear();
        allowed_action.clear();
        for(Action action:type.getActions()){
            Log.d(LOG,action+"");
            actions.put(action,new ActionData(type.getData(action)));
            allowed_action.add(action);
        }
    }

    public Weapon(Long id, Long serverId, Long owner, String name, String description, int weight, UsableType type){
        super(id, serverId, owner,name,description,weight);
        set(type);
    }

    public Weapon(Item item, UsableType type){
        super(item);
        set(type);
    }

    public Weapon(UsableType type, String name){
        super( null, null, null, name , "",0);
        set(type);
    }

    // Getter
    @Override
    public UsableType getTyp(){
        return mType;
    }

    @Override
    public ActionData getData(Action action){
        return new ActionData(actions.get(action));
    }

    @Override
    public List<Action> getActions(){
        return allowed_action;
    }

    @Override
    public boolean canAction(Action action){
        return allowed_action.contains(action);
    }

    public Weapon combine(Weapon toCombine){
        Weapon res = new Weapon(mType,toString() +"-"+toCombine.toString());
        res.mCombinedType = toCombine.mType;
        res.mWeight = mWeight + toCombine.mWeight;
        for(Action action:toCombine.actions.keySet()){
            // for each check if the main Weapon can the action or is weaker
            // than the to be combined and in this case put the information from to be combined
            if(!canAction(action) || actions.get(action).enhancer< toCombine.actions.get(action).enhancer)
                res.actions.put(action,new ActionData(toCombine.actions.get(action)));
        }

        // TODO special combination boni
        return res;
    }


    @Override
    public void setTalents(Map<Talent, Integer> talents, MentalState mentalState) {
        for (ActionData data : actions.values())
            data.reset();
        allowed_action.clear();
        for (Action action : mType.getActions())
            allowed_action.add(action);

        if (mCombinedType != null) {
            for (Action action : mCombinedType.getActions())
                if(!allowed_action.contains(action))
                    allowed_action.add(action);
            TalentSetter.setTalents(actions, allowed_action, mCombinedType, talents, mentalState);
        }

        TalentSetter.setTalents(actions, allowed_action, mType, talents, mentalState);
    }

    @Override
    public String getName(Resources resources) {
        return getName();
    }

    public void setEquipmentMalus(State state) {
        for(Map.Entry<Action,ActionData> entry:actions.entrySet()){
            entry.getValue().equipmeentFatigue = Resolver.getEquipmentFatigue(state,entry.getKey());
            // TODO entry.getValue().equipmentEnhancer =
            entry.getValue().equipmentModifier = Resolver.getEquipementModifier(state,entry.getKey());
        }
    }

    public void addData(Action action, ActionData data) {actions.put(action,data);}


    // Editing Methods
    private void setAction(Action test,List<Attribute> attributes,int value,int fatigue){
        if(actions.containsKey(test)) {
            actions.get(test).attributes = attributes;
            actions.get(test).enhancer = value;
            actions.get(test).fatigue = fatigue;
        }
    }

    private void setAction(Action test,List<Attribute> attributes,int value,int fatigue,
                            String resultString, ArrayList<Dice> damageDices){
        if(actions.containsKey(test)){
            actions.get(test).attributes = attributes;
            actions.get(test).enhancer=value;
            actions.get(test).fatigue=fatigue;
            actions.get(test).resultDice =damageDices;
            actions.get(test).resultString = resultString;
        }
    }


    // Editing Base actions methods
    public void addDice(Action action, Dice dice){
        if(action.hasResult() && actions.containsKey(action))
            actions.get(action).resultDice.add(dice);
    }

    public void setLoad(boolean load){
        isLoaded = load;
    }

    public boolean getIsLoaded(){
        return isLoaded;
    }
}
