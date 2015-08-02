package ch.yannick.activityPlay;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;
import ch.yannick.intern.State;
import ch.yannick.views.ValueControler;

public class Dialog_health extends MyBaseActivity{
	private State st;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		st = mRootApplication.getState(getIntent().getLongExtra("id",0));
		
		setContentView(R.layout.dialog_health);
		final ValueControler now = (ValueControler) findViewById(R.id.health);
        final ValueControler max = (ValueControler) findViewById(R.id.max);

        now.setValue(st.getHealth());
        max.setValue(st.getHealthMax());


        findViewById(R.id.now_fill).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                now.setValue(max.getValue());
            }
        });

        findViewById(R.id.max_compute).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                max.setValue(st.getHealthMaxStd());
            }
        });

        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                st.setHealth(now.getValue(),max.getValue());
                Dialog_health.this.finish();
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Dialog_health.this.finish();
            }
        });
	}
	
		@Override
	public void react(String res, int Flag) {
	}

}
