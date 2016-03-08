package ch.yannick.intern.usables;

import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.yannick.context.MyXmlParser;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.state.MentalState;

/**
 * Created by Yannick on 15.01.2016.
 */
public class Role implements  UsableInterface {

    private static final String LOG = "Role";
    private static HashMap<String, Role> values = new HashMap<>();
    protected Map<Action, ActionData> actions = new HashMap<>();
    private List<Action> allowedActions = new ArrayList<>(), standardActions = new ArrayList<>();
    private String mName;

    private int mNameId, mDescriptionId;


    private Role(String name,int stringId, int descriptionId, List<String> actions, List<String> supplementaryActions) {
        mNameId = stringId;
        mName = name;
        mDescriptionId = descriptionId;
        Log.d(LOG, "Creating " + name);
        for (String s : actions){
            Log.d(LOG,"adding action "+s);
            this.actions.put(Action.valueOf(s), new ActionData(Action.valueOf(s)));
            standardActions.add(Action.valueOf(s));
        }
        for(String s:supplementaryActions)
            this.actions.put(Action.valueOf(s), new ActionData(Action.valueOf(s)));

    }

    public static Set<String> getRoleNames(){
        return values.keySet();
    }

    public static Role valueOf(String name){
        return values.get(name);
    }

    // Getter
    @Override
    public UsableType getTyp(){
        return UsableType.ROLE;
    }

    @Override
    public ActionData getData(Action action){
        return new ActionData(actions.get(action));
    }

    @Override
    public List<Action> getActions(){
        return allowedActions;
    }

    @Override
    public boolean canAction(Action action){
        return allowedActions.contains(action);
    }

    @Override
    public void setTalents(Map<Talent, Integer> talents, MentalState mentalState) {
        allowedActions.clear();
        allowedActions.addAll(standardActions);
        for (ActionData data : actions.values())
            data.reset();
        TalentSetter.setTalents(actions,allowedActions, UsableType.ROLE, talents, mentalState);
    }

    @Override
    public String getName(Resources resources) {
        return resources.getString(mNameId);
    }

    public String getDescription(Resources resources) {
        return resources.getString(mDescriptionId);
    }

    public static void parse(MyXmlParser myParser) {
        for(MyXmlParser.Entry entry : myParser.main.entrys){
            if(entry.hasAttribute("hidden_actions"))
                values.put(entry.getAttribute("name"),
                    new Role(entry.getAttribute("name"),entry.getStringResource("show_name"),
                            entry.getStringResource("description"),
                            entry.getList("actions"),
                            entry.getList("hidden_actions")));
            else {
                Log.d(LOG,"adding "+entry.getAttribute("name"));
                Log.d(LOG,(new Role(entry.getAttribute("name"), entry.getStringResource("show_name"),
                        entry.getStringResource("description"),
                        entry.getList("actions"), new ArrayList<String>())).toString());
                values.put(entry.getAttribute("name"),
                        new Role(entry.getAttribute("name"), entry.getStringResource("show_name"),
                                entry.getStringResource("description"),
                                entry.getList("actions"), new ArrayList<String>()));
            }
        }
    }
}