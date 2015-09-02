package ch.yannick.intern;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.yannick.enums.Action;
import ch.yannick.enums.Attribute;
import ch.yannick.enums.Dice;
import ch.yannick.enums.WaffenTyp;


public class Weapon {

    private static String LOG = "Weapon";

    protected WaffenTyp mType;
    protected boolean isDirect, isLoaded = true;
    protected String name;
	protected Long id;
    protected Map<Action,ActionData> actions;
   	protected ArrayList<Dice> schadenWuerfel;
    protected int schaden,penetration, mWeight;
    private Map<Talent, Integer> talents;

    public Weapon(Long id, String name, WaffenTyp type, boolean isDirect,int penetration, int schaden){
		this.name=name;
		this.id=id;
		schadenWuerfel = new ArrayList<Dice>();
		this.mType = type;
        this.isDirect = isDirect;
        this.penetration = penetration;
        this.schaden = schaden;
        actions = new HashMap<>();
        for(Action act:type.getActions())
            actions.put(act,new ActionData());
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
    public void changeType(WaffenTyp type){
        this.mType = type;
        actions = new HashMap<>();
        for(Action act:type.getActions())
            actions.put(act,new ActionData());
    }

	public void addDice(Dice dice){
		schadenWuerfel.add(dice);
	}

    public void setDices(List<Dice> dices){
        schadenWuerfel = new ArrayList<>();
        for(Dice d:dices)
            schadenWuerfel.add(d);
    }

    public void setWeight(int weight){
        mWeight = weight;
    }

    public int getWeight(){
        return mWeight;
    }

    public void setEnhancer(Action test,int number){
        if(actions.containsKey(test))
            actions.get(test).enhancer=number;
    }

    public void setFatigue(Action test,int number){
        if(actions.containsKey(test))
            actions.get(test).fatigue=number;
    }

    public void setActionAttributes(Action action, Attribute first, Attribute second){
        if(actions.containsKey(action)){
            actions.get(action).firstAttribute=first;
            actions.get(action).secondAttribute=second;
        }
    }

    public void setAction(Action test,Attribute first, Attribute second,int value,int fatigue){
        if(actions.containsKey(test)){
            actions.get(test).firstAttribute=first;
            actions.get(test).secondAttribute=second;
            actions.get(test).enhancer=value;
            actions.get(test).fatigue=fatigue;
        }
    }

    public Set<Action> getActions(){
        return actions.keySet();
    }

    public boolean canAction(Action action){
        if(actions.containsKey(action)) {
            switch (action) {
                case ATTACK: return isLoaded;
                case LOAD: return !isLoaded;
                default: return true;
            }
        }
        return false;
    }

    public void action(Action action){
        switch (action){
            case ATTACK:
                if(mType == WaffenTyp.RANGEWEAPON)
                    isLoaded = false;
                break;
            case LOAD:
                isLoaded = true;
        }
    }
	
	// Getter
    public int getSchaden() {
		return schaden;
	}
	
	public ArrayList<Dice> schadenW(){
		return schadenWuerfel;
	}
	
	public String getName(){
		return name;
	}

    public boolean isTwohanded(){
        return mType.isTwohanded();
    }

    public int getEnhancer(Action test){
        if(actions.containsKey(test))
            return actions.get(test).enhancer;
        Log.w(LOG,"trying to acces non action enhancer");
        return 0;
    }

    public int getFatigue(Action action){
        if(actions.containsKey(action))
            return actions.get(action).fatigue;
        Log.w(LOG,"trying to acces non action fatigue");
        return 0;
    }

	public ArrayList<Attribute> getAttributes(Action action){
        ArrayList<Attribute> res = new ArrayList<>();
        res.add(actions.get(action).firstAttribute);
        res.add(actions.get(action).secondAttribute);
        return res;
    }

    public void setIsDirect(boolean direct){
        this.isDirect = direct;
    }

    public boolean isDirect(){
        return isDirect;
    }
	
	public int penetration(){
        return penetration;
    }

    public void setDegats(int degats) {
        this.schaden = degats;
    }

    public void setPenetration(int penetration) {
        this.penetration = penetration;
    }

    public int getPenetration() {
        return penetration;
    }

    public void setLoad(boolean load){
        isLoaded = load;
    }

    protected class ActionData{
        protected int fatigue,enhancer;
        protected Attribute   firstAttribute, secondAttribute;

        public ActionData(){
            fatigue =0;
            enhancer = 0;
            firstAttribute = Attribute.ACUITY;
            secondAttribute = Attribute.ACUITY;
        }
    }
}