package ch.yannick.intern.state;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.yannick.display.activityMental.Vector;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.dice.Dice;
import ch.yannick.intern.items.Armor;
import ch.yannick.intern.items.Equipement;
import ch.yannick.intern.personnage.Attribute;
import ch.yannick.intern.personnage.HitZone;
import ch.yannick.intern.personnage.Limb;
import ch.yannick.intern.personnage.Personnage;
import ch.yannick.intern.personnage.Race;
import ch.yannick.intern.usables.Role;
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
    protected Usable mRole = Role.valueOf("FIGHTER");
    protected int liveNow,liveMax,staminaMax,staminaNow,staminaUsed;
	protected Equipement equipement;

    private Vector mentalPosition;
    protected MentalState mentalState;

    public State(Personnage p){
		this.p = p;
		staminaUsed=0;
        equipement = new Equipement();
        mentalPosition = new Vector(0,0);
        computeMentalState();

        liveNow=liveMax = Resolver.getValue(this, Resolver.Value.HEALTH);
        staminaNow = staminaMax = Resolver.getValue(this,Resolver.Value.STAMINA);
        this.newRound();
	}

    public void setPersonnage(Personnage p){
        this.p = p;
        equipement.setTalents(p.getTalents(), mentalState);
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
        equipement.setTalents(p.getTalents(),mentalState);
        mRole.setTalents(p.getTalents(),mentalState);
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

    public List<Action> getActions(Limb which){
        switch (which) {
            case ROLE:
                return new ArrayList<>(mRole.getActions());
            case ALL:
                List<Action> res = equipement.getActions(which);
                res.addAll(mRole.getActions());
                return res;
            default:
                return equipement.getActions(which);
        }
    }

    public boolean canAct(int staminaCost, boolean reaction){
        if(staminaCost <0) return false;
        if(!reaction)
            staminaCost += staminaUsed;
        return staminaCost<=staminaNow;
    }

    public boolean canAct(Action action, Limb which, boolean reaction) {
        if(!canAct(getFatigue(action, which), reaction))
            return false;
        switch (which){
            case ALL:
                for (Limb limb : Limb.values()) {
                    if (limb != Limb.ALL && canAct(action, limb, reaction))
                        return true;
                }
                return false;
            case ROLE:
                return mRole.canAction(action);
            default:
                return equipement.hasWeapon(which)
                        && equipement.getWeapon(which).canAction(action);
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

    public List<Attribute> getAttribute(Action act, Limb which){
        if(which == Limb.ROLE)
            return mRole.getAttributes(act);
        if(equipement.hasWeapon(which))
            return equipement.getWeapon(which).getAttributes(act);
        return null;
    }

    public int getAttributeValue(Attribute attribute) {
        return p.getAttr(attribute);
    }

    public int getSkillEnhancer(Action act, Limb which){
        if(which == Limb.ROLE)
            return mRole.getEnhancer(act);
        if(equipement.hasWeapon(which))
            return equipement.getWeapon(which).getEnhancer(act);
        return 0;
    }

    public int getSkillModifier(Action act, Limb which){
        int base = Resolver.getEquipementModification(this,act);
        if(which == Limb.ROLE)
            return mRole.getModifier(act);
        if(equipement.hasWeapon(which)) {
            return equipement.getWeapon(which).getModifier(act);
        }
        return 0;
    }

    public int getFatigue(Action act,Limb which) {
        int base = Resolver.getEquipmentRaceFatigue(this, act);
        if(which == Limb.ROLE)
            return base + mRole.getFatigue(act);
        if(equipement.hasWeapon(which) && equipement.getWeapon(which).canAction(act)) {
            return base + equipement.getWeapon(which).getFatigue(act);
        }
        return base;
    }

    // Result specific
    public int getPenetration(Limb which, Action act){
        // Roles dont have penetration
        if(equipement.hasWeapon(which) && act.is("Attack"))
            return equipement.getWeapon(which).getResolvedData(act).penetration;
        return 0;
    }

    public int getResultValue(Limb which, Action act) {
        if(which == Limb.ROLE)
            return mRole.getResult(act);
        if (equipement.hasWeapon(which))
            return equipement.getWeapon(which).getResult(act);
        return 0;
    }

    public List<Dice> getResultDice(Limb which, Action act){
        if(which == Limb.ROLE)
            return mRole.getResultDice(act);
        if(equipement.hasWeapon(which))
            return equipement.getWeapon(which).getResultDice(act);
        return new ArrayList<>();
    }

    public void setUsable(Usable u, Limb which){
        if(which == Limb.ALL)
            return;
        u.setTalents(p.getTalents(), mentalState);
        if(which == Limb.ROLE)
            mRole = u;
        else if(u instanceof Weapon) {
            equipement.setWeapon((Weapon) u, which);
        }
    }

    public void removeWeapon(Limb which) {
        equipement.removeWeapon(which);
    }

    public boolean hasWeapon(Limb which){
        return equipement.hasWeapon(which);
    }

    public Usable getUsable(Limb which){
        if(which == Limb.ROLE)
            return mRole;
        return equipement.getWeapon(which);
    }

    public void combine(Limb limb) {
        equipement.combine(limb);
        equipement.getWeapon(Limb.BOTHHANDS).setTalents(p.getTalents(),mentalState);
    }

    public Map<Talent,Integer> getTalents() {
        return p.getTalents();
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
        return equipement.getWeight();
    }
}