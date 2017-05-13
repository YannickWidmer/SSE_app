package ch.yannick.intern.action_talent;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.yannick.context.MyXmlParser;
import ch.yannick.display.technical.AdapterUsable;
import ch.yannick.intern.personnage.Attribute;

/**
 * Created by Yannick on 02.03.2015.
 */
public class Action implements AdapterUsable {
    private static Map<String,Action> mValues = new HashMap();
    private static Map<String,List<Action>> mFlagActions = new HashMap<>();

    private static final String LOG = "Action";

    private int mStringId, mFatigue;
    private String mName;
    // The standard Attributes, these might be changed for weapons.
    private List<Attribute> mAttributes = new ArrayList<>();

    // If the action has a Result, like a quantity of production or damage
    private boolean hasResult = false;
    private int resultNameId;

    Action(String name,int stringId, Attribute first, Attribute second, int fatigue){
        this.mStringId = stringId;
        mAttributes.add(first);
        mAttributes.add(second);
        mFatigue = fatigue;
        mName = name;
    }

    public int getStringId(){
        return mStringId;
    }

    public List<Attribute> getAttributes(){
        return new ArrayList<>(mAttributes);
    }

    public int getFatigue(){return mFatigue;}

    public boolean is(String flag){
        return mFlagActions.containsKey(flag) && mFlagActions.get(flag).contains(this);
    }

    public static Action valueOf(String name){
        if(!mValues.containsKey(name)){
            Log.e(LOG,"action value inexistant "+name );
        }

        return mValues.get(name);
    }

    public String getName() {
        return mName;
    }

    public boolean hasResult(){
        return hasResult;
    }

    public int getResultNameId(){
        return resultNameId;
    }


    // XML PASING
    private static void readFlag(MyXmlParser.Entry entry, Map<String, Integer> flagResults){
        String name = entry.getAttribute("name");
        mFlagActions.put(name,new ArrayList<Action>());
        if(entry.hasAttribute("result"))
            flagResults.put(name,entry.getStringResource("result"));
    }

    private static void readAction(MyXmlParser.Entry entry, String flag){
        Log.d(LOG,"reading action "+entry.getAttribute("name"));
        Attribute[] attributes = parseStringAttributeArray(entry.getList("attributes"));
        Action action = new Action(entry.getAttribute("name"), entry.getStringResource("show_name"),
                attributes[0], attributes[1], entry.hasAttribute("fatigue")?entry.getInt("fatigue"):0);
        mValues.put(entry.getAttribute("name"),action);
        if(flag != null)
            mFlagActions.get(flag).add(action);
        if(entry.hasAttribute("flags"))
            for(String f:entry.getList("flags"))
                mFlagActions.get(f).add(action);
    }


    private static Attribute[] parseStringAttributeArray(List<String> items){
        Attribute[] res = new Attribute[items.size()];
        for(int i = 0;i<items.size();++i){
            res[i] = Attribute.valueOf(items.get(i));
        }
        for(Attribute attribute:res)
            Log.d(LOG,"  -"+attribute);

        return res;
    }

    public static void parse(MyXmlParser myParser) {
        String flag = null;
        // To add results according to flags at the end of init we need this map which is filled in the Flags entry populated with flags
        Map<String,Integer> flagResults = new HashMap<>();
        if(myParser.main.hasEntry("Flags"))
           for(MyXmlParser.Entry entry: myParser.main.getEntryWithName("Flags").entrys)
                readFlag(entry,flagResults);

        for(MyXmlParser.Entry entry: myParser.main.entrys) {
            if (entry.name.equals("Flags"))
                continue;
            if (entry.name.equals("Action"))
                readAction(entry,null);
            else {
                flag = entry.name;
                if (!mFlagActions.containsKey(flag))
                    mFlagActions.put(flag, new ArrayList<Action>());
                for (MyXmlParser.Entry innerEntry : entry.entrys)
                        readAction(innerEntry, flag);
            }
        }

        // All actions have been created and now we fill the missing results which are implicit by flags
        for(Map.Entry<String,Integer> entry: flagResults.entrySet())
            for(Action act:mFlagActions.get(entry.getKey())){
                act.hasResult = true;
                act.resultNameId =  (entry.getValue());
            }
    }

    public static Set<String> values() {
        return mValues.keySet();
    }

}

