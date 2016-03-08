package ch.yannick.context;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yannick on 27.02.2016.
 */
public class MyXmlParser {
    private static String LOG = "XML Parser";
    public Entry main;
    public RootApplication application;

    public MyXmlParser(InputStream ip, XmlPullParser parser, RootApplication app) {
        application = app;
        try {
            parser.setInput(ip, null);
            parser.next();
            if (parser.getEventType() == XmlPullParser.START_TAG)
                main = new Entry(parser);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ip.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Entry {
        public String name, text;
        private Map<String, String> attributes = new HashMap<>();
        public List<Entry> entrys = new ArrayList<>();

        public Entry getEntryWithName(String name){
            for(Entry e:entrys)
                if(e.name.equals(name))
                    return e;
            return null;
        }

        public boolean hasEntry(String name){
            return getEntryWithName(name)!= null;
        }

        public boolean hasAttribute(String name){
            return attributes.containsKey(name);
        }

        public String getAttribute(String name){
            return attributes.get(name);
        }

        public List<String> getAttributes(){
            return new ArrayList<>(attributes.keySet());
        }

        public List<String> getList(String name){
            return Arrays.asList(attributes.get(name).split("\\s*,\\s*"));
        }

        public List<Integer> getIntegerList(String name){
            ArrayList<Integer> res = new ArrayList<>();
            for(String s:Arrays.asList(attributes.get(name).split("\\s*,\\s*")))
                res.add(Integer.valueOf(s));
            return res;
        }

        public int getInt(String name){
            return Integer.valueOf(attributes.get(name));
        }

        public int getStringResource(String name){
            return application.getStringResource(attributes.get(name));
        }

        public Entry(XmlPullParser parser) {
            try {
                name = parser.getName();

                for (int i = 0; i < parser.getAttributeCount(); ++i)
                    attributes.put(parser.getAttributeName(i), parser.getAttributeValue(i));


                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() == XmlPullParser.START_TAG)
                        entrys.add(new Entry(parser));

                    if (parser.getEventType() == XmlPullParser.TEXT) {
                        text = parser.getText();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
