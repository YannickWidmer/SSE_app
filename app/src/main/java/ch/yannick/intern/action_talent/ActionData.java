package ch.yannick.intern.action_talent;

import java.util.ArrayList;
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

    public List<Attribute> attributes = new ArrayList<>();

    // Result attributes
    public ArrayList<Dice> resultDice;
    public int resultValue = -1, talentResult;
    public String resultString = "";

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
        return attributes;
    }

    public ActionData(int fatigue,int enhancer, int modifier, String result) {
        this.fatigue = fatigue;
        this.enhancer = enhancer;
        this.modifier = modifier;
        setResultFromString(result);
    }

    public ActionData(Action action){
        fatigue =action.getFatigue();
        enhancer = 0;
        modifier = 0;
        attributes = action.getAttributes();
        if (action.hasResult()) {
            resultDice = new ArrayList<>();
        }
    }

    public ActionData(ActionData copy){
        set(copy);
    }

    public ActionData(ActionData copy,boolean remake){
        set(copy);
        isRemake = remake;
    }

    public void set(ActionData copy){
        fatigue =copy.fatigue;
        enhancer = copy.enhancer;
        modifier = copy.modifier;
        attributes = new ArrayList<>(copy.attributes);
        resultValue = copy.resultValue;
        resultString = copy.resultString;

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

    public String getAttributesString() {
        String res = "";
        for(Attribute attribute:attributes)
            res +=attribute.name()+",";
        return res;
    }

    public String getResult() {
        String res = "";
        if(resultValue !=-1){
            res = resultValue + ","+ resultString;
            if(resultDice != null)
                for(Dice d:resultDice)
                    res += ","+d.name();
        }
        return "";
    }
// TODO Test that those two cooperate
    public void setResultFromString(String result) {
        String[] list = result.split("\\s*,\\s*");
        if(list.length>1) {
            resultValue = Integer.valueOf(list[0]);
            resultString = list[1];
        }
        if(list.length>2){
            resultDice = new ArrayList<>();
            for(int i=2;i<list.length;++i)
                resultDice.add(Dice.valueOf(list[i]));
        }
    }
}