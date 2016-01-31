package ch.yannick.intern.usables;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

import ch.yannick.context.RootApplication;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;

/**
 * Created by Yannick on 15.01.2016.
 */
public class Role extends Usable {

    private static final String LOG = "Role";
    private static boolean isReady = false;
    public static HashMap<String, Role> values;

    private int mNameId, mDescriptionId;


    private Role(String name,int stringId, int descriptionId, String[] actions) {
        super(name, UsableTyp.ROLE);
        mNameId = stringId;
        mDescriptionId = descriptionId;
        values.put(name,this);
        for (String s : actions)
            base_actions.put(Action.valueOf(s), new ActionData(Action.valueOf(s)));
    }

    public static Set<String> getRoleNames(){
        return values.keySet();
    }

    public static Role valueOf(String name){
        return values.get(name);
    }

    public static boolean isReady() {
        return isReady;
    }

    public static void init(InputStream ip, XmlPullParser parser, RootApplication app) {

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
                Log.d(LOG, "event start " + (parser.getEventType() == XmlPullParser.START_TAG) + " name " + name);
                // Starts by looking for the Talent tag
                if (name.equals("Role")) {
                    readEntry(parser, app);
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
        String name = null;
        Integer stringId = null, descriptionId = null;
        String[] actions = null;

        int attributeCount = parser.getAttributeCount();
        for (int i = 0; i < attributeCount; ++i) {
            Log.d(LOG, "attribute " + parser.getAttributeName(i) + " value " + parser.getAttributeValue(i));

            switch (parser.getAttributeName(i)) {
                case "name":
                    name = parser.getAttributeValue(i);
                    break;
                case "description":
                    descriptionId = application.getStringResource(parser.getAttributeValue(i));
                    Log.d(LOG,"Stringvalue check" + application.getResources().getString(descriptionId));
                    break;
                case "show_name":
                    stringId = application.getStringResource(parser.getAttributeValue(i));
                    Log.d(LOG,"Stringvalue check" + application.getResources().getString(stringId));
                    break;
                case "actions":
                    actions = parser.getAttributeValue(i).split("\\s*,\\s*");
                    break;
            }
        }

        values.put(name, new Role(name,stringId,descriptionId,actions));
    }
}