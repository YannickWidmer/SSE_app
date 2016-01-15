package ch.yannick.intern.usables;

import java.util.ArrayList;

import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;
import ch.yannick.intern.dice.Dice;
import ch.yannick.intern.personnage.Attribute;


public class Weapon extends Usables{

    private static String LOG = "Weapon";
    private UsableType  mCombinedType;
    private boolean isLoaded = true;

   	private int mWeight;

    public Weapon(String id, String name, UsableType type, int weight){
	    super(name,id,type);
        mWeight = weight;
    }


    public Weapon combine(Weapon toCombine){
        Weapon res = new Weapon(null,toString() +"-"+toCombine.toString(),mType,mWeight + toCombine.mWeight);
        res.mCombinedType = toCombine.mType;
        for(Action action:toCombine.getBase_actions()){
            // for each check if the main Weapon can the action or is weaker
            // than the to be combined and in this case put the information from to be combined
            if(!canAction(action) || base_actions.get(action).enhancer< toCombine.base_actions.get(action).enhancer)
                res.base_actions.put(action,new ActionData(toCombine.base_actions.get(action)));
        }

        // TODO spectial combination boni
        ActionData data;

        return res;
    }

    public int getWeight(){
        return mWeight;
    }

    public void setWeight(int weight){
        mWeight = weight;
    }

    private void setAction(Action test,Attribute first, Attribute second,int value,int fatigue,
                          int schaden, int penetration, boolean direct){
        if(base_actions.containsKey(test)){
            base_actions.get(test).firstAttribute = first;
            base_actions.get(test).secondAttribute = second;
            base_actions.get(test).enhancer = value;
            base_actions.get(test).fatigue = fatigue;
            base_actions.get(test).resultValue = schaden;
            base_actions.get(test).penetration = penetration;
            base_actions.get(test).isDirect = direct;
        }
    }

    private void setAction(Action test,Attribute first, Attribute second,int value,int fatigue,
                          int schaden, int penetration, boolean direct, ArrayList<Dice> damageDices){
        if(base_actions.containsKey(test)){
            base_actions.get(test).firstAttribute=first;
            base_actions.get(test).secondAttribute=second;
            base_actions.get(test).enhancer=value;
            base_actions.get(test).fatigue=fatigue;
            base_actions.get(test).resultDice =damageDices;
            base_actions.get(test).resultValue = schaden;
            base_actions.get(test).penetration = penetration;
            base_actions.get(test).isDirect = direct;
        }
    }

    // Editing Base actions methods
    public void addDice(Action action, Dice dice){
        if(action.hasResult() && base_actions.containsKey(action))
            base_actions.get(action).resultDice.add(dice);
    }

    // Attack attributes
    public boolean isDirect(Action action){
        return resolved_actions.get(action).isDirect;
    }

    public int penetration(Action action){
        return resolved_actions.get(action).penetration;
    }

    public int getPenetration(Action action) {
        return resolved_actions.get(action).penetration;
    }

    public void setLoad(boolean load){
        isLoaded = load;
    }

    public boolean getIsLoaded(){
        return isLoaded;
    }

}