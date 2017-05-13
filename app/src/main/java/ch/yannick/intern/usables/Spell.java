package ch.yannick.intern.usables;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.state.MentalState;

/**
 * Created by Yannick on 24.02.2016.
 */
public class Spell  implements UsableInterface {
    private static final String LOG = "Spell";
    protected List<Action> actions = new ArrayList<>();
    private int mana_need, mana_max, mana_used, difficulty,level;
    private int mNameId, mDescriptionId;
    private String mName;


    public Spell(String  name,int nameId, int descriptionId, int mana_need, int mana_max, int mana_used, int difficulty,int level){
        this.mana_max = mana_max;
        this.mana_need = mana_need;
        this.mana_used = mana_used;
        this.difficulty = difficulty;
        this.level = level;
        mName = name;
        mNameId = nameId;
        mDescriptionId = descriptionId;
    }


    public int getManaNeed() {
        return mana_need;
    }

    public int getManaMax(){
        return mana_max;
    }

    public int getManaUsed(){
        return mana_used;
    }

    public int getDifficulty(){
        return difficulty;
    }

    public int getLevel(){
        return  level;
    }

    @Override
    public UsableType getTyp() {
        return UsableType.ROLE;
    }

    @Override
    public ActionData getData(Action action) {
        return new ActionData(action);
    }

    @Override
    public List<Action> getActions() {
        return actions;
    }

    @Override
    public boolean canAction(Action action) {
        return actions.contains(action);
    }

    @Override
    public void setTalents(Map<Talent, Integer> talents, MentalState mentalState) {
        // Does nothing but this might change in the future.
    }

    @Override
    public String getName(Resources resources) {
        return resources.getString(mNameId);
    }
}
