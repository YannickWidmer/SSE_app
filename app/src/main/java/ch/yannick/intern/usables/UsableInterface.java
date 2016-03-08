package ch.yannick.intern.usables;

import android.content.res.Resources;

import java.util.List;
import java.util.Map;

import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.state.MentalState;

/**
 * Created by Yannick on 23.02.2016.
 */
public interface UsableInterface {
    UsableType getTyp();
    ActionData getData(Action action);
    List<Action> getActions();
    boolean canAction(Action action);
    void setTalents(Map<Talent, Integer> talents, MentalState mentalState);
    String getName(Resources resources);
}
