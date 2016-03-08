package ch.yannick.intern.state;

import android.util.Log;

import java.util.List;
import java.util.Map;

import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.EffectType;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.items.Clothe;
import ch.yannick.intern.personnage.Attribute;
import ch.yannick.intern.personnage.HitZone;
import ch.yannick.intern.personnage.Limb;

/**
 * Created by Yannick on 20.08.2015.
 * This class takes care of the talents of the Hero, State uses it to get any value which might be object of a talent.
 */
public class Resolver {
    private static String LOG ="Resolver";

    public static int getEquipementModifier(State state, Action act) {
        int weight = state.mInventory.getWeight();
        int fight = state.p.getRasse().getFightCoeff();
        int move = state.p.getRasse().getMovementCoeff();
        int res = 0;
        if (act.getName().equals("ESQUIV")) // ESQUIV is handled diffferently as all other mouvement, it is sligthly different
                res += -(weight / move / 2);
        else if(act.is("Mouvement"))
            res += -Math.max(0, (weight - 100 - move) / move);
        else if(act.getName().equals("WEATHERTEST")) {
            res -= state.p.getAttr(Attribute.PHYSIQUE);
            for (Clothe arm : state.mInventory.getAllArmor()) {
                res += arm.getWeatherProtection();
                Log.d(LOG, "Weather from " + arm.getName() + " " + arm.getWeatherProtection());
            }
        }else if (act.is("Fight"))
                    res -= Math.max(0, ((weight - 8 * fight) / fight) / 2);
        return res;
    }

    public static int getEquipmentFatigue(State state, Action act) {
        int weight = state.mInventory.getWeight();
        int baseFatigue = state.p.getRasse().getBaseFatigue();
        int fatigue = state.p.getRasse().getFatigueCoeff();
        int res = 0;
        if (act.is("Fight") || act.is("Mouvement")) {
            res = Math.max(0, weight/fatigue/15 );
        }
        if(act.is("Mouvement")) // Additionally one more time for mouvement
            res += baseFatigue + weight/fatigue/10;
        return res;
    }

    // Nicht konsequent gebraucht
    public static int getBaseSkill(State state, Action act, Limb limb){
        return getSkill(state,state.getActionData(limb, act).attributes);
    }

    public static int getSkill(State state, List<Attribute> attributes){
        int res = 0;
        for(Attribute attr : attributes)
            res += state.p.getAttr(attr);
        return 2*res/attributes.size();
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

    public static int computeDamage(Inventory equipement,HitZone where, int damage, int pierce, boolean direct){
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
