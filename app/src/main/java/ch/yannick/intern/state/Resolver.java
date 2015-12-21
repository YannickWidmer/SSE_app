package ch.yannick.intern.state;

import android.util.Log;

import java.util.List;
import java.util.Map;

import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.EffectType;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.items.Armor;
import ch.yannick.intern.items.Equipement;
import ch.yannick.intern.personnage.Attribute;
import ch.yannick.intern.personnage.HitZone;
import ch.yannick.intern.personnage.Limb;

/**
 * Created by Yannick on 20.08.2015.
 * This class takes care of the talents of the Hero, State uses it to get any value which might be object of a talent.
 */
public class Resolver {
    private static String LOG ="Resolver";

    public static int getAvatarEnhancer(State state,Action act){
        return getEquipementModification(state, act) + getAvatarTalentEnhancer(state, act);
    }

    public static int getAvatarTalentEnhancer(State state, Action act){
        int res = 0;
        for(Map.Entry<Talent,Integer> entry:state.p.getTalents().entrySet()){
            if(entry.getKey().getEffect() == EffectType.ACTIONENHANCER && entry.getKey().getAction() == act)
                res += entry.getKey().getEffect(state.mentalState, entry.getValue());
        }
        return res;
    }

    public static int getAvatarTalentModificator(State state, Action act){
        int res = 0;
        for(Map.Entry<Talent,Integer> entry:state.p.getTalents().entrySet()){
            if(entry.getKey().getEffect() == EffectType.ACTIONMODIFIER && entry.getKey().getAction() == act)
                res += entry.getKey().getEffect(state.mentalState, entry.getValue());
        }
        return res;
    }

    public static int getEquipementModification(State state, Action act) {

        int weight = state.equipement.getWeight();
        int fight = state.p.getRasse().getFightCoeff();
        int move = state.p.getRasse().getMovementCoeff();
        int res = 0;
        switch (act) {
            case ESQUIV:
                res += -(weight / move / 2);
                break;
            case RUN:
                res += -Math.max(0, (weight - 100 - move) / move);
                break;
            case WEATHERTEST:
                res -= state.p.getAttr(Attribute.PHYSIQUE);
                for (Armor arm : state.equipement.getAllArmor()) {
                    res += arm.getWeatherProtection();
                    Log.d(LOG, "Weather from " + arm.getName() + " " + arm.getWeatherProtection());
                }
            default:
                if (act.isFightAction())
                    res -= Math.max(0, ((weight - 8 * fight) / fight) / 2);
        }
        return res;
    }


    // NIcht konsequent gebraucht
    public static int getBaseSkill(State state, Action act, Limb limb){
        List<Attribute> attributes = state.equipement.getWeapon(limb).getAttributes(act);
        int res = 0;
        for(Attribute attr : attributes)
            res += state.p.getAttr(attr);
        return 2*res/attributes.size();
    }

    public static int getFatigue(State state, Action act) {
        int weight = state.equipement.getWeight();
        int baseFatigue = state.p.getRasse().getBaseFatigue();
        int fatigue = state.p.getRasse().getFatigueCoeff();
        int res = 0;
        if (act.isFightAction() || act.isMouvementAction()) {
            res = Math.max(0, weight/fatigue/15 );
        }
        if(act.isMouvementAction())
            res += baseFatigue + weight/fatigue/10;
        return res;
    }

    public static int getValue(State state,Value value){
        int res = 0;
        for(Map.Entry<Talent,Integer> entry:state.p.getTalents().entrySet()){
            if(entry.getKey().getEffect() == EffectType.VALUE && entry.getKey().getValue() == value)
                res += entry.getKey().getEffect(state.mentalState,entry.getValue());
        }

        switch (value) {
            case HEALTH:
                return state.p.getVie() + res;
            case STAMINA:
                return state.p.getStamina() + res;
        }
        return 0;
    }

    public static int computeDamage(Equipement equipement,HitZone where, int damage, int pierce, boolean direct){
        pierce = Math.max(0,pierce);
        int armor= equipement.getProtection(where);
        if(!direct)
            damage -= Math.max(0,armor-pierce);
        return Math.max(damage,0);
    }

    public enum Value{
        HEALTH, STAMINA
    }
}
