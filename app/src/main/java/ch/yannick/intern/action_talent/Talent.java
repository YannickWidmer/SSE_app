package ch.yannick.intern.action_talent;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.yannick.context.R;
import ch.yannick.intern.items.WaffenTyp;
import ch.yannick.intern.state.MentalState;
import ch.yannick.intern.state.Resolver;

/**
 * Created by Yannick on 18.07.2015.
 */
public class Talent {
    public static HashMap<String,Talent> values;
    public static HashMap<String,ArrayList<Talent>> talentGroups;

    private EffectType mEffect;
    private int mStringId;
    private Action mAction, remadeAction;
    private WaffenTyp mWeaponType;
    private Resolver.Value mValue;

    // The effects value, depending if it depends on the Mental state or not
    private int[] mEffects;
    private Map<MentalState,int[]> mMentalEffects;
    // If the Talent changes the usual action
    private int mFatigueModifier, mEnhancerModifier, mDegatsModifier;

    public Talent(int stringID,Action act, WaffenTyp wt){
        this.mAction = act;
        mWeaponType = wt;
        mStringId = stringID;
        mEffect = EffectType.ACTIONMODIFIER;
    }

    public Talent(int stringID,Action action, WaffenTyp wt,int[] effects){
        mEffects = effects;
        mWeaponType = wt;
        mStringId = stringID;
        mAction = action;
        mEffect = EffectType.ACTIONMODIFIER;
    }

    public Talent(int stringID,Action action,WaffenTyp wt, Map<MentalState,int[]> mentalEffects){
        mAction = action;
        mWeaponType = wt;
        mStringId = stringID;
        mMentalEffects = mentalEffects;
        mEffect = EffectType.ACTIONMODIFIER;
    }

    public Talent(int stringID,Action act, WaffenTyp wt, EffectType effectType){
        this.mAction = act;
        mWeaponType = wt;
        mStringId = stringID;
        mEffect = effectType;
    }

    public Talent(int stringID, Action action, WaffenTyp wt,int[] effects,EffectType effectType){
        mEffects = effects;
        mStringId = stringID;
        mWeaponType = wt;
        mAction = action;
        mEffect = effectType;
    }

    public Talent(int stringID,Action action,WaffenTyp wt, Map<MentalState,int[]> mentalEffects, EffectType effectType){
        mAction = action;
        mWeaponType = wt;
        mStringId = stringID;
        mMentalEffects = mentalEffects;
        mEffect = effectType;
    }

    public Talent(int stringID, Action newAction, Action toReplace, WaffenTyp wt, int fatigueSupplement, int enhancer, int degatsSupplement){
        mFatigueModifier = fatigueSupplement;
        mEnhancerModifier = enhancer;
        mStringId = stringID;
        mDegatsModifier = degatsSupplement;
        mEffect = EffectType.ACTIONREMAKE;
    }

    public Talent(int stringID, Resolver.Value value, int[] effects){
        mEffect = EffectType.VALUE;
        mStringId = stringID;
        mValue = value;
        this.mEffects = effects;
    }

    public Talent(int stringID, Resolver.Value value, Map<MentalState,int[]> mentalEffects){
        mEffect = EffectType.VALUE;
        mValue = value;
        mStringId = stringID;
        mMentalEffects = mentalEffects;
    }

    public static void init(InputStream ip){
        values = new HashMap<>();

        values.put(Action.STEAL.name(), new Talent(R.string.steal, Action.STEAL, WaffenTyp.BAREHANDS));
        values.put(Action.MAKEFIRE.name(), new Talent(R.string.make_fire, Action.MAKEFIRE, WaffenTyp.BAREHANDS));
        values.put(Action.COOK.name(), new Talent(R.string.cook, Action.COOK, WaffenTyp.BAREHANDS));
        values.put(Action.BUTCHER.name(), new Talent(R.string.butcher, Action.BUTCHER, WaffenTyp.BAREHANDS));
        values.put(Action.FISHING.name(), new Talent(R.string.fishing, Action.FISHING, WaffenTyp.BAREHANDS));
        values.put(Action.CLIMB.name(), new Talent(R.string.climb, Action.CLIMB, WaffenTyp.BAREHANDS));
        values.put(Action.SWIM.name(), new Talent(R.string.swim, Action.SWIM, WaffenTyp.BAREHANDS));
        values.put(Action.THROW.name(), new Talent(R.string.th_row, Action.THROW, WaffenTyp.BAREHANDS));
        values.put(Action.MAKEMUSIC.name(), new Talent(R.string.make_music, Action.MAKEMUSIC, WaffenTyp.BAREHANDS));
        values.put(Action.SING.name(), new Talent(R.string.sing, Action.SING, WaffenTyp.BAREHANDS));

        values.put(Resolver.Value.HEALTH.name(), new Talent(R.string.health, Resolver.Value.HEALTH, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
        values.put(Resolver.Value.STAMINA.name(), new Talent(R.string.stamina, Resolver.Value.STAMINA, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));

        HashMap<MentalState,int[]> temp = new HashMap<>();
        temp.put(MentalState.FOCUSED,new int[]{0,0,0,1,1,1,2,2,3,3});
        temp.put(MentalState.IMMERSED,new int[]{0,1,1,2,2,3,4,5,5,5});
        temp.put(MentalState.ECSTASY,new int[]{1,2,3,3,4,5,5,6,6,7});
        values.put(Action.CONJURE.name(), new Talent(R.string.conjure, Action.CONJURE, WaffenTyp.BAREHANDS, temp));

        temp = new HashMap<>();
        temp.put(MentalState.FRENETIC,new int[]{0,0,0,1,1,1,2,2,3,3});
        temp.put(MentalState.AGRESSIVE,new int[]{0,1,1,2,2,3,4,5,5,5});
        temp.put(MentalState.BERSERK,new int[]{1,2,3,3,4,5,5,6,6,7});
        values.put("Two Hand Destroyer", new Talent(R.string.twohand_destroyer, Action.ATTACK, WaffenTyp.BISWORD, temp, EffectType.ACTIONENHANCER));

        temp = new HashMap<>();
        temp.put(MentalState.CONCENTRATED,new int[]{0,0,0,1,1,1,2,2,3,3});
        temp.put(MentalState.METAPHASE,new int[]{0,1,1,2,2,3,4,5,5,5});
        temp.put(MentalState.SYNESTHESIA,new int[]{1,2,3,3,4,5,5,6,6,7});
        values.put("Two sword Dance", new Talent(R.string.twohsword_dance, Action.ATTACK, WaffenTyp.TWOSWORDS, temp, EffectType.ACTIONENHANCER));

        temp = new HashMap<>();
        temp.put(MentalState.CONCENTRATED,new int[]{0,0,0,1,1,1,2,2,3,3});
        temp.put(MentalState.METAPHASE,new int[]{0,1,1,2,2,3,4,5,5,5});
        temp.put(MentalState.SYNESTHESIA,new int[]{1,2,3,3,4,5,5,6,6,7});
        values.put("Pole master", new Talent(R.string.pole_master, Action.ATTACK, WaffenTyp.TWOSWORDS, temp, EffectType.ACTIONENHANCER));

        temp = new HashMap<>();
        temp.put(MentalState.FRENETIC,new int[]{0,0,0,1,1,1,2,2,3,3});
        temp.put(MentalState.AGRESSIVE,new int[]{0,1,1,2,2,3,4,5,5,5});
        temp.put(MentalState.BERSERK,new int[]{1,2,3,3,4,5,5,6,6,7});
        values.put("Bisword Fury", new Talent(R.string.fury, Action.ATTACK, WaffenTyp.BISWORD, temp, EffectType.ATTACKDAMAGE));
    }

    public static boolean hasTalent(String name){ return values.containsKey(name);}

    public static Talent getTalent(String name){
        return values.get(name);
    }

    public static String getName(Talent tl){
        for(Map.Entry<String,Talent> entry:values.entrySet()){
            if(entry.getValue() == tl)
                return entry.getKey();
        }
        return null;
    }

    public int getEffect(MentalState mentalState, int level){
        if(mEffects != null)
            return mEffects[Math.min(level,mEffects.length-1)];
        if(mMentalEffects!= null){
            if(mMentalEffects.containsKey(mentalState)) {
                int[] effects = mMentalEffects.get(mentalState);
                return effects[Math.min(level, effects.length - 1)];
            }else
                return 0;
        }
        return level;
    }

    public EffectType getEffect(){
        return mEffect;
    }

    public Action getAction(){
        return mAction;
    }

    public Action actionToCopy(){ return remadeAction;}

    public WaffenTyp getWeaponType(){
        return mWeaponType;
    }

    public Resolver.Value getValue(){return mValue;}

    public int getStringId(){
        return mStringId;
    }

    public int getFatigueModifier(){
        return mFatigueModifier;
    }

    public int getMax(){
        int max = -1;
        if(mEffects != null)
            max = mEffects.length;
        if(mMentalEffects != null){
            for(int[] ar:mMentalEffects.values())
                max = Math.min(max,ar.length);
        }
        if(max != -1)
           return max;
        return 5;
    }
}
