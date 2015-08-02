package ch.yannick.enums;

import ch.yannick.context.R;

/**
 * Created by Yannick on 18.07.2015.
 */
public enum Effect {
    ACTION(R.string.act), STAMINAMAX(R.string.endurance), HEALTHMAX(R.string.health), COMBAT(R.string.combat), FURY(R.string.fury);

    private long id;

    private Effect(int id) {
        this.id = id;
    }

    public long getStringId() {
        return id;
    }
}
