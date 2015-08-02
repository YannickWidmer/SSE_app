package ch.yannick.intern;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import ch.yannick.enums.Action;
import ch.yannick.enums.Attribute;
import ch.yannick.enums.Effect;
import ch.yannick.enums.MentalState;

/**
 * Created by Yannick on 18.07.2015.
 */
public class Talent {
    private Action mAction;
    private Attribute firstAttribute,secondAttribute;
    private Effect mEffect;
    private int[] mEffects;
    private int fatigue;
    private Map<MentalState,int[]> mMentalEffects;
    public static Map<String,Talent> values;

    public Talent(Action act,Attribute first, Attribute second,int fatigue){
        this.mAction = act;
        firstAttribute = first;
        secondAttribute = second;
        this.fatigue = fatigue;
        mEffect = Effect.ACTION;
    }

    public Talent(Action action,Attribute first, Attribute second, int[] effects,int fatigue){
        mEffects = effects;
        mAction = action;
        this.fatigue = fatigue;
        firstAttribute = first;
        secondAttribute = second;
        mEffect = Effect.ACTION;
    }

    public Talent(Action action,Attribute first, Attribute second, Map<MentalState,int[]> mentalEffects, int fatigue,boolean mental){
        mAction = action;
        firstAttribute = first;
        secondAttribute = second;
        this.fatigue = fatigue;
        mMentalEffects = mentalEffects;
        mEffect = Effect.ACTION;
    }

    public Talent(Effect effect, int[] effects){
        this.mEffects = effects;
        mEffect = effect;
    }

    public Talent(Effect effect, Map<MentalState,int[]> mentalEffects, boolean mental){
        mMentalEffects = mentalEffects;
        mEffect = effect;
    }

    public int getEffect(MentalState mentalState, int level){
        if(mEffects != null)
            return mEffects[Math.min(level,mEffects.length-1)];
        if(mMentalEffects!= null){
            if(mMentalEffects.containsKey(mentalState))
                return mMentalEffects.get(mentalState)[Math.min(level,mEffects.length-1)];
            else
                return 0;
        }
        return level;
    }

    public Effect getEffect(){
        return mEffect;
    }

    public Action getAction(){
        return mAction;
    }

    public String getName(){
        if(mEffect == Effect.ACTION)
            return mAction.name();
        return mEffect.name();
    }

    public long getStringId(){
        if(mEffect == Effect.ACTION)
            return mAction.getStringId();
        return mEffect.getStringId();
    }

    public static void init(InputStream ip){
        values = new HashMap<>();

        values.put(Action.STEAL.name(), new Talent(Action.STEAL,Attribute.ACUITY,Attribute.AGILLITY,4));
        values.put(Action.MAKEFIRE.name(),new Talent(Action.MAKEFIRE,Attribute.AGILLITY,Attribute.ACUITY,2));
        values.put(Action.COOK.name(),new Talent(Action.COOK,Attribute.AGILLITY,Attribute.ASTUTENESS,0));
        values.put(Action.BUTCHER.name(),new Talent(Action.BUTCHER,Attribute.AGILLITY,Attribute.FORCE,4));
        values.put(Action.FISHING.name(), new Talent(Action.FISHING,Attribute.AGILLITY,Attribute.SPEED,0));
        values.put(Action.CLIMB.name(),new Talent(Action.CLIMB,Attribute.AGILLITY,Attribute.FORCE,3));
        values.put(Action.SWIM.name(),new Talent(Action.SWIM,Attribute.FORCE,Attribute.PHYSIQUE,3));
        values.put(Action.THROW.name(), new Talent(Action.THROW,Attribute.AGILLITY,Attribute.ACUITY,1));
        values.put(Action.MAKEMUSIC.name(), new Talent(Action.MAKEMUSIC,Attribute.CHARM,Attribute.AGILLITY,0));
        values.put(Action.SING.name(),new Talent(Action.SING,Attribute.CHARM,Attribute.CHARM,0));

        values.put(Effect.HEALTHMAX.name(), new Talent(Effect.HEALTHMAX,new int[]{1,2,3,4,5,6,7,8,9,10}));
        values.put(Effect.STAMINAMAX.name(), new Talent(Effect.STAMINAMAX,new int[]{1,2,3,4,5,6,7,8,9,10}));

        HashMap<MentalState,int[]> temp = new HashMap<>();
        temp.put(MentalState.FOCUSED,new int[]{0,0,0,1,1,1,2,2,3,3});
        temp.put(MentalState.IMMERSED,new int[]{0,1,1,2,2,3,4,5,5,5});
        temp.put(MentalState.ECSTASY,new int[]{1,2,3,3,4,5,5,6,6,7});
        values.put(Action.CONJURE.name(),new Talent(Action.CONJURE,Attribute.ASTUTENESS,Attribute.MAGIC,temp,0,true));

        temp = new HashMap<>();
        temp.put(MentalState.FRENETIC,new int[]{0,0,0,1,1,1,2,2,3,3});
        temp.put(MentalState.AGRESSIVE,new int[]{0,1,1,2,2,3,4,5,5,5});
        temp.put(MentalState.BERSERK,new int[]{1,2,3,3,4,5,5,6,6,7});
        values.put(Effect.COMBAT.name(),new Talent(Effect.COMBAT,temp,true));

        temp = new HashMap<>();
        temp.put(MentalState.FRENETIC,new int[]{0,0,0,1,1,1,2,2,3,3});
        temp.put(MentalState.AGRESSIVE,new int[]{0,1,1,2,2,3,4,5,5,5});
        temp.put(MentalState.BERSERK,new int[]{1,2,3,3,4,5,5,6,6,7});
        values.put(Effect.FURY.name(),new Talent(Effect.FURY,temp,true));

    }

    public int getFatigue(){
        return fatigue;
    }

    public Attribute getFirstAttribute() {
        return firstAttribute;
    }

    public Attribute getSecontAttribute() {
        return secondAttribute;
    }

    public static Talent getTalent(String name){
        return values.get(name);
    }
}
