package ch.yannick.intern.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.dice.Dice;
import ch.yannick.intern.personnage.Attribute;
import ch.yannick.intern.state.MentalState;


public class Weapon {

    private static String LOG = "Weapon";

    private WaffenTyp mType, mCombinedType;
    private boolean isLoaded = true;
    private String name;
	private Long id;
    // The weapon has it's own defined actions with defined properties independently of the Hero wearing it those are the BAse Actions.
    // The resolved actions are those which the Hero really uses. There might be more actions than the base actions depending on the Heroes talents.
    private Map<Action,ActionData> base_actions, resolved_actions;
   	private int mWeight;
    private int mMana;

    public Weapon(Long id, String name, WaffenTyp type){
		this.name=name;
		this.id=id;
		this.mType = type;
        base_actions = new HashMap<>();
        resolved_actions = new HashMap<>();
        for(Action action:type.getActions()) {
            base_actions.put(action, new ActionData(action));
        }
    }


    /* This method looks if the Action newActio is in the resolved Actions,
    *if it is not, it first looks if it is in the base_actions and to copy the AcitonData from there, if its not there
    * it makes a new ActionData and fills in the standard Data from Action.
    *
    * The actions toCopy and new should always both be Attack actions or not.
    */
    private void copyActionData(Action toCopy, Action newAction) {
        ActionData actionData;
        if(resolved_actions.containsKey(newAction))
            return; // nothing to do.

        if(base_actions.containsKey(toCopy)){
            actionData = new ActionData(base_actions.get(toCopy));
        }else{
            actionData = new ActionData(newAction);
        }
        resolved_actions.put(newAction,actionData);
    }

    public void setTalents(Map<Talent,Integer> talents,MentalState mentalState) {
        //First remove all action which where added by previus talents
        resolved_actions.clear();

        // Add the weapons data
        for(Action action:base_actions.keySet())
                copyActionData(action,action);

        Action action;
        boolean typeMatch;
        for(Map.Entry<Talent,Integer> e: talents.entrySet()) {
            typeMatch =  e.getKey().getWeaponType() == mType
                    ||  (mCombinedType != null && e.getKey().getWeaponType() == mCombinedType);
            if (e.getKey().getEffect().isAction() && typeMatch) {
                action = e.getKey().getAction();
                switch (e.getKey().getEffect()) {
                    case ACTIONMODIFIER:
                        copyActionData(action,action);
                        resolved_actions.get(action).modifier += e.getKey().getEffect(mentalState,e.getValue());
                        break;
                    case ACTIONENHANCER:
                        copyActionData(action,action);
                        resolved_actions.get(action).enhancer += e.getKey().getEffect(mentalState,e.getValue());
                        break;
                    case ATTACKDAMAGE:
                        copyActionData(action,action);
                        resolved_actions.get(action).schaden += e.getKey().getEffect(mentalState,e.getValue());
                        break;
                    case ACTIONREMAKE:
                        copyActionData(e.getKey().actionToCopy(), action);
                        resolved_actions.get(action).fatigue += e.getKey().getFatigueModifier();
                        resolved_actions.get(action).enhancer += e.getKey().getEffect(mentalState,e.getValue());
                        break;
                    case VALUE:
                        break; // this is not resolved here but in the class resolver!
                }
            }
        }
    }

    public Weapon combine(Weapon toCombine){
        Weapon res = new Weapon(null,getName() +"-"+toCombine.getName(),mType);
        res.mCombinedType = toCombine.mType;
        res.setWeight(mWeight + toCombine.mWeight);
        for(Action action:toCombine.getBase_actions()){
            // for each check if the main Weapn can the action or is weaker
            // than the tobe combined and in this case put the information from to be combined
            if(!canAction(action) || base_actions.get(action).enhancer< toCombine.base_actions.get(action).enhancer)
                res.base_actions.put(action,new ActionData(toCombine.base_actions.get(action)));
        }
        ActionData data;
        switch(mType){
            case SWORD:
                if(toCombine.mType == WaffenTyp.SWORD){
                    data = new ActionData(res.base_actions.get(Action.ATTACK));
                    data.enhancer +=4;
                    data.fatigue  +=1;
                    res.mType = WaffenTyp.TWOSWORDS;
                    res.base_actions.put(Action.TWOHANDEDATTACK,data);
                }else if(toCombine.mType == WaffenTyp.SHIELD){
                    res.base_actions.get(Action.DEFEND).enhancer +=4;
                }
        }
        return res;
    }


    public WaffenTyp getType(){
		return mType;
	}

	public Long getId(){
		return id;
	}
	
	public String toString(){
		return name;
	}


    // Editing methods
	public void addDice(Action action, Dice dice){
		if(action.isAttack() && base_actions.containsKey(action))
            base_actions.get(action).schadenWuerfel.add(dice);
	}

    public ActionData getData(Action action){
        return base_actions.get(action);
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
            base_actions.get(test).firstAttribute=first;
            base_actions.get(test).secondAttribute=second;
            base_actions.get(test).enhancer=value;
            base_actions.get(test).fatigue=fatigue;
            base_actions.get(test).schaden = schaden;
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
            base_actions.get(test).schadenWuerfel =damageDices;
            base_actions.get(test).schaden = schaden;
            base_actions.get(test).penetration = penetration;
            base_actions.get(test).isDirect = direct;
        }
    }

    public Set<Action> getBase_actions(){
        return base_actions.keySet();
    }

    public Set<Action> get_actions(){
        return resolved_actions.keySet();
    }

    public boolean canAction(Action action){
        if(resolved_actions.containsKey(action)) {
            switch (action) {
                case ATTACK: return isLoaded;
                case LOAD: return !isLoaded;
                default: return true;
            }
        }
        return false;
    }
	
	// Getter
    public int getSchaden(Action action) {
        return resolved_actions.get(action).schaden;
	}
	
	public ArrayList<Dice> schadenW(Action action){
		return resolved_actions.get(action).schadenWuerfel;
	}
	
	public String getName(){
		return name;
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

    public int getMana(){
        return mMana;
    }

    public void setMana(int mana){
        mMana = mana;
    }

    public class ActionData{
        public int fatigue;
        public int enhancer, modifier;
        public boolean isDirect = false;
        public Attribute   firstAttribute;
        public Attribute secondAttribute;
        public ArrayList<Dice> schadenWuerfel;
        public int schaden,penetration;

        public ActionData(){
            fatigue =0;
            enhancer = 0;
            modifier = 0;
            firstAttribute = Attribute.ACUITY;
            secondAttribute = Attribute.ACUITY;
        }

        public ActionData(Action action){
            fatigue =0;
            enhancer = 0;
            modifier = 0;
            firstAttribute = action.getFirstAttribute();
            secondAttribute = action.getSecondAttribute();
            fatigue = action.getFatigue();
            if (action.isAttack()) {
                schadenWuerfel = new ArrayList<>();
            }
        }

        public ActionData(ActionData copy){
            fatigue =copy.fatigue;
            enhancer = copy.enhancer;
            modifier = copy.modifier;
            firstAttribute = copy.firstAttribute;
            secondAttribute = copy.secondAttribute;
            schaden = copy.schaden;
            penetration = copy.penetration;
            if(copy.schadenWuerfel != null)
                schadenWuerfel = new ArrayList<>(copy.schadenWuerfel);
        }
    }
}