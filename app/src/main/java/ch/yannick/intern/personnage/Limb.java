package ch.yannick.intern.personnage;

import ch.yannick.context.R;

/**
 * Created by Yannick on 19.08.2015.
 */
public enum Limb {
    LEFTHAND(R.string.left_hand), RIGHTHAND(R.string.right_hand),
    BOTHHANDS(R.string.both_hands),
    FOOTS(R.string.foots), BAREHANDS(R.string.bare_hands),ALL(0);


    private int stringId;
    Limb(int id){
        stringId = id;
    }
    public int getStringId(){
        return stringId;
    }
}
