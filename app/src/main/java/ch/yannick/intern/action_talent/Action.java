package ch.yannick.intern.action_talent;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.yannick.context.RootApplication;
import ch.yannick.display.technical.AdapterUsable;
import ch.yannick.intern.personnage.Attribute;

/**
 * Created by Yannick on 02.03.2015.
 */
public class Action implements AdapterUsable {
    private static Map<String,Action> mValues = new HashMap();
    private static Map<String,List<Action>> mFlagActions = new HashMap<>();

    private static final String LOG = "Action";
    private static  boolean isReady = false;

    private int mStringId, mFatigue;
    private String mName;
    // The standard Attributes, these might be changed for weapons.
    private Attribute mFirst,mSecond;

    // If the action has a Result, like a quantity of production or damage
    private boolean hasResult = false;
    private int resultNameId;

    Action(String name,int stringId, Attribute first, Attribute second, int fatigue){
        this.mStringId = stringId;
        mFirst = first;
        mSecond = second;
        mFatigue = fatigue;
        mName = name;
    }

    public int getStringId(){
        return mStringId;
    }

    public Attribute getFirstAttribute(){
        return mFirst;
    }

    public Attribute getSecondAttribute(){
        return mSecond;
    }

    public int getFatigue(){return mFatigue;}

    public boolean is(String flag){
        return mFlagActions.containsKey(flag) && mFlagActions.get(flag).contains(this);
    }

    public static Action valueOf(String name){
        return mValues.get(name);
    }

    public static boolean isReady(){
        return isReady;
    }

    public String getName() {
        return mName;
    }

    public boolean hasResult(){
        return hasResult;
    }

    public int getResultName(){
        return resultNameId;
    }

    public static void init(InputStream ip,XmlPullParser parser, RootApplication app){
        String flag = null;
        // To add results according to flags at the end of init we need this map which is filled in the Flags entry populated with flags
        Map<String,String> flagResults = new HashMap<>();
        try {
            parser.setInput(ip, null);
            parser.nextTag();
            //parser.require(XmlPullParser.START_TAG, null, "Talents");
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if(parser.getEventType() == XmlPullParser.END_TAG && parser.getName().equals(flag)){
                    flag = null;
                }else if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                Log.d(LOG, "event start " + (parser.getEventType() == XmlPullParser.START_TAG) + " name " + name);
                // Starts by looking for the Action tag
                if (name.equals("Action")) {
                    readEntry(parser,app,flag);
                }else if(name.equals("Flags")){
                    while(parser.next() != XmlPullParser.END_TAG || !parser.getName().equals("Flags")){
                        readFlag(parser,flagResults);
                    }
                }else{ // it is a group which is handled with tags
                    flag = name;
                    if(!mFlagActions.containsKey(flag))
                        mFlagActions.put(flag,new ArrayList<Action>());
                }
            }

            // All actions have been created and now we fille the missing results which are implicit by flags
            for(Map.Entry<String,String> entry: flagResults.entrySet())
                for(Action act:mFlagActions.get(entry.getKey())){
                    act.hasResult = true;
                    act.resultNameId =  app.getStringResource(entry.getValue());
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

    public static void readFlag(XmlPullParser parser, Map<String,String> flagResults){
        String name = null;
        String result = null;
        int attributeCount = parser.getAttributeCount();
        for(int i = 0;i<attributeCount;++i) {
            Log.d(LOG, "attribute " + parser.getAttributeName(i) + " value " + parser.getAttributeValue(i));

            switch (parser.getAttributeName(i)) {
                case "name":
                    name = parser.getAttributeValue(i);
                    break;
                case "result":
                    result = parser.getAttributeValue(i);
            }
        }
        mFlagActions.put(name,new ArrayList<Action>());
        if(result != null)
            flagResults.put(name,result);
    }

    public static void readEntry(XmlPullParser parser, RootApplication application, String flag) throws IOException, XmlPullParserException {
        String name= null;
        Integer stringId = null, fatigue = null;
        Attribute[] attributes = null;
        String[] flags = null;
        int attributeCount = parser.getAttributeCount();
        for(int i = 0;i<attributeCount;++i) {
            Log.d(LOG,"attribute "+parser.getAttributeName(i) + " value "+parser.getAttributeValue(i));

            switch(parser.getAttributeName(i)) {
                case "name":
                    name = parser.getAttributeValue(i);
                    break;
                case "show_name":
                    stringId = application.getStringResource(parser.getAttributeValue(i));
                    Log.d(LOG,"Stringvalue check"+ application.getResources().getString(stringId));
                    break;
                case "attributes":
                    attributes = parseStringAttributeArray(parser.getAttributeValue(i));
                    break;
                case "fatigue":
                    fatigue = Integer.valueOf(parser.getAttributeValue(i));
                    break;
                case "flags":
                    flags = parser.getAttributeValue(i).split("\\s*,\\s*");
                    break;
            }
        }
        if(name == null || stringId == null || attributes == null || fatigue == null)
            Log.w(LOG,"This entry misses attributes");
        else {
            Action action = new Action(name, stringId, attributes[0], attributes[1], fatigue);
            mValues.put(name,action);
            if(flag != null)
                mFlagActions.get(flag).add(action);
            if(flags != null)
                for(String f:flags)
                    mFlagActions.get(f).add(action);
        }

    }

    private static Attribute[] parseStringAttributeArray(String s){
        List<String> items = Arrays.asList(s.split("\\s*,\\s*"));
        Attribute[] res = new Attribute[items.size()];
        for(int i = 0;i<items.size();++i){
            res[i] = Attribute.valueOf(items.get(i));
        }
        return res;
    }
}

