package ch.yannick.intern.usables;

import java.util.HashMap;
import java.util.Map;

import ch.yannick.context.R;
import ch.yannick.display.technical.AdapterUsable;

/**
 * Created by Yannick on 02.03.2015.
 */

// This list must agree with the llist weapon_types in strings
public class UsableTyp implements AdapterUsable {
    public static final UsableTyp ROLE = new UsableTyp("ROLE",R.string.bare_hands);


    private int stringId;
    private String name;
    private static Map<String,UsableTyp> values = new HashMap<>();

    protected UsableTyp(String name, int stringId){
        this.stringId = stringId;
        this.name = name;
        values.put(name,this);
    }

    public static UsableTyp valueOf(String name){
        return values.get(name);
    }

    public String getName(){
        return name;
    }

    public int getStringId(){
        return stringId;
    }
}
