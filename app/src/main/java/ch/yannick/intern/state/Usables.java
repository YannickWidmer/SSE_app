package ch.yannick.intern.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.personnage.Limb;
import ch.yannick.intern.personnage.Personnage;
import ch.yannick.intern.usables.Usable;
import ch.yannick.intern.usables.Weapon;

/**
 * Created by Yannick on 20.02.2016.
 */
public class Usables {
    private Map<Limb,Usable> mUsableMap = new HashMap<>();

    public void setBoniAndMalus(Map<Talent, Integer> talents, MentalState mentalState, State state){
        for(Usable usable: mUsableMap.values()) {
            usable.setTalents(talents, mentalState);
            usable.setEquipmentMalus(state);
        }
    }

    public int getWeight(){
        int weight =0;
        for(Usable usable: mUsableMap.values())
            if(usable instanceof  Weapon)
                weight += ((Weapon)usable).getWeight();
        return weight;
    }

    public void removeWeapon(Limb limb){
        mUsableMap.remove(limb);
    }

    public void setWeapon(Weapon w, Limb limb){
        if(limb == Limb.LEFTHAND || limb == Limb.RIGHTHAND || limb == Limb.BOTHHANDS)
            mUsableMap.remove(Limb.BOTHHANDS);
        if((w.getTyp()).isTwohanded()) {
            limb = Limb.BOTHHANDS;
            mUsableMap.remove(Limb.LEFTHAND);
            mUsableMap.remove(Limb.RIGHTHAND);
        }
        mUsableMap.put(limb, w);
    }

    public boolean hasUsable(Limb which){
        return mUsableMap.containsKey(which);
    }

    public Usable getUsable(Limb limb) {
        return mUsableMap.get(limb);
    }

    public Map<Limb,Usable> getUsableMap(){
        return mUsableMap;
    }

    public boolean canAction(Limb which, Action act){
        return hasUsable(which) && mUsableMap.get(which).canAction(act);
    }

    public List<Action> getActions(Limb which){
        Set<Action> set = new HashSet<>();
        // Add the resolved actions
        if(which == Limb.ALL)
            for(Usable w: mUsableMap.values())
                set.addAll(w.getActions());
        if(mUsableMap.containsKey(which))
            set.addAll(mUsableMap.get(which).getActions());

        // Remove both hands action if has two weapons
        if(wearsTwoWeapons()){
            Iterator<Action> it = set.iterator();
            while(it.hasNext()){
                if(it.next().is("TWOHANDED")) // TODO test if it works
                    it.remove();
            }
        }
        return new ArrayList<>(set);
    }

    public ActionData getActionData(Limb which, Action act){
        if(mUsableMap.containsKey(which) && mUsableMap.get(which).canAction(act))
            return mUsableMap.get(which).getData(act);
        return new ActionData(act);
    }

    public void combine(Limb main, Personnage p, MentalState mentalState) {
        Limb other;
        if(main == Limb.LEFTHAND)
            other = Limb.RIGHTHAND;
        else other = Limb.LEFTHAND;

        if(mUsableMap.containsKey(main) && mUsableMap.get(main) instanceof  Weapon
                && mUsableMap.containsKey(other) && mUsableMap.get(other) instanceof  Weapon){
            mUsableMap.put(Limb.BOTHHANDS, ((Weapon) mUsableMap.get(main)).combine(((Weapon) mUsableMap.get(other))));
            mUsableMap.get(Limb.BOTHHANDS).setTalents(p.getTalents(), mentalState);
            mUsableMap.remove(Limb.LEFTHAND);
            mUsableMap.remove(Limb.RIGHTHAND);
        }
    }

    private boolean wearsTwoWeapons(){
        return mUsableMap.containsKey(Limb.LEFTHAND) && mUsableMap.containsKey(Limb.RIGHTHAND);
        // When two swords are combined they become one twohanded weapon
    }
}
