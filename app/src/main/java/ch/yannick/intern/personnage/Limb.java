package ch.yannick.intern.personnage;

import ch.yannick.context.R;

/**
 * Created by Yannick on 19.08.2015.
 */
public enum Limb {
    LEFTHAND(R.string.left_hand),
    ROLE(R.string.role),
    RIGHTHAND(R.string.right_hand),
    BOTHHANDS(R.string.both_hands),
    FOOTS(R.string.foots),
    ALL(null);

    private Integer stringId;
    Limb(Integer id){
        stringId = id;
    }
    public int getStringId(){
        return stringId;
    }
}
