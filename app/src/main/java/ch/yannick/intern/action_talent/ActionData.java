package ch.yannick.intern.action_talent;

import java.util.ArrayList;

import ch.yannick.intern.dice.Dice;
import ch.yannick.intern.personnage.Attribute;

/**
 * Created by Yannick on 08.01.2016.
 */
public class ActionData{
    // Must attributes
    public int fatigue;
    public int enhancer, modifier;
    public Attribute firstAttribute;
    public Attribute secondAttribute;

    // Result attributes
    public ArrayList<Dice> resultDice;
    public int resultValue;

    // Attack attributes
    public boolean isDirect = false;
    public int penetration;

    public ActionData(){
        fatigue =0;
        enhancer = 0;
        modifier = 0;
        firstAttribute = Attribute.ACUITY;
        secondAttribute = Attribute.ACUITY;
    }

    public ActionData(Action action){
        fatigue =action.getFatigue();
        enhancer = 0;
        modifier = 0;
        firstAttribute = action.getFirstAttribute();
        secondAttribute = action.getSecondAttribute();
        if (action.is("Attack")) {
            resultDice = new ArrayList<>();
        }
    }

    public ActionData(ActionData copy){
        fatigue =copy.fatigue;
        enhancer = copy.enhancer;
        modifier = copy.modifier;
        firstAttribute = copy.firstAttribute;
        secondAttribute = copy.secondAttribute;
        resultValue = copy.resultValue;
        penetration = copy.penetration;
        if(copy.resultDice != null)
            resultDice = new ArrayList<>(copy.resultDice);
    }
}