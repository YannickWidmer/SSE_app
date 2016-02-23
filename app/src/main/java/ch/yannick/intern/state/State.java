package ch.yannick.intern.state;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.yannick.display.activityMental.Vector;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.items.Armor;
import ch.yannick.intern.personnage.Attribute;
import ch.yannick.intern.personnage.HitZone;
import ch.yannick.intern.personnage.Limb;
import ch.yannick.intern.personnage.Personnage;
import ch.yannick.intern.personnage.Race;
import ch.yannick.intern.usables.Usable;
import ch.yannick.intern.usables.Weapon;


/**
 * Created by Yannick on 20.08.2015.
 * This class is the Top class of the Heros state, it contains the Hero itself, his equipement, his inventory and his Mentalstate aswell as his live and stamina value
 * All activity except those for creating the different object making up a hero should only use this class and it's methods.
 */

public class State {

    private static String LOG= "State";

    protected Personnage p;
    protected int liveNow,liveMax,staminaMax,staminaNow,staminaUsed;
	protected Equipement equipement = new Equipement();
    private Usables mUsables = new Usables();

    private Vector mentalPosition;
    protected MentalState mentalState;

    public State(Personnage p){
		this.p = p;
		staminaUsed=0;
        mentalPosition = new Vector(0,0);
        computeMentalState();

        liveNow=liveMax = Resolver.getValue(this, Resolver.Value.HEALTH);
        staminaNow = staminaMax = Resolver.getValue(this,Resolver.Value.STAMINA);
        this.newRound();
	}

    public void setPersonnage(Personnage p){
        this.p = p;
        newRound();
    }

    public Long getId() {
        return p.getId();
    }

	public String getName(){
		return p.toString();
	}

    public Race getRace(){
        return p.getRasse();
    }

    public void newRound() {
        staminaUsed = 0;
        mUsables.setBoniAndMalus(p.getTalents(), mentalState, this);
        p.setMalus(equipement);
    }

    public void setHealth(int now, int max){
        liveNow = Math.min(now,max);
        liveMax = max;
    }

    public int getHealthMax(){ return liveMax;}

    public int getHealth(){
        return liveNow;
    }

    public void setStamina(int now, int used, int max) {
        staminaNow = Math.min(now,max);
        staminaUsed = used;
        staminaMax = max;
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
	public int takeHit(HitZone where, int damage, int pierce, boolean direct){
        damage = Resolver.computeDamage(equipement,where,damage,pierce,direct);
		liveNow -= damage;
        return damage;
	}

	public boolean isMagic() {
		return getAttributeValue(Attribute.MAGIC)!=0;
	}

    public boolean canAct(int staminaCost, boolean reaction){
        if(staminaCost <0) return false;
        if(!reaction)
            staminaCost += staminaUsed;
        return staminaCost<=staminaNow;
    }

    public boolean canAct(Action action, Limb which, boolean reaction) {
        if(!canAct(mUsables.getActionData(which,action).getFatigue(), reaction))
            return false;
        return mUsables.hasUsable(which)
                        && mUsables.canAction(which,action);
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

    public Map<Talent,Integer> getTalents() {
        return p.getTalents();
    }

    public int getAttributeValue(Attribute attribute) {
        return p.getAttr(attribute);
    }

    public List<Action> getActions(Limb which){
        return mUsables.getActions(which);
    }

    public void setUsable(Usable u, Limb which){
        if(which == Limb.ALL)
            return;
        u.setTalents(p.getTalents(), mentalState);
        mUsables.setWeapon((Weapon) u, which);
    }

    public void removeUsable(Limb which) {
        mUsables.removeWeapon(which);
    }

    public boolean hasUsable(Limb which){
        return mUsables.hasUsable(which);
    }

    public ActionData getActionData(Limb which, Action action){
        return mUsables.getActionData(which, action);
    }

    public Map<Limb,Usable> getUsableMap(){
        return mUsables.getUsableMap();
    }

    public Usable getUsable(Limb limb) {
        return mUsables.getUsable(limb);
    }

    public void combine(Limb limb) {
        mUsables.combine(limb, p, mentalState);
    }

    public void putOn(Armor armor) {
        this.equipement.put(armor);
    }

    public ArrayList<Armor> getAllArmor() {
        return equipement.getAllArmor();
    }

    public void removeArmor(Armor armor) {
        equipement.removeArmor(armor);
    }

    public int getProtection(HitZone where){
        return equipement.getProtection(where);
    }

    public Vector getMentalPosition(){
        return mentalPosition;
    }

    private void computeMentalState(){
        mentalState = MentalState.getState(mentalPosition);
    }

    public MentalState getMentalState(){
        return mentalState;
    }

    public void setMentalState(Vector position){
        this.mentalPosition = position;
        computeMentalState();
    }

    public int getWeight() {
        return equipement.getWeight() + mUsables.getWeight();
    }
}