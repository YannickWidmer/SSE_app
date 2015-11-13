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
import ch.yannick.intern.items.Weapon;
import ch.yannick.intern.personnage.Attribute;
import ch.yannick.intern.personnage.HitZone;
import ch.yannick.intern.personnage.Limb;
import ch.yannick.intern.personnage.Personnage;
import ch.yannick.intern.personnage.Race;


/**
 * Created by Yannick on 20.08.2015.
 * This class is the Top class of the Heros state, it contains the Hero itself, his equipement, his inventory and his Mentalstate aswell as his live and stamina value
 * All activity except those for creating the different object making up a hero should only use this class and it's methods.
 */

public class State {

    private static String LOG= "State";

    private Personnage p;
    private int liveNow,liveMax,staminaMax,staminaNow,staminaUsed;
	private Equipement equipement;

    private Vector mentalPosition;
    private MentalState mentalState;

    public State(Personnage p){
		this.p = p;
		staminaUsed=0;
        equipement = new Equipement();
        mentalPosition = new Vector(0,0);
        computeMentalState();

        liveNow=liveMax = getResolvedValue(Resolver.Value.HEALTH);
        staminaNow = staminaMax = getResolvedValue(Resolver.Value.STAMINA);
        this.newRound();
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
    }

    public void setPersonnage(Personnage p){
        this.p = p;
        equipement.setTalents(p.getTalents(),mentalState);
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
		return getSkill(Attribute.MAGIC)!=0;
	}

    public List<Action> getActions(Limb which){
        return equipement.getActions(which);
    }

    public boolean canAct(int staminaCost, boolean reaction){
        if(staminaCost <0) return false;
        if(!reaction)
            staminaCost += staminaUsed;
        return staminaCost<=staminaNow;
    }

    public boolean canAct(Action action, Limb which, boolean reaction) {
        if (which == Limb.ALL) {
            for (Limb limb : Limb.values()) {
                if (limb != Limb.ALL && canAct(action, limb, reaction))
                    return true;
            }
            return false;
        }
        return canAct(getFatigue(action, which), reaction)
                && equipement.hasWeapon(which)
                && equipement.getWeapon(which).canAction(action);
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
        if(equipement.hasWeapon(which))
            return equipement.getWeapon(which).getAttributes(act);
        return null;
    }

    public int getSkill(Attribute attribute) {
        return p.getAttr(attribute);
    }

    public int getSkillEnhancer(Action act, Limb which){
        int base = getResolvedEnhancer(act);
        if(equipement.hasWeapon(which))
            return equipement.getWeapon(which).getEnhancer(act)+base;
        return base;
    }

    public int getSkillModifier(Action act, Limb which){
        if(equipement.hasWeapon(which))
            return equipement.getWeapon(which).getModifier(act);
        return 0;
    }

    public Map<Talent,Integer> getTalents() {
        return p.getTalents();
    }

    public int getPenetration(Limb which, Action act){
        if(equipement.hasWeapon(which))
            return equipement.getWeapon(which).getPenetration(act);
        return 0;
    }

    public int getDegats(Limb which, Action act) {
        if (equipement.hasWeapon(which))
            return equipement.getWeapon(which).getSchaden(act);
        return 0;
    }

    public List<Dice> getDegatsDice(Limb which, Action act){
            if(equipement.hasWeapon(which))
                return equipement.getWeapon(which).schadenW(act);
            return new ArrayList<>();
    }

    public int getFatigue(Action act,Limb which) {
        int base = getResolvedFatigue(act);
        if(equipement.hasWeapon(which) && equipement.getWeapon(which).canAction(act)) {
            return base + equipement.getWeapon(which).getFatigue(act);
        }
        return base;
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

    public void setWeapon(Weapon w, Limb which){
        equipement.setWeapon(w, which);
        w.setTalents(p.getTalents(),mentalState);
    }

    public void removeWeapon(Limb which) {
        equipement.removeWeapon(which);
    }

    public boolean hasWeapon(Limb which){
        return equipement.hasWeapon(which);
    }

    public Weapon getWeapon(Limb which){
        return equipement.getWeapon(which);
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

    private int getResolvedEnhancer(Action act){
        return Resolver.getAvatarEnhancer(p, equipement, mentalState, act);
    }

    private int getResolvedFatigue(Action act){
        return Resolver.getFatigue(p, equipement, mentalState, act);
    }

    public int getResolvedValue(Resolver.Value value){
        return Resolver.getValue(equipement, p, mentalState, value);
    }

    public void combine(Limb limb) {
        equipement.combine(limb);
        equipement.getWeapon(Limb.BOTHHANDS).setTalents(p.getTalents(),mentalState);
    }
}