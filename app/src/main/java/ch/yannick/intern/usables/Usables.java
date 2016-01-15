package ch.yannick.intern.usables;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.dice.Dice;
import ch.yannick.intern.personnage.Attribute;
import ch.yannick.intern.state.MentalState;

/**
 * Created by Yannick on 08.01.2016.
 * This class represents all groups of actions, actions can be grouped by roles which are actually just another name for group of actions,
 * then also mainly weapons which also influence the values of the action and then all kind of tools or vehicles can be described.
 * This class also takes care of influence of talents on actions.
 * The Usable has it's own defined actions with defined properties independently of the Hero wearing it those are the BAse Actions.
 * The resolved actions are those which the Hero really uses.
 * There might be more actions than the base actions depending on the Heroes talents.
 */

public class Usables {
    private static int ID = 0;
    private String mName, mId;
    protected UsableType mType;
    protected Map<Action, ActionData> base_actions, resolved_actions;

    private boolean isLoaded;

    public Usables(String name,String id,UsableType type){
        mType = type;
        if(id == null)
            mId = "weapon_"+ (++ID);
        else
            mId = id;
        mName = name;
    }


    // Getter
    public String getId() {
        return mId;
    }

    public String toString(){
        return mName;
    }

    public int getResult(Action action) {
        return resolved_actions.get(action).resultValue;
    }

    public ArrayList<Dice> getResultDice(Action action){
        return resolved_actions.get(action).resultDice;
    }

    public int getEnhancer(Action test){
        if(resolved_actions.containsKey(test))
            return resolved_actions.get(test).enhancer;
        return 0;
    }

    public int getModifier(Action test){
        if(resolved_actions.containsKey(test))
            return resolved_actions.get(test).modifier;
        return 0;
    }

    public int getFatigue(Action action){
        return resolved_actions.get(action).fatigue;
    }

    public ArrayList<Attribute> getAttributes(Action action){
        ArrayList<Attribute> res = new ArrayList<>();
        res.add(resolved_actions.get(action).firstAttribute);
        res.add(resolved_actions.get(action).secondAttribute);
        return res;
    }


    public Set<Action> getBase_actions(){
        return base_actions.keySet();
    }

    public Set<Action> get_actions(){
        return resolved_actions.keySet();
    }

    public boolean canAction(Action action){
        if(resolved_actions.containsKey(action)) {
            if(action.is("ATTACK"))
                return isLoaded;
            if(action.is("LOADING"))
                return !isLoaded;
            return true;
        }
        return false;
    }


    /*
   this method checks if this is the right usable type, it is separated such that subclasses
   can override it in case they have different types or count as any type
    */

    protected boolean isType(UsableType type){
        return mType == type;
    }

    // Talent related till the bottom
    public void setTalents(Map<Talent, Integer> talents, MentalState mentalState) {
        //First remove all action which where added by previus talents
        resolved_actions.clear();

        // copy the from the usable data
        for (Action action : base_actions.keySet())
            copyActionData(action, action);

        Action action;
        boolean typeMatch;
        for (Map.Entry<Talent, Integer> e : talents.entrySet()) {

            if (e.getKey().getEffect().isAction() && isType( e.getKey().getUsableType())) {
                action = e.getKey().getAction();
                switch (e.getKey().getEffect()) {
                    case LUCKMODIFIER:
                        copyActionData(action, action);
                        resolved_actions.get(action).modifier += e.getKey().getEffect(mentalState, e.getValue());
                        break;
                    case SKILLMODIFIER:
                        copyActionData(action, action);
                        resolved_actions.get(action).enhancer += e.getKey().getEffect(mentalState, e.getValue());
                        break;
                    case RESULTMODIFIER:
                        copyActionData(action, action);
                        resolved_actions.get(action).resultValue += e.getKey().getEffect(mentalState, e.getValue());
                        break;
                    case ACTIONREMAKE:
                        copyActionData(e.getKey().actionToCopy(), action);
                        resolved_actions.get(action).fatigue += e.getKey().getFatigueModifier();
                        resolved_actions.get(action).enhancer += e.getKey().getEffect(mentalState, e.getValue());
                        break;
                    case VALUE:
                        break; // this is not resolved here, and has nothing to do with actions, instead it is taken care of in resolver!
                }
            }
        }
    }

    /* This method looks if the Action newAction is in the resolved Actions,
    *if it is not, it first looks if it is in the base_actions and to copy the AcitonData from there, if its not there
    * it makes a new ActionData and fills in the standard Data from Action.
    *
    * The actions toCopy and new should always both be Attack actions or not.
    */
    private void copyActionData(Action toCopy, Action newAction) {
        ActionData actionData;
        if (resolved_actions.containsKey(newAction))
            return; // nothing to do.

        if (base_actions.containsKey(toCopy)) {
            actionData = new ActionData(base_actions.get(toCopy));
        } else {
            actionData = new ActionData(newAction);
        }
        resolved_actions.put(newAction, actionData);
    }
}
