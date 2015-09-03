package ch.yannick.display.activityPlay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;
import ch.yannick.intern.personnage.HitZone;
import ch.yannick.display.views.ValueControler;

public class Dialog_hit extends MyBaseActivity implements OnClickListener {

	public void onCreate(Bundle bn){
		super.onCreate(bn);
		setContentView(R.layout.dialog_hit);

		int[] ids = new int[]{R.id.head,R.id.chest,R.id.arms,R.id.legs};
	 	for(int i:ids)
			((Button) findViewById(i)).setOnClickListener(this);

	}


	@Override
	public void onClick(View v) {
        Intent result = new Intent();
		HitZone hitZone = HitZone.CHEST;
		switch (v.getId()){
			case R.id.arms:
				hitZone = HitZone.ARMS;
				break;
			case R.id.head:
				hitZone = HitZone.HEAD;
				break;
			case R.id.chest:
				hitZone = HitZone.CHEST;
				break;
			case R.id.legs:
				hitZone = HitZone.LEGS;
				break;
		}
        result.putExtra("bodyPart", hitZone.name());
        result.putExtra("hit",((ValueControler)findViewById(R.id.hit_value)).getValue());
        result.putExtra("pierce", ((ValueControler) findViewById(R.id.pierce_value)).getValue());
        result.putExtra("direct",((ToggleButton)findViewById(R.id.direct)).isChecked());
        setResult(Activity.RESULT_OK, result);
        finish();
        super.finish();


	}
	
	@Override
	public void react(String res, int Flag) {
	}
}
