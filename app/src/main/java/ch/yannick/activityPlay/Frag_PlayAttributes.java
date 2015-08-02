package ch.yannick.activityPlay;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import java.util.List;

import ch.yannick.context.R;
import ch.yannick.enums.Attribute;
import ch.yannick.intern.State;

public class Frag_PlayAttributes extends Fragment implements OnClickListener {
	private State st;
	private static final String LOG = "frag:Attributes";
    private Frag_Displayer mDisplayer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(LOG, "onCreateView");
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.frag_play_attributes, container, false);
	}

	public void setState(State state) {
		st = state;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(LOG, "onActivityCreated");

		View v = getView();

        mDisplayer = (Frag_Displayer) getActivity().getFragmentManager().findFragmentById(R.id.display);
		ToggleButton b;
		for (Attribute attr : Attribute.values()) {
            b = (ToggleButton) v.findViewById(attr.getId());
            b.setOnClickListener(this);
            b.setText(b.getText() + ":" + st.getSkill(attr));
            b.setTextOn(b.getText());
            b.setTextOff(b.getText());
		}

        if(!st.isMagic())
            v.findViewById(Attribute.MAGIC.getId()).setVisibility(View.GONE);

	}

	@Override
	public void onClick(View v) {
		compute();
        mDisplayer.setModif(0);
        mDisplayer.setAlter(0);
		mDisplayer.refresh();
	}

    public void setSelection(List<Attribute> attributes) {

        View frag = getView();
        for (Attribute attribute : Attribute.values()) {
            ((ToggleButton) frag.findViewById(attribute.getId())).setChecked(attributes.contains(attribute));
        }
        compute();
    }

	private void compute(){
		int num = 0, sum = 0;
		View frag = getView();
		for (Attribute attribute: Attribute.values()) {
			if (((ToggleButton) frag.findViewById(attribute.getId())).isChecked()) {
				sum += st.getSkill(attribute);
				++num;
			}
		}

		if (num == 0)
			num = 1;
		mDisplayer.setSkill(2 * sum / num);
	}

	protected void react() {
		// TODO
	}
}
