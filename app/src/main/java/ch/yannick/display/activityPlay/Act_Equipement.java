package ch.yannick.display.activityPlay;

import android.os.Bundle;
import android.util.Log;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;

/**
 * Created by Yannick on 19.05.2015.
 */
public class Act_Equipement extends MyBaseActivity {

    private String LOG="Act_Equipement";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_equipement);
    }


    @Override
    public void react(String res, int Flag) {
        Log.d(LOG, "Does nothing onReact");
    }

}