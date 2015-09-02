package ch.yannick.intern;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.yannick.activityMental.Vector;
import ch.yannick.enums.Action;
import ch.yannick.enums.Attribute;
import ch.yannick.enums.BodyPart;
import ch.yannick.enums.Dice;
import ch.yannick.enums.Effect;
import ch.yannick.enums.MentalState;
import ch.yannick.enums.Race;
import ch.yannick.enums.WaffenTyp;

public class State {

    private static String LOG= "State";
	private static final int HEAD=0,ARMS=1,LEGS=2,CHEST=3;
	public static final int AWARE=0,INCONSCIOUS =1,DEAD =2;
    public static final int ALL=0,LEFTARM =1,RIGHTARM =2,FREE = 3;
    public static boolean LEFT = true, RIGHT=false;

    private Personnage p;
	private Weapon wLinks, wRechts;
    private FreeAction wFree;
	private int liveNow,liveMax,staminaMax,staminaNow,staminaUsed;
	private ArrayList<Armor> mArmor;
	private boolean isLoaded;

    private Vector mentalPosition;
    private MentalState mentalState;

    public State(Personnage p){
		this.p = p;
		staminaUsed=0;
        wFree = new FreeAction(this);
        wFree.setTalents(p.getTalents());
		liveNow=liveMax = getHealthMaxStd();
		staminaNow = staminaMax = getStaminaMaxStd();
		mArmor = new ArrayList<>();
        mentalPosition = new Vector(0,0);
        computeMentalState();
		isLoaded = true;
	}

    public Long getId() {
        return p.getId();
    }

    public void setPersonnage(Personnage p){
        this.p = p;
    }
	
	public String getName(){
		return p.toString();
	}

    public Race getRace(){
        return p.getRasse();
    }

    public void setArmor(ArrayList<Armor> armor){
        mArmor = armor;
    }

    public ArrayList<Armor> getAllArmor() {
        return mArmor;
    }

    public int getProtection(BodyPart part){
        int res = 0;
        for(Armor a:mArmor){
            if(a.getPart()==part)
                res+= a.getProtection();
        }
        return res;
    }

    public void removeWeapon(boolean left){
        if(left)
            wLinks = null;
        else
            wRechts = null;
    }
	
	public void setWeapon(Weapon w, boolean left){
		if(left){
            wLinks = w;
            if(w.isTwohanded() || (wRechts != null && wRechts.isTwohanded()))
                wRechts = null;
        }else{
            wRechts = w;
            if(w.isTwohanded() || (wLinks != null && wLinks.isTwohanded()))
                wLinks = null;
        }
	}

    public boolean hasWeaponLeft(){
        return wLinks != null;
    }

    public boolean hasWeaponRight(){
        return wRechts != null;
    }

    public String getLeftWeaponName() {
        if(wLinks != null)
            return wLinks.getName();
        return "";
    }

    public String getRightWeaponName() {
        if(wRechts != null)
            return wRechts.getName();
        return "";
    }

    public boolean isDirect(int which) {
        switch(which) {
            case LEFTARM:
                return wLinks!=null && wLinks.isDirect;
            case RIGHTARM:
                return wRechts!= null && wRechts.isDirect;
            case FREE:
                return wFree.isDirect;
            default:
                return false;
        }
    }

    public boolean weaponRightIsLoadable() {
        return (wRechts != null && wRechts.getType() == WaffenTyp.RANGEWEAPON);
    }
    public boolean weaponLeftIsLoadable() {
        return (wLinks != null && wLinks.getType() == WaffenTyp.RANGEWEAPON);
    }

    public boolean weaponRightIsLoaded() {
        return (wRechts != null && wRechts.isLoaded);
    }

    public boolean weaponLeftIsLoaded() {
        return (wLinks != null && wLinks.isLoaded);
    }

    public void setHealth(int now, int max){
        liveNow = now;
        liveMax = max;
    }

    public int getHealthMaxStd(){
        Talent health = Talent.getTalent(Effect.HEALTHMAX.name());
        if(p.getTalents().containsKey(health)){
            return p.getVie() + health.getEffect(MentalState.NEUTRAL,p.getTalents().get(health));
        }
        return p.getVie();
    }

    public int getHealthMax(){ return liveMax;}

    public int getHealth(){
        return liveNow;
    }

    public void setStamina(int now, int used, int max) {
        staminaNow = now;
        staminaUsed = used;
        staminaMax = max;
    }

    public int getStaminaMaxStd(){
        Talent stam = Talent.getTalent(Effect.STAMINAMAX.name());
        if(p.getTalents().containsKey(stam)){
            return p.getVie() + stam.getEffect(MentalState.NEUTRAL,p.getTalents().get(stam));
        }
        return p.getStamina();
    }
	
	public int getStaminaMax(){
		return staminaMax;
	}
	
	public int getStaminaNow(){
		return staminaNow;
	}
	
	public int getStaminaUsed(){
		return staminaUsed;
	}

    // returns effective damage
	public int takeHit(BodyPart where, int damage, int pierce, boolean direct){
        pierce = Math.max(0,pierce);
        int armor= getProtection(where);
        if(!direct)
            damage -= Math.max(0,armor-pierce);
		liveNow -= damage;
        return damage;
	}

	public boolean isMagic() {
		return getSkill(Attribute.MAGIC)!=0;
	}

	public int getSkill(Attribute attribute) {
        return p.getAttr(attribute);
	}

	public void newRound() {
		staminaUsed = 0;
	}

    public List<Action> getActions(int which){
        Set<Action> set = new HashSet<>();
        if(which == ALL || which == FREE)
            set.addAll(wFree.getActions());
        if(wLinks != null && (which == LEFTARM || which == ALL))
            set.addAll(wLinks.actions.keySet());
        if(wRechts != null && (which == RIGHTARM || which == ALL))
            set.addAll(wRechts.actions.keySet());

        //remove all actions which take both hands
        if(wRechts!= null && wLinks != null){
            Iterator<Action> it = set.iterator();
            while(it.hasNext()){
                if(it.next().takesBothHands())
                    it.remove();
            }
        }

        ArrayList<Action> res = new ArrayList<Action>();
        res.addAll(set);
        return res;
    }

    public boolean canAct(int staminaCost, boolean reaction){
        if(staminaCost <0) return false;
        if(!reaction)
            staminaCost += staminaUsed;
        return staminaCost<=staminaNow;
    }

    public boolean canAct(Action action, int which, boolean reaction) {
        if(!canAct(getFatigue(action, which), reaction))
            return false;
        switch(which){
            case FREE:
                return wFree.canAction(action);
            case LEFTARM:
                return wLinks != null && wLinks.canAction(action);
            case RIGHTARM:
                return wRechts != null && wRechts.canAction(action);
            case ALL: // If any possiblilty allows this action
                return canAct(action, FREE, reaction) || canAct(action, LEFTARM, reaction)|| canAct(action, RIGHTARM, reaction);
            default:
                return false;
        }
    }

    public boolean act(int staminaCost, boolean reaction){
        if(!canAct(staminaCost,reaction))
            return false;
        else {
            staminaUsed += staminaCost;
            staminaNow -= reaction?staminaCost:staminaUsed;
            return true;
        }
    }

    public void action(Action action, int which){ // Just change of state no Fatigue!!!
        switch (which){
            case LEFTARM:
                wLinks.action(action);
                break;
            case RIGHTARM:
                wRechts.action(action);
                break;
            case FREE:
                wFree.action(action);
        }
    }

    public List<Attribute> getAttribute(Action act, int which){
        switch(which){
            case LEFTARM:
                return wLinks.getAttributes(act);
            case RIGHTARM:
                return wRechts.getAttributes(act);
            default:
                return wFree.getAttributes(act);
        }
    }


    public int getTalentEnhancer(Action act, int which){
        int base = getAvatarEnhancer(act);
        switch(which){
            case FREE:
                if(wFree != null)
                    return wFree.getEnhancer(act) + base;
            case LEFTARM:
                if(wLinks != null)
                   return wLinks.getEnhancer(act) + base;
            case RIGHTARM:
                if(wRechts != null)
                    return wRechts.getEnhancer(act) + base;
        }
        return 0;
    }

    public int getAvatarTalent(Action act){
        Talent tl = Talent.getTalent(act.name());
        if(tl!= null) {
            if (p.getTalents().containsKey(tl))
               return tl.getEffect(mentalState,p.getTalents().get(tl));
        }
        return 0;
    }


    public int getAvatarEnhancer(Action act){

        switch(act){
            case ESQUIV:
                return getAvatarEsquivTalent();
            case RUN:
                return getAvatarRunTalent();
            default:
                if(act.isFightAction())
                    return getAvatarFightTalent();
                else
                    return 0;
        }
    }

    public int getAvatarEsquivTalent(){
        int w = getWeight();
        return -(w/p.getRasse().getMovementCoeff())/2;
    }

    public int getAvatarRunTalent(){
        int w = getWeight();
        int c = p.getRasse().getMovementCoeff();
        return -Math.max(0,(w-100-c)/c);
    }

    public int getAvatarFightTalent(){
        int w = getWeight();
        int c = p.getRasse().getFightCoeff();
        int res = 0;
        Talent combat = Talent.getTalent(Effect.COMBAT.name());
        if(p.getTalents().containsKey(combat))
            res = combat.getEffect(this.mentalState,p.getTalents().get(combat));
        return - Math.max(0,((w-8*c)/c)/2)+res;
    }

    public int getDegats(int which){
        int res =0;
        Talent fury = Talent.getTalent(Effect.FURY.name());
        if(p.getTalents().containsKey(fury))
            res = fury.getEffect(this.mentalState,p.getTalents().get(fury));
        switch (which){
            case LEFTARM:
                if(hasWeaponLeft())
                    return wLinks.getSchaden()+res;
            case RIGHTARM:
                if(hasWeaponRight())
                    return wRechts.getSchaden()+res;
        }
        return 0;
    }

    public int getPenetration(int which){
        switch (which){
            case LEFTARM:
                if(hasWeaponLeft())
                    return wLinks.getPenetration();
            case RIGHTARM:
                if(hasWeaponRight())
                    return wRechts.getPenetration();
        }
        return 0;
    }

    public List<Dice> getDegatsDice(int which){
        switch (which){
            case LEFTARM:
                if(hasWeaponLeft())
                    return wLinks.schadenW();
            case RIGHTARM:
                if(hasWeaponRight())
                    return wRechts.schadenW();
        }
        return new ArrayList<Dice>();
    }

    public int getFatigue(Action act,int which) {
        int base = 0;
        if(act.isFightAction())
            base = getFatigueBase();
        if(act.isMouvementAction())
            base = getFatigueMouvememnt();
        switch (which){
            case FREE:
                return wFree.getFatigue(act)+base;
            case LEFTARM:
                if(wLinks == null)
                    return -1;
                return wLinks.getFatigue(act) + base;
            case RIGHTARM:
                if(wRechts == null)
                    return -1;
                return wRechts.getFatigue(act) + base;
            default:
                return 0;
        }
    }

    public int getWeight() {
        int weight = 0;
        for(Armor armor:mArmor)
            weight += armor.mWeight;
        if(wLinks != null)
            weight+= wLinks.mWeight;
        if(wRechts != null)
            weight += wRechts.mWeight;
        return weight;
    }

    public int getFatigueBase(){
        return (getWeight()/p.getRasse().getFatigueCoeff())/15;
    }

    public int getFatigueMouvememnt(){
        int w = getWeight();
        int a  = p.getRasse().getBaseFatigue();
        int b = p.getRasse().getFatigueCoeff();
        return a + (w/b)/10 + getFatigueBase();
    }

    public void putOn(Armor armor) {
        this.mArmor.add(armor);
    }

    public void removeArmor(Armor armor) {
        for(Iterator<Armor> it = mArmor.iterator();it.hasNext();){
            if(it.next().getId() == armor.getId()) {
                it.remove();
                break;
            }
        }
    }

    public Vector getMentalPosition(){
        return mentalPosition;
    }

    public void setMentalState(Vector position){
        this.mentalPosition = position;
        computeMentalState();
    }

    public void computeMentalState(){
        mentalState = MentalState.getState(mentalPosition);
    }

    public MentalState getMentalState(){
        return mentalState;
    }
}