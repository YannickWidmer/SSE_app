package ch.yannick.intern.usables;

import java.util.List;
import java.util.Map;

import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.state.MentalState;

/**
 * Created by Yannick on 23.02.2016.
 */
public class TalentSetter {

    private TalentSetter(){
    }

    public static void setTalents(Map<Action,ActionData> actionDataMap, List<Action> allowedActions, UsableType typ,Map<Talent, Integer> talents, MentalState mentalState) {

        Action action;
        for (Map.Entry<Talent, Integer> e : talents.entrySet()) {
            if (e.getKey().getEffect().isAction() && typ == e.getKey().getUsableType()) {
                action = e.getKey().getAction();
                switch (e.getKey().getEffect()) {
                    // All action effects
                    case ALLOWACTION:
                         /* it first checks if this action is allowe and if it is not it checks if
                          * it is available, in which case it allows this action.
                          */
                        if (!allowedActions.contains(action)
                                && actionDataMap.containsKey(action))
                        allowedActions.add(e.getKey().getAction());
                    case LUCKMODIFIER:
                        actionDataMap.get(action).talentModifier += e.getKey().getEffect(mentalState, e.getValue());
                        break;
                    case SKILLMODIFIER:
                        actionDataMap.get(action).talentEnhancer += e.getKey().getEffect(mentalState, e.getValue());
                        break;
                    case RESULTMODIFIER: // Not used very often might remove this or make it more prominent
                        actionDataMap.get(action).talentResult+= e.getKey().getEffect(mentalState, e.getValue());
                        break;
                }
            }
        }
    }
}
