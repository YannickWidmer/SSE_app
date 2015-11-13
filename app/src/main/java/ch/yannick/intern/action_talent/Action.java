package ch.yannick.intern.action_talent;

import java.util.ArrayList;
import java.util.List;

import ch.yannick.context.R;
import ch.yannick.display.technical.AdapterUsable;
import ch.yannick.intern.personnage.Attribute;

/**
 * Created by Yannick on 02.03.2015.
 */
public enum Action implements AdapterUsable {
    ATTACK(R.string.attack,Attribute.AGILLITY,Attribute.FORCE,2),
    SHOOT(R.string.shoot,Attribute.AGILLITY,Attribute.ACUITY,1),
    TWOHANDEDATTACK(R.string.two_hand_attack,true,Attribute.FORCE,Attribute.AGILLITY,4),
    DEFEND(R.string.defend,Attribute.AGILLITY,Attribute.SPEED,2),
    KEEPDISTANCE(R.string.keep_distance,Attribute.ACUITY,Attribute.AGILLITY,2),
    LOAD(R.string.load, Attribute.AGILLITY,Attribute.FORCE,3),
    RUN(R.string.run,Attribute.SPEED,Attribute.AGILLITY,0),
    MANAATTACK(R.string.mana_attack,Attribute.MAGIC,Attribute.AGILLITY,1),
    FILLMANA(R.string.fill_mana,Attribute.MAGIC,Attribute.ASTUTENESS,1),
    USEMANA(R.string.use_mana,Attribute.ASTUTENESS,Attribute.MAGIC,1),

    WEATHERTEST(R.string.cold_sleep,Attribute.PHYSIQUE,Attribute.PHYSIQUE,0),
    ESQUIV(R.string.esquiv,Attribute.AGILLITY,Attribute.ACUITY,0),
    OTHER(R.string.other,Attribute.FORCE,Attribute.FORCE,0),
    // Talents
    STEAL(R.string.steal,Attribute.AGILLITY,Attribute.SPEED,0),
    CONJURE(R.string.conjure,Attribute.MAGIC,Attribute.ASTUTENESS,1),
    MAKEFIRE(R.string.make_fire,Attribute.ASTUTENESS,Attribute.AGILLITY,1),
    COOK(R.string.cook,Attribute.AGILLITY,Attribute.STAMINA,1),
    BUTCHER(R.string.butcher,Attribute.FORCE,Attribute.STAMINA,2),
    FISHING(R.string.fishing,Attribute.AGILLITY,Attribute.STAMINA,0),
    CLIMB(R.string.climb,Attribute.FORCE,Attribute.AGILLITY,2),
    SWIM(R.string.swim,Attribute.STAMINA,Attribute.PHYSIQUE,3),
    THROW(R.string.th_row,Attribute.AGILLITY,Attribute.ACUITY,1),
    MAKEMUSIC(R.string.make_music,Attribute.ASTUTENESS,Attribute.AGILLITY,1),
    SING(R.string.sing,Attribute.CHARM,Attribute.ASTUTENESS,0);
    private int id, mFatigue;
    private boolean onlyTwoHanded;
    // The standard Attributes, these might be changed for weapons.
    private Attribute mFirst,mSecond;
    Action(int id, Attribute first, Attribute second, int fatigue){
        this.id = id;
        onlyTwoHanded = false;
        mFirst = first;
        mSecond = second;
        mFatigue = fatigue;
    }
    Action(int id, boolean onlyTwoHanded, Attribute first, Attribute second, int fatigue){
        this.id = id;
        this.onlyTwoHanded = onlyTwoHanded;
        mFirst = first;
        mSecond = second;
        mFatigue = fatigue;
    }
    public int getStringId(){
        return id;
    }
    public boolean takesBothHands(){
        return onlyTwoHanded;
    }
    public Attribute getFirstAttribute(){
        return mFirst;
    }
    public Attribute getSecondAttribute(){
        return mSecond;
    }
    public int getFatigue(){return mFatigue;}

    public boolean isFightAction(){
        return fightActions.contains(this);
    }
    public boolean isMouvementAction(){
        return mouvementActions.contains(this);
    }
    public boolean isAttack() {return attackActions.contains(this);}

    private static List<Action> attackActions, fightActions, mouvementActions;
    static{
        fightActions = new ArrayList<Action>();
        fightActions.add(ATTACK);
        fightActions.add(SHOOT);
        fightActions.add(TWOHANDEDATTACK);
        fightActions.add(DEFEND);
        fightActions.add(LOAD);
        fightActions.add(MANAATTACK);
        fightActions.add(KEEPDISTANCE);

        attackActions = new ArrayList<Action>();
        attackActions.add(ATTACK);
        attackActions.add(SHOOT);
        attackActions.add(TWOHANDEDATTACK);
        attackActions.add(MANAATTACK);

        mouvementActions = new ArrayList<>();
        mouvementActions.add(RUN);
        mouvementActions.add(ESQUIV);
        mouvementActions.add(CLIMB);
        mouvementActions.add(SWIM);
        mouvementActions.add(THROW);
    }
}

