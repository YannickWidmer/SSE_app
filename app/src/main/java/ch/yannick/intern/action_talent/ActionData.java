package ch.yannick.intern.action_talent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.yannick.intern.dice.Dice;
import ch.yannick.intern.personnage.Attribute;

/**
 * Created by Yannick on 08.01.2016.
 */
public class ActionData{
    // Must attributes
    public int fatigue,enhancer, modifier;
    public boolean isRemake = false;
    // talent Bonus
    public int talentEnhancer, talentModifier, talentFatigue;
    // Equipement malus
    public int equipmentEnhancer, equipmentModifier, equipmeentFatigue;

    public Attribute[] attributes = new Attribute[2];

    // Result attributes
    public ArrayList<Dice> resultDice;
    public int resultValue, talentResult;

    // Attack attributes
    public boolean isDirect = false;
    public int penetration;

    public int getEnhancer(){
        return enhancer + talentEnhancer + equipmentEnhancer;
    }

    public int getModifier(){
        return modifier + talentModifier + equipmentModifier;
    }

    public int getFatigue(){
        return fatigue + talentFatigue + equipmeentFatigue;
    }

    public List<Attribute> getAttributes() {
        return Arrays.asList(attributes);
    }

    public ActionData(Action action){
        fatigue =action.getFatigue();
        enhancer = 0;
        modifier = 0;
        attributes[0] = action.getFirstAttribute();
        attributes[1] = action.getSecondAttribute();
        if (action.hasResult()) {
            resultDice = new ArrayList<>();
        }
    }

    public ActionData(ActionData copy){
        fatigue =copy.fatigue;
        enhancer = copy.enhancer;
        modifier = copy.modifier;
        attributes = copy.attributes.clone();
        resultValue = copy.resultValue;
        penetration = copy.penetration;
        if(copy.resultDice != null)
            resultDice = new ArrayList<>(copy.resultDice);
        // Talent
        talentEnhancer = copy.talentEnhancer;
        talentModifier = copy.talentModifier;
        talentFatigue = copy.talentFatigue;
        // equipment
        equipmentEnhancer = copy.equipmentEnhancer;
        equipmentModifier = copy.equipmentModifier;
        equipmeentFatigue = copy.equipmeentFatigue;
    }

    public void reset(){
        talentEnhancer = 0;
        talentModifier = 0;
        talentFatigue = 0;
        equipmentEnhancer = 0;
        equipmentModifier = 0;
        equipmeentFatigue = 0;
    }
}