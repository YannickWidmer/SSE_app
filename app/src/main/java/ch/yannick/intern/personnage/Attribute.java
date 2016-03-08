package ch.yannick.intern.personnage;

import ch.yannick.context.R;
import ch.yannick.display.technical.AdapterUsable;

/**
 * Created by Yannick on 02.03.2015.
 */
public enum Attribute implements AdapterUsable {
    FORCE(R.string.STR_KA,R.id.controler_KA, R.id.KEY_KA),
    AGILITY(R.string.STR_GK,R.id.controler_GK,R.id.KEY_GK),
    SPEED(R.string.STR_GW,R.id.controler_GW,R.id.KEY_GW),
    ASTUTENESS(R.string.STR_SZ,R.id.controler_SZ,R.id.KEY_SZ),
    CHARM(R.string.STR_CH,R.id.controler_CH, R.id.KEY_CH),
    ACUITY(R.string.STR_SS,R.id.controler_SS, R.id.KEY_SS),
    PHYSIQUE(R.string.STR_KB,R.id.controler_KB,R.id.KEY_KB),
    STAMINA(R.string.STR_AU,R.id.controler_AU,R.id.KEY_AU),
    WILL(R.string.STR_WI,R.id.controler_WI, R.id.KEY_WI),
    MAGIC(R.string.STR_MA,R.id.controler_MA, R.id.KEY_MA);

    private int controler, string, mId;
    Attribute(int stringId, int controler, int id){
        this.string = stringId;
        this.controler = controler;
        this.mId = id;
    }
    public int getStringId(){
        return string;
    }
    public int getControler(){return controler;}

    public int getId() {
        return mId;
    }
}
