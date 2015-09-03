package ch.yannick.intern.personnage;

import ch.yannick.context.R;
import ch.yannick.display.technical.AdapterUsable;

/**
 * Created by Yannick on 02.05.2015.
 */
public enum HitZone implements AdapterUsable {
    HEAD(R.string.bodypart_head, new int[]{6},R.id.head), CHEST(R.string.bodypart_chest,new int[]{2,3},R.id.chest), ARMS(R.string.bodypart_arms,new int[]{4,5},R.id.arms),
    LEGS(R.string.bodypart_legs,new int[]{1},R.id.legs);

    private int striId, layoutId;
    private int[] treffer;
    private HitZone(int id, int[] treffer, int layoutId){
        striId = id;
        this.treffer = treffer;
        this.layoutId = layoutId;
    }
    public int getStringId(){
        return striId;
    }
    public int getLayoutId(){ return layoutId;}
}
