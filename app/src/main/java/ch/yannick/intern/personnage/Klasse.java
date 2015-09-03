package ch.yannick.intern.personnage;

import ch.yannick.intern.action_talent.Action;

/**
 * Created by Yannick on 17.04.2015.
 */
public enum Klasse {
    MAGE();

    private int id;
    private Action[] actions;
    public int getStringId(){
        return id;
    }

    public Action[] getActions(){
        return actions;
    }
}
