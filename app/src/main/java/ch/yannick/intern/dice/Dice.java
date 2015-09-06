package ch.yannick.intern.dice;

import ch.yannick.context.R;

/**
 * Created by Yannick on 10.03.2015.
 */
public enum Dice {
    D4(4, R.drawable.w4),D6(6,R.drawable.w6),D8(8,R.drawable.w8),D10(10,R.drawable.w10),
    D12(12,R.drawable.w12),D16(16,R.drawable.w16),D20(20,R.drawable.w20),D30(30,R.drawable.w30);

    private int resource, eyes;

    Dice(int eyes, int resource){
        this.resource = resource;
        this.eyes = eyes;
    }

    public static Dice getSmallest(){
        return D4;
    }

    public int getDrawableId(){
        return resource;
    }

    public int getEyes(){return eyes;}
}
