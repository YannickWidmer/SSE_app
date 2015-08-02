package ch.yannick.enums;

import ch.yannick.context.R;
import ch.yannick.technical.AdapterUsable;

/**
 * Created by Yannick on 17.04.2015.
 */
public enum Race implements AdapterUsable {
    HUMAN(R.string.human,2,31,46,33),GARAN(R.string.garan,3,46,24,40),LEMUR(R.string.lemur,1,21,28,29),PFEILOR(R.string.pfeilor,4,60,15,15);

    private Race(int stringId, int baseFatigue, int fatigueCoeff, int movementCoeff, int fightCoeff){
        this.stringId = stringId;
        this.baseFatigue = baseFatigue;
        this.fatigueCoeff = fatigueCoeff;
        this.movementCoeff = movementCoeff;
        this.fightCoeff = fightCoeff;
    }

    private int stringId, baseFatigue,fatigueCoeff,movementCoeff,fightCoeff;

    public int getStringId(){
        return stringId;
    }


    public int getBaseFatigue() {
        return baseFatigue;
    }

    public int getFatigueCoeff(){ return fatigueCoeff;}

    public int getMovementCoeff(){ return movementCoeff;}

    public int getFightCoeff(){ return fightCoeff;}
}