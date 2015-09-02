package ch.yannick.enums;

import java.util.ArrayList;
import java.util.List;

import ch.yannick.context.R;
import ch.yannick.technical.AdapterUsable;

/**
 * Created by Yannick on 02.03.2015.
 */
public enum Action implements AdapterUsable {
    ATTACK(R.string.attack),TWOHANDEDATTACK(R.string.two_hand_attack,true), DEFEND(R.string.defend), LOAD(R.string.load), ESQUIV(R.string.esquiv), RUN(R.string.run),  OTHER(R.string.other),
    // Talents
    STEAL(R.string.steal),CONJURE(R.string.conjure),MAKEFIRE(R.string.make_fire),COOK(R.string.cook),BUTCHER(R.string.butcher),FISHING(R.string.fishing),
    CLIMB(R.string.climb),SWIM(R.string.swim),THROW(R.string.th_row),MAKEMUSIC(R.string.make_music),SING(R.string.sing);
    private int id;
    private boolean onlyTwoHanded;
    private Action(int id){
        this.id = id;
        onlyTwoHanded = false;
    }
    private Action(int id, boolean onlyTwoHanded){
        this.id = id;
        this.onlyTwoHanded = onlyTwoHanded;
    }
    public int getStringId(){
        return id;
    }
    public boolean takesBothHands(){
        return onlyTwoHanded;
    }
    public boolean isFightAction(){
        return fightActions.contains(this);
    }

    public boolean isMouvementAction(){
        return mouvementActions.contains(this);
    }
    private static List<Action> fightActions;
    private static List<Action> mouvementActions;
    static{
        fightActions = new ArrayList<Action>();
        fightActions.add(ATTACK);
        fightActions.add(TWOHANDEDATTACK);
        fightActions.add(DEFEND);

        mouvementActions = new ArrayList<>();
        mouvementActions.add(RUN);
        mouvementActions.add(ESQUIV);
        mouvementActions.add(CLIMB);
        mouvementActions.add(SWIM);
    }
}

