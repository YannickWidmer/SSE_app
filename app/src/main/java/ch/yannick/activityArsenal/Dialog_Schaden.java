package ch.yannick.activityArsenal;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.enums.Dice;
import ch.yannick.intern.Weapon;
import ch.yannick.views.DiceDisplayer;
import ch.yannick.views.ValueControler;

/**
 * Created by Yannick on 14.03.2015.
 */
public class Dialog_Schaden extends MyBaseActivity {

    private static String LOG = "Dialog Schaden";
    private ToggleButton direct;
    private ValueControler schaden, penetration;
    private DiceDisplayer diceDisplayer;
    private final List<Dice> dices = new ArrayList<Dice>();
    private Weapon w;
    @Override
    public void react(String res, int Flag) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_schaden);

        try{
            w = ((RootApplication) getApplication()).currentWeapon;
        }catch(Exception e){ e.printStackTrace();}

        dices.clear();
        for(Dice d:w.schadenW())
            dices.add(d);
        schaden = (ValueControler) findViewById(R.id.schaden);
        penetration = (ValueControler) findViewById(R.id.penetration);
        direct = (ToggleButton)findViewById(R.id.direct);
        direct.setChecked(w.isDirect());
        diceDisplayer = (DiceDisplayer) findViewById(R.id.degats_dice);

        schaden.setValue(w.getSchaden());
        penetration.setValue(w.getPenetration());
        diceDisplayer.setDices(dices);

        diceDisplayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(LOG,"diceDisplayer.onTouch");
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (dices.size() > 0) {
                        dices.remove(dices.size() - 1);
                        diceDisplayer.setDices(dices);
                        diceDisplayer.invalidate();
                        diceDisplayer.requestLayout();
                    }
                    return true;
                }
                return false;
            }
        });


        int textSize = getResources().getDimensionPixelOffset(R.dimen.big_text);

        Log.d(LOG, "text Size "+ textSize);

        LinearLayout dicesLayout = (LinearLayout) findViewById(R.id.dices);
        for(final Dice d: Dice.values()){
            DiceDisplayer diceImage = new DiceDisplayer(this);

            diceImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dices.add(d);
                    diceDisplayer.setDices(dices);
                    diceDisplayer.requestLayout();
                    diceDisplayer.getParent().requestLayout();
                    diceDisplayer.getParent().getParent().requestLayout();
                }
            });
            diceImage.setTextSize(textSize);
            diceImage.setDice(d);
            dicesLayout.addView(diceImage);
        }
        schaden.invalidate();
        penetration.invalidate();
        diceDisplayer.invalidate();
        dicesLayout.invalidate();
        dicesLayout.requestLayout();
    }

    public void confirmed(View v){
        w.setDegats(schaden.getValue());
        w.setPenetration(penetration.getValue());
        w.setIsDirect(direct.isChecked());
        w.setDices(diceDisplayer.getDices());
        super.finish();
    }

    public void cancel(View v){
        super.finish();
    }
}
