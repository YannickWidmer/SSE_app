package ch.yannick.intern;

import java.util.HashMap;
import java.util.Map;

import ch.yannick.enums.Action;
import ch.yannick.enums.Attribute;
import ch.yannick.enums.Effect;
import ch.yannick.enums.WaffenTyp;

/**
 * Created by Yannick on 17.04.2015.
 */
public class FreeAction extends Weapon{

    private State state;
    public FreeAction(State state){
        super(0l,"",WaffenTyp.SWORD, false,0,0);
        this.state = state;

        this.actions = new HashMap<Action,ActionData>();

        actions.put(Action.ESQUIV,new ActionData());
        actions.get(Action.ESQUIV).firstAttribute = Attribute.ACUITY;
        actions.get(Action.ESQUIV).secondAttribute = Attribute.AGILLITY;

        actions.put(Action.RUN,new ActionData());
        actions.put(Action.RUN,new ActionData());
        actions.get(Action.RUN).firstAttribute = Attribute.SPEED;
        actions.get(Action.RUN).secondAttribute = Attribute.AGILLITY;

        actions.put(Action.OTHER,new ActionData());
    }

    private void init(){
        actions.put(Action.ESQUIV,new ActionData());
        actions.get(Action.ESQUIV).firstAttribute = Attribute.ACUITY;
        actions.get(Action.ESQUIV).secondAttribute = Attribute.AGILLITY;

        actions.put(Action.RUN,new ActionData());
        actions.put(Action.RUN,new ActionData());
        actions.get(Action.RUN).firstAttribute = Attribute.SPEED;
        actions.get(Action.RUN).secondAttribute = Attribute.AGILLITY;

        actions.put(Action.OTHER, new ActionData());
    }

    public void setTalents(Map<Talent, Integer> talents) {
        actions.clear();
        init();
        for(Talent tl: talents.keySet()){
            if(tl.getEffect() == Effect.ACTION){
                actions.put(tl.getAction(),new ActionData());
                actions.get(tl.getAction()).firstAttribute = tl.getFirstAttribute();
                actions.get(tl.getAction()).secondAttribute= tl.getSecontAttribute();
                actions.get(tl.getAction()).fatigue = tl.getFatigue();
            }
        }
    }

    public int getEnhancer(Action test){
        return 0;
    }

    public int getFatigue(Action action){
        return 0;
    }
}
