package ch.yannick.intern.state;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.yannick.context.datamanagement.DataManager;
import ch.yannick.display.activityMental.Vector;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.items.Clothe;
import ch.yannick.intern.personnage.Attribute;
import ch.yannick.intern.personnage.Character;
import ch.yannick.intern.personnage.HitZone;
import ch.yannick.intern.personnage.Limb;
import ch.yannick.intern.personnage.Race;
import ch.yannick.intern.usables.UsableInterface;
import ch.yannick.intern.usables.Weapon;


/**
 * Created by Yannick on 20.08.2015.
 * This class is the Top class of the Heros state, it contains the Hero itself, his equipement, his inventory and his Mentalstate aswell as his live and stamina value
 * All activity except those for creating the different object making up a hero should only use this class and it's methods.
 */

public class State {

    private static String LOG= "State";

    protected Character p;
    protected int liveNow,liveMax,staminaMax,staminaNow;
	protected Inventory mInventory = new Inventory();

    private Vector mentalPosition;
    protected MentalState mentalState;

    public State(Character p){
		this.p = p;
        mentalPosition = new Vector(0,0);
        computeMentalState();

        liveNow=liveMax = Resolver.getValue(this, Resolver.Value.HEALTH);
        staminaNow = staminaMax = Resolver.getValue(this,Resolver.Value.STAMINA);
        this.newRound();
	}

    public void getInventoryFromSQL(DataManager dM){
        mInventory.setFromDB(p.getId(), dM);
    }

    public void setPersonnage(Character p){
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
        mInventory.setBoniAndMalus(p.getTalents(), this);
    }

    public void setHealth(int now, int max){
        liveNow = Math.min(now,max);
        liveMax = max;
    }

    public int getHealthMax(){ return liveMax;}

    public int getHealth(){
        return liveNow;
    }

    public void setStamina(int now,  int max) {
        staminaNow = Math.min(now,max);
        staminaMax = max;
    }

	public int getStaminaMax(){
		return staminaMax;
	}
	
	public int getStaminaNow(){
		return staminaNow;
	}

    // returns effective damage
	public int takeHit(HitZone where, int damage, int pierce, boolean direct){
        damage = Resolver.computeDamage(mInventory,where,damage,pierce,direct);
		liveNow -= damage;
        return damage;
	}

	public boolean isMagic() {
		return getAttributeValue(Attribute.MAGIC)!=0;
	}


    public boolean canAct(int staminaCost){
        if(staminaCost <0) return false;
        return staminaCost<=staminaNow;
    }

    public boolean canAct(Action action, Limb which) {
        if(!canAct(mInventory.mUsables.getActionData(which,action).getFatigue()))
            return false;
        return mInventory.mUsables.hasUsable(which)
                        && mInventory.mUsables.canAction(which,action);
    }

    public boolean act(int staminaCost){
        if(!canAct(staminaCost))
            return false;
        else {
            staminaNow -= staminaCost;
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
        return mInventory.mUsables.getActions(which);
    }

    public void setUsable(Weapon u, Limb which){
        if(which == Limb.ALL)
            return;
        u.setTalents(p.getTalents(), mentalState);
        mInventory.mUsables.setUsable(u, which);
    }

    public void removeUsable(Limb which) {
        mInventory.mUsables.removeUsable(which);
    }

    public boolean hasUsable(Limb which){
        return mInventory.mUsables.hasUsable(which);
    }

    public ActionData getActionData(Limb which, Action action){
        return mInventory.mUsables.getActionData(which, action);
    }

    public Map<Limb,UsableInterface> getUsableMap(){
        return mInventory.mUsables.getUsableMap();
    }

    public UsableInterface getUsable(Limb limb) {
        return mInventory.mUsables.getUsable(limb);
    }

    public void combine(Limb limb) {
        mInventory.mUsables.combine(limb, p, mentalState);
    }

    public void putOn(Clothe armor) {
        mInventory.mEquipement.put(armor);
    }

    public ArrayList<Clothe> getAllArmor() {
        return mInventory.mEquipement.getAllArmor();
    }

    public void removeArmor(Clothe armor) {
        mInventory.mEquipement.removeArmor(armor);
    }

    public int getProtection(HitZone where){
        return mInventory.mEquipement.getProtection(where);
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
        return mInventory.getWeight();
    }

    public Inventory getInventory() {
        return mInventory;
    }

    public Set<Attribute> getAttributes() {
        return p.getAttributes();
    }
}