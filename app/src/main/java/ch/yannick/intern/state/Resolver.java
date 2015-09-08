package ch.yannick.intern.state;

import android.util.Log;

import java.util.Map;

import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.EffectType;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.items.Armor;
import ch.yannick.intern.items.Equipement;
import ch.yannick.intern.personnage.Attribute;
import ch.yannick.intern.personnage.HitZone;
import ch.yannick.intern.personnage.Personnage;

/**
 * Created by Yannick on 20.08.2015.
 * This class takes care of the talents of the Hero, State uses it to get any value which might be object of a talent.
 */
public class Resolver {
    private static String LOG ="Resolver";

    public static int getAvatarEnhancer(Personnage personnage, Equipement equipement, MentalState ms,Action act){
        
        int weight = equipement.getWeight();
        int fight = personnage.getRasse().getFightCoeff();
        int move = personnage.getRasse().getMovementCoeff();
        int res = 0;
        switch(act) {
            case ESQUIV:
                res += -(weight / move / 2);
                break;
            case RUN:
                res += -Math.max(0, (weight - 100 - move) / move);
                break;
            case WEATHERTEST:
                res -= personnage.getAttr(Attribute.PHYSIQUE);
                for(Armor arm:equipement.getAllArmor()) {
                    res += arm.getWeatherProtection();
                    Log.d(LOG,"Weather from "+arm.getName()+" "+arm.getWeatherProtection());
                }
            default:
                if(act.isFightAction())
                    res -= Math.max(0, ((weight - 8 * fight) / fight) / 2);
        }
        for(Map.Entry<Talent,Integer> entry:personnage.getTalents().entrySet()){
            if(entry.getKey().getEffect() == EffectType.ACTIONENHANCER && entry.getKey().getAction() == act)
                res += entry.getKey().getEffect(ms, entry.getValue());
        }
        return res;
    }

    public static int getFatigue(Personnage p, Equipement equipement, MentalState mentalState, Action act) {
        int weight = equipement.getWeight();
        int baseFatigue = p.getRasse().getBaseFatigue();
        int fatigue = p.getRasse().getFatigueCoeff();
        int res = 0;
        if (act.isFightAction() || act.isMouvementAction()) {
            res = Math.max(0, weight/fatigue/15 );
        }
        if(act.isMouvementAction())
            res += baseFatigue + weight/fatigue/10;
        return res;
    }

    public static int getValue(Equipement equipement, Personnage personnage, MentalState ms,Value value){
        int res = 0;
        for(Map.Entry<Talent,Integer> entry:personnage.getTalents().entrySet()){
            if(entry.getKey().getEffect() == EffectType.VALUE && entry.getKey().getValue() == value)
                res += entry.getKey().getEffect(ms,entry.getValue());
        }

        switch (value) {
            case HEALTH:
                return personnage.getVie() + res;
            case STAMINA:
                return personnage.getStamina() + res;
        }
        return 0;

    }

    public static int computeDamage(Equipement equipement,HitZone where, int damage, int pierce, boolean direct){
        pierce = Math.max(0,pierce);
        int armor= equipement.getProtection(where);
        if(!direct)
            damage -= Math.max(0,armor-pierce);
        return damage;
    }

    public enum Value{
        HEALTH, STAMINA
    }
}
