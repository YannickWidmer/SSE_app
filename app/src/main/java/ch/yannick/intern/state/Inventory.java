package ch.yannick.intern.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.yannick.context.datamanagement.DataManager;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.items.Clothe;
import ch.yannick.intern.items.Equipement;
import ch.yannick.intern.items.Item;
import ch.yannick.intern.items.Usables;
import ch.yannick.intern.personnage.HitZone;
import ch.yannick.intern.usables.Weapon;

/**
 * Created by Yannick on 19.08.2015.
 * This class represents the Heros possesions,
 * what he bears with him and what he lays down, from here he can equip stuff. The only effect might be the weight.
 */
public class Inventory {
    Usables mUsables = new Usables();
    Equipement mEquipement = new Equipement();
    List<Item> items = new ArrayList<>();

    public void setFromDB(Long id, DataManager data){
        //TODO let it do this in background
        items = data.getAllItem(id);
    }

    public void setBoniAndMalus(Map<Talent, Integer> talents, State state) {
        mUsables.setBoniAndMalus(talents,state.getMentalState(),state);
    }

    public int getWeight() {
        return mEquipement.getWeight() + mUsables.getWeight();
    }

    public ArrayList<Clothe> getAllArmor() {
        return mEquipement.getAllArmor();
    }

    public int getProtection(HitZone where) {
        return mEquipement.getProtection(where);
    }

    public ArrayList<Weapon> getAllWeapon() {
        ArrayList<Weapon> res = new ArrayList<>();
        for(Item it:items)
        if(it instanceof  Weapon)
            res.add((Weapon) it);
        return res;
    }
}
