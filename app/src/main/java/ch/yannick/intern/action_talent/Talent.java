package ch.yannick.intern.action_talent;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.yannick.context.RootApplication;
import ch.yannick.intern.usables.UsableTyp;
import ch.yannick.intern.state.MentalState;
import ch.yannick.intern.state.Resolver;

/**
 * Created by Yannick on 18.07.2015.
 */
public class Talent {
    private static final String LOG = "Talent";
    private static  boolean isReady = false;
    public static HashMap<String,Talent> values;

    private EffectType mEffect;
    private int mStringId, mDescriptionId;
    private Action mAction, remadeAction;
    private UsableTyp mUsableType;
    private Resolver.Value mValue;

    // The effects value, depending if it depends on the Mental state or not
    private int[] mEffects;
    private Map<MentalState,int[]> mMentalEffects;

    // If the Talent changes the usual action
    private int mFatigueModifier;


    private Talent(int stringID,int descriptionId, Action act, UsableTyp wt, EffectType effectType){
        this.mAction = act;
        mUsableType = wt;
        mStringId = stringID;
        mEffect = effectType;
    }

    private Talent(int stringID,int descriptionId, Action newAction, Action oldAction, UsableTyp wt, int fatigueSupplement){
        mFatigueModifier = fatigueSupplement;
        mStringId = stringID;
        mEffect = EffectType.ACTIONREMAKE;
    }


    private Talent(int stringID,int descriptionId, Resolver.Value value){
        mEffect = EffectType.VALUE;
        mStringId = stringID;
        mValue = value;
    }

    private void setEffet(int[] effects){
        mEffects = effects;
    }

    private void setEffet(Map<MentalState,int[]> mentalEffects){
        mMentalEffects = mentalEffects;
    }


    public static boolean isReady(){
        return isReady;
    }

    public static void init(InputStream ip,XmlPullParser parser, RootApplication app){

        values = new HashMap<>();

        try {
            parser.setInput(ip, null);
            parser.nextTag();
            //parser.require(XmlPullParser.START_TAG, null, "Talents");
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                Log.d(LOG,"event start "+(parser.getEventType()== XmlPullParser.START_TAG) + " name "+name);
                // Starts by looking for the Talent tag
                if (name.equals("Talent")) {
                    readEntry(parser,app);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ip.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        isReady = true;
    }

    public static void readEntry(XmlPullParser parser, RootApplication application) throws IOException, XmlPullParserException {
        String name= null;
        UsableTyp weaponType = null;
        Resolver.Value value = null;
        EffectType effectType = null;
        Action action = null, newAction = null;
        Integer stringId = null, descriptionId = null, fatigueModifier = null;
        Talent talent = null;

        // Reading attributes of the Talent entry
        int attributeCount = parser.getAttributeCount();
        for(int i = 0;i<attributeCount;++i) {
            Log.d(LOG,"attribute "+parser.getAttributeName(i) + " value "+parser.getAttributeValue(i));

            switch(parser.getAttributeName(i)) {
                case "name":
                    name = parser.getAttributeValue(i);
                    break;
                case "description":
                    descriptionId = application.getStringResource(parser.getAttributeValue(i));
                    Log.d(LOG,"Stringvalue check"+ application.getResources().getString(descriptionId));
                    break;
                case "show_name":
                    stringId = application.getStringResource(parser.getAttributeValue(i));
                    Log.d(LOG,"Stringvalue check"+ application.getResources().getString(stringId));
                    break;
                case "effect_type":
                    effectType = EffectType.valueOf(parser.getAttributeValue(i));
                    break;
                case "action":
                    action = Action.valueOf(parser.getAttributeValue(i));
                    break;
                case "new_action":
                    newAction = Action.valueOf(parser.getAttributeValue(i));
                    break;
                case "weapon_type":
                    weaponType = UsableTyp.valueOf(parser.getAttributeValue(i));
                    break;
                case "resolver_value":
                    value = Resolver.Value.valueOf(parser.getAttributeValue(i));
                    break;
                case "fatigue_modifier":
                    fatigueModifier = Integer.valueOf(parser.getAttributeValue(i));
            }
        }

        // filling information which is implicit
        if(action == null && effectType.isAction()) {
            try {
                action = Action.valueOf(name);
            }catch(Exception e){}
        }

        if(weaponType == null)
            weaponType = UsableTyp.ROLE;

        switch (effectType){
            case DESCRIPTIVE:
                break;
            case RESULTMODIFIER:
            case LUCKMODIFIER:
            case SKILLMODIFIER:
                talent = new Talent(stringId, descriptionId,action,weaponType,effectType);
                break;
            case ACTIONREMAKE:
                talent = new Talent(stringId,descriptionId, newAction, action, weaponType, fatigueModifier);
                break;
            case VALUE:
                talent = new Talent(stringId,descriptionId,value);
                break;
        }

        if(!parser.isEmptyElementTag() && parser.next() == XmlPullParser.START_TAG){
            switch (parser.getName()){
                case "Effect":
                    int numOfAttributes = parser.getAttributeCount();
                    for(int i = 0;i<numOfAttributes;++i){
                        if(parser.getAttributeName(i).equals("VALUES")){
                            talent.setEffet(parseStringArray(parser.getAttributeValue(i)));
                        }
                    }
                    break;
                case "MentalEffect":
                    int num_of_attributes = parser.getAttributeCount();
                    HashMap<MentalState,int[]> temp = new HashMap<>();
                    for(int i = 0;i<num_of_attributes;++i){
                        temp.put(MentalState.valueOf(parser.getAttributeName(i)),parseStringArray(parser.getAttributeValue(i)));
                    }
                    talent.setEffet(temp);
                    break;
            }
        }

        values.put(name,talent);
    }

    private static int[] parseStringArray(String s){
        List<String> items = Arrays.asList(s.split("\\s*,\\s*"));
        int [] res = new int[items.size()];
        for(int i = 0;i<items.size();++i){
            res[i] = Integer.valueOf(items.get(i));
        }
        return res;
    }

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

    public UsableTyp getUsableType(){
        return mUsableType;
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
