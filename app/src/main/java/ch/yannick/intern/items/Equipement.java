package ch.yannick.intern.items;

import java.util.ArrayList;
import java.util.Iterator;

import ch.yannick.intern.personnage.HitZone;

/**
 * Created by Yannick on 19.08.2015.
 * this class represents what the Hero has equipped, these items have effects on him also the weight has some effects.
 */
public class Equipement {
    private ArrayList<Clothe> mArmor = new ArrayList<>();

    public int getProtection(HitZone part){
        int res = 0;
        for(Clothe a:mArmor){
            if(a.getPart()==part)
                res+= a.getProtection();
        }
        return res;
    }

    public void setArmor(ArrayList<Clothe> armor){
        mArmor = armor;
    }

    public ArrayList<Clothe> getAllArmor() {
        return mArmor;
    }

    public void put(Clothe armor) {
        this.mArmor.add(armor);
    }

    public void removeArmor(Clothe armor) {
        for(Iterator<Clothe> it = mArmor.iterator();it.hasNext();){
            if(it.next().getId().equals(armor.getId())) {
                it.remove();
                break;
            }
        }
    }

    public int getWeight() {
        int weight = 0;
        for(Clothe armor:mArmor)
            weight += armor.getWeight();
        return weight;
    }

}
