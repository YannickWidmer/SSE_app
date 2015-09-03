package ch.yannick.intern.action_talent;

/**
 * Created by Yannick on 18.07.2015.
 * This enum describes the differernt kinds of Talents,
 */
public enum EffectType {
    /*
    *Talents with this effect change the luck of the corresponding Action
     */
    ACTIONMODIFIER(true),
    /*
    * Talents with this effect change the Skill of the corresponding Action
      */
    ACTIONENHANCER(true),
    /*
    * Talents with this effect change the damage dealed of the corresponding Attack Action, not the dices only the constant
    */
    ATTACKDAMAGE(true),
    /*
    *Talents with this effect Add an action to a weapon and change its fatigue and its skill, there might be a Talent of the
    * type ACTIONMODIFIER combined with this one, if this is the case it is important that the REMAKE has to be learned first.
    */
    ACTIONREMAKE(true),
    /*
    *Talents with this effect change values (Resolver.Value) computed in Resolver
     */
    VALUE(false);

    // this means if it has something to do with actions and hence has to be treated in Weapon.setTalents;
    private boolean isAction;
    EffectType(boolean action) {
        isAction = action;
    }

    public boolean isAction(){return isAction;}

}
