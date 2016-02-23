package ch.yannick.intern.state;

import java.util.ArrayList;
import java.util.Iterator;

import ch.yannick.intern.items.Armor;
import ch.yannick.intern.personnage.HitZone;

/**
 * Created by Yannick on 19.08.2015.
 * this class represents what the Hero has equipped, these items have effects on him also the weight has some effects.
 */
public class Equipement {
    private ArrayList<Armor> mArmor = new ArrayList<>();

    public int getProtection(HitZone part){
        int res = 0;
        for(Armor a:mArmor){
            if(a.getPart()==part)
                res+= a.getProtection();
        }
        return res;
    }

    public void setArmor(ArrayList<Armor> armor){
        mArmor = armor;
    }

    public ArrayList<Armor> getAllArmor() {
        return mArmor;
    }

    public void put(Armor armor) {
        this.mArmor.add(armor);
    }

    public void removeArmor(Armor armor) {
        for(Iterator<Armor> it = mArmor.iterator();it.hasNext();){
            if(it.next().getId().equals(armor.getId())) {
                it.remove();
                break;
            }
        }
    }

    public int getWeight() {
        int weight = 0;
        for(Armor armor:mArmor)
            weight += armor.getWeight();
        return weight;
    }

}
