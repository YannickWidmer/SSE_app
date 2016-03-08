package ch.yannick.intern.action_talent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.yannick.context.MyXmlParser;
import ch.yannick.intern.state.MentalState;
import ch.yannick.intern.state.Resolver;
import ch.yannick.intern.usables.UsableType;

/**
 * Created by Yannick on 18.07.2015.
 */
public class Talent {
    private static final String LOG = "Talent";
    public static HashMap<String,Talent> values = new HashMap<>();

    private EffectType mEffect;
    private String mName;
    private int mStringId, mDescriptionId;
    private Action mAction;
    private UsableType mUsableType;
    private Resolver.Value mValue;

    // The effects value, depending if it depends on the Mental state or not
    private List<Integer> mEffects;
    private Map<MentalState,List<Integer>> mMentalEffects;


    private Talent(String name,int stringID,int descriptionId, Action act, UsableType wt, EffectType effectType){
        mName = name;
        this.mAction = act;
        mUsableType = wt;
        mStringId = stringID;
        mEffect = effectType;
        mDescriptionId = descriptionId;
    }

    private Talent(String name, int stringID,int descriptionId, Resolver.Value value){
        mName = name;
        mEffect = EffectType.VALUE;
        mStringId = stringID;
        mValue = value;
        mDescriptionId = descriptionId;
    }

    private void setEffet(List<Integer> effects){
        mEffects = effects;
    }

    private void setEffet(Map<MentalState,List<Integer>> mentalEffects){
        mMentalEffects = mentalEffects;
    }

    public static Talent getTalent(String name){
        return values.get(name);
    }

    public  String getName(){
        return mName;
    }

    public int getEffect(MentalState mentalState, int level){
        if(mEffects != null)
            return mEffects.get(Math.min(level, mEffects.size() - 1));
        if(mMentalEffects!= null){
            if(mMentalEffects.containsKey(mentalState)) {
                List<Integer> effects = mMentalEffects.get(mentalState);
                return effects.get(Math.min(level, effects.size() - 1));
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

    public UsableType getUsableType(){
        return mUsableType;
    }

    public Resolver.Value getValue(){return mValue;}

    public int getStringId(){
        return mStringId;
    }

    public int getMax(){
        int max = -1;
        if(mEffects != null)
            max = mEffects.size();
        if(mMentalEffects != null){
            for(List<Integer> ar:mMentalEffects.values())
                max = Math.min(max,ar.size());
        }
        if(max != -1)
           return max;
        return 5;
    }

    public static void parse(MyXmlParser myParser) {
        Action action;
        EffectType effectType;
        UsableType usableType;
        Talent talent;

        for(MyXmlParser.Entry entry:myParser.main.entrys){
            if(entry.hasAttribute("usable_type"))
                usableType = UsableType.valueOf(entry.getAttribute("usable_type"));
            else
                usableType = UsableType.ROLE;
            effectType = EffectType.valueOf(entry.getAttribute("effect_type"));
            switch (effectType) {
                case DESCRIPTIVE:
                    break;
                case RESULTMODIFIER:
                case LUCKMODIFIER:
                case SKILLMODIFIER:
                case ALLOWACTION:
                    if(entry.hasEntry("action"))
                        action = Action.valueOf(entry.getAttribute("action"));
                    else
                        action = Action.valueOf(entry.getAttribute("name"));
                    talent = new Talent(entry.getAttribute("name"),entry.getStringResource("show_name"), entry.getStringResource("description"),
                            action, usableType, effectType);
                    setValues(entry,talent);
                    values.put(entry.getAttribute("name"), talent);
                    break;
                case VALUE:
                    talent = new Talent(entry.getAttribute("name"),entry.getStringResource("show_name"),entry.getStringResource("description"),
                            Resolver.Value.valueOf(entry.getAttribute("resolver_value")));
                    setValues(entry,talent);
                    values.put(entry.getAttribute("name"), talent);
                    break;
            }
        }
    }

    private static void setValues(MyXmlParser.Entry entry, Talent talent){
        if(entry.hasEntry("Effect")) {
            talent.setEffet(entry.getEntryWithName("Effect").getIntegerList("values"));
        }
        if(entry.hasEntry("MentalEffect")){
            HashMap<MentalState,List<Integer>> temp = new HashMap<>();
            MyXmlParser.Entry valueEntry = entry.getEntryWithName("MentalEffect");
            for(String s:valueEntry.getAttributes())
                temp.put(MentalState.valueOf(s),valueEntry.getIntegerList(s));
            talent.setEffet(temp);
        }
    }
}
