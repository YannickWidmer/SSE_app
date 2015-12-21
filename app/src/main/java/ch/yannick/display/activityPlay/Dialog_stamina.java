package ch.yannick.display.activityPlay;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;
import ch.yannick.display.views.ValueControler;
import ch.yannick.intern.state.Resolver;
import ch.yannick.intern.state.State;

public class Dialog_stamina extends MyBaseActivity {
	private State st;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		st = mRootApplication.getState(getIntent().getLongExtra("id",0));
		setContentView(R.layout.dialog_stamina);


        final ValueControler now = ((ValueControler)findViewById(R.id.stamina_now));
        now.setValue(st.getStaminaNow());
        final ValueControler used =((ValueControler)findViewById(R.id.stamina_used));
        used.setValue(st.getStaminaUsed());
        final ValueControler max =((ValueControler)findViewById(R.id.stamina_max));
        max.setValue(st.getStaminaMax());


        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                st.setStamina(now.getValue(),used.getValue(),max.getValue());
                Dialog_stamina.this.finish();
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Dialog_stamina.this.finish();
            }
        });

        findViewById(R.id.now_empty).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                now.setValue(0);
            }
        });

        findViewById(R.id.now_fill).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                now.setValue(max.getValue());
            }
        });

        findViewById(R.id.used_empty).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                used.setValue(0);
            }
        });

        findViewById(R.id.max_compute).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                max.setValue(Resolver.getValue(st,Resolver.Value.STAMINA));
            }
        });
	}
	



	@Override
	public void react(String res, int Flag) {
	}

}
