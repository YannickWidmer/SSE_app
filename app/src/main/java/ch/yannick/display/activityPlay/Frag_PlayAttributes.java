package ch.yannick.display.activityPlay;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.intern.personnage.Attribute;
import ch.yannick.intern.state.State;

public class Frag_PlayAttributes extends Fragment implements OnClickListener {
	private State st;
	private static final String LOG = "frag:Attributes";
    private Frag_Displayer mDisplayer;
	private List<Holder> holders = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(LOG, "onCreateView");
		setHasOptionsMenu(true);
		st = ((RootApplication)getActivity().getApplication()).getCurrentState();
		View v = inflater.inflate(R.layout.frag_play_attributes, container, false);
		LinearLayout list = (LinearLayout) v.findViewById(R.id.list);
		Holder holder;
		for (Attribute attr : st.getAttributes()) {
			holder = new Holder();
			holders.add(holder);
            holder.attribute = attr;
            holder.tl = (ToggleButton) inflater.inflate(R.layout.list_item_attribute, null, false);
            list.addView(holder.tl);
			Log.d(LOG," ToggleButton for " + attr.name());
			holder.tl.setOnClickListener(this);
			holder.tl.setText(attr.getStringId());
			holder.tl.setText(holder.tl.getText() + ":" + st.getAttributeValue(attr));
			holder.tl.setTextOn(holder.tl.getText());
			holder.tl.setTextOff(holder.tl.getText());
		}
        return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(LOG, "onActivityCreated");
        mDisplayer = (Frag_Displayer) getActivity().getFragmentManager().findFragmentById(R.id.display);
	}

	@Override
	public void onClick(View v) {
		compute();
        mDisplayer.setEnhancer(0);
        mDisplayer.setModif(0);
		mDisplayer.setSplit(1);
		mDisplayer.refresh();
	}

    public void setSelection(List<Attribute> attributes) {
        View frag = getView();
        for (Holder holder:holders) {
            holder.tl.setChecked(attributes.contains(holder.attribute));
        }
        compute();
    }

	private void compute(){
		int num = 0, sum = 0;
		for (Holder holder: holders) {
			if (holder.tl.isChecked()) {
				sum += st.getAttributeValue(holder.attribute);
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

	private class Holder{
		ToggleButton tl;
        Attribute attribute;
	}
}
