package ch.yannick.intern.usables;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;
import ch.yannick.intern.action_talent.EffectType;
import ch.yannick.intern.action_talent.Talent;
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

public class Usable {
    private String mName;
    protected UsableTyp mType;
    protected Map<Action, ActionData> base_actions = new HashMap<>();

    public Usable(String name, UsableTyp type){
        mType = type;
        mName = name;
    }

    // Getter
    public String toString(){
        return mName;
    }

    public String getName(){
        return mName;
    }

    public UsableTyp getTyp(){
        return mType;
    }

    public ActionData getData(Action action){
        return new ActionData(base_actions.get(action));
    }

    public Set<Action> getActions(){
        return base_actions.keySet();
    }

    public boolean canAction(Action action){
        if(base_actions.containsKey(action)) {
            return true;
        }
        return false;
    }

    /*
    *this method checks if this is the right usable type, it is separated such that subclasses
    *can override it in case they have different types or count as any type
    */

    protected boolean isType(UsableTyp type){
        return mType == type;
    }

    // Talent related till the bottom
    public void setTalents(Map<Talent, Integer> talents, MentalState mentalState) {

        // copy the actions from the usable data
        for (ActionData data: base_actions.values())
            data.reset();

        // Add all remade actions, those are actions which are only available with corresponding Talents
        for (Map.Entry<Talent, Integer> e : talents.entrySet()) {
            if (e.getKey().getEffect() == EffectType.ACTIONREMAKE) {
                copyActionData(e.getKey().actionToCopy(), e.getKey().getAction());
                base_actions.get(e.getKey().getAction()).talentFatigue += e.getKey().getFatigueModifier();
                base_actions.get(e.getKey().getAction()).talentEnhancer += e.getKey().getEffect(mentalState, e.getValue());
            }
        }

        Action action;
        for (Map.Entry<Talent, Integer> e : talents.entrySet()) {
            if (e.getKey().getEffect().isAction() && isType( e.getKey().getUsableType())) {
                action = e.getKey().getAction();
                switch (e.getKey().getEffect()) {
                    // All action effects except ACTIONREMAKE, which is handled above, are listed here
                    case LUCKMODIFIER:
                        base_actions.get(action).talentModifier += e.getKey().getEffect(mentalState, e.getValue());
                        break;
                    case SKILLMODIFIER:
                        base_actions.get(action).talentEnhancer += e.getKey().getEffect(mentalState, e.getValue());
                        break;
                    case RESULTMODIFIER: // Not used very often might remove this or make it more prominent
                        base_actions.get(action).talentResult+= e.getKey().getEffect(mentalState, e.getValue());
                        break;
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
        if (!base_actions.containsKey(toCopy))
            base_actions.put(newAction,new ActionData(base_actions.get(toCopy)));
    }

    public void setEquipmentMalus(State state) {
        for(Map.Entry<Action,ActionData> entry:base_actions.entrySet()){
            entry.getValue().equipmeentFatigue = Resolver.getEquipmentFatigue(state,entry.getKey());
            // TODO entry.getValue().equipmentEnhancer =
            entry.getValue().equipmentModifier = Resolver.getEquipementModifier(state,entry.getKey());
        }
    }
}
