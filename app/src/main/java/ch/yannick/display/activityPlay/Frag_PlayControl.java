package ch.yannick.display.activityPlay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.display.activityMental.Act_Mental;
import ch.yannick.display.activityMental.Vector;
import ch.yannick.display.technical.AdapterColored;
import ch.yannick.display.technical.ColoredHolder;
import ch.yannick.display.views.JaugeView;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.personnage.HitZone;
import ch.yannick.intern.personnage.Limb;
import ch.yannick.intern.state.State;
import ch.yannick.intern.usables.UsableType;
import ch.yannick.intern.usables.Weapon;

public class Frag_PlayControl extends Fragment {
	private static final String LOG = "frag:Control";
	private static final int act_end = 0, new_round = 3, mental = 4, hit = 5;
	private State st;
    private JaugeView healthJauge, staminaJauge;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(LOG, "onCreateView");
        st = ((RootApplication)getActivity().getApplication()).getCurrentState();
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.frag_play_control, container, false);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.menu_play_control, menu);
	    super.onCreateOptionsMenu(menu, inflater);
	}
	


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(LOG, "onActivityCreated");
        getActivity().setTitle(st.getName());
        View v = getView();

        healthJauge = (JaugeView) v.findViewById(R.id.vie);
        staminaJauge = (JaugeView) v.findViewById(R.id.endurance);
        healthJauge.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), Dialog_health.class);
                intent.putExtra("id", st.getId());
                startActivityForResult(intent, R.id.health);
            }
        });

        staminaJauge.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), Dialog_stamina.class);
                intent.putExtra("id",st.getId());
                startActivityForResult(intent, R.id.stamina);
            }
        });

		v.findViewById(R.id.act).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                act();
            }
        });
		v.findViewById(R.id.takehit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                hit();
            }
        });
        refresh();
	}

    // Accesed by the Button hit. This opens the Dialog where the player can enter the damage he got and where he was hit, such that
    // the effective damage is calculated and then removed from the health bar.
    private void hit() {
		Intent intent = new Intent(getActivity().getApplication(), Dialog_hit.class);
		startActivityForResult(intent,hit);
	}


    /* This is accesed via the act and react buttons, this is what the player will do every time when its his turn or he has the ocasion to react.
     * The possible actions are displayed, those which from the State object should not be allowed are grayed out but still
     * clickable if for some reason the master should still allow these actions.
     * Once the action is chosen the method act(action,react) is called.
     */
	private void act() {
		if(st.canAct(0)){
            List<ColoredHolder<Action>> actionsColored = new ArrayList<>();
            for(Action action:st.getActions(Limb.ALL)){
                actionsColored.add(new ColoredHolder<Action>(action, action.getStringId(),
                        st.canAct(action, Limb.ALL) ? R.color.white : R.color.grey));
                Log.d(LOG,action.getName() +" canAct "+st.canAct(action, Limb.ALL));
            }

            final AdapterColored<Action> actionAdapter = new AdapterColored<Action>(getActivity(),actionsColored);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.act);
            builder.setSingleChoiceItems(actionAdapter, 1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position) {
                    Action action = actionAdapter.getItem(position).getObject();
                    act(action);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
		}else{
            toTired();
		}
	}

    /* This method is called when act(reaction) is called and an action is chosen. This method first check if the action can only
    * be acomplished with one weapon or if there is a choice. In case there are multiple posiibilities it opens a list dialog with the different
    * choices, the choices which should not be possible considering the State are greeyed out but still clickable in case the master should
    * still allow these. Once the choice is made the method fatigue is called.
     */
    private void act(final Action act){
        final List<ColoredHolder<Limb>> coloredPossiblities = new ArrayList<>();

        for(Limb limb:Limb.values()){
            if(st.hasUsable(limb) && st.getActions(limb).contains(act)){
                coloredPossiblities.add(new ColoredHolder<>(limb,limb.getStringId(),
                        st.canAct(act, limb)?R.color.white:R.color.grey));
            }
        }


        if(coloredPossiblities.size()>1){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.act);
            AdapterColored<Limb> adapterColored = new AdapterColored<Limb>(getActivity(),coloredPossiblities);
            builder.setSingleChoiceItems(adapterColored, 1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position) {
                    fatigue(coloredPossiblities.get(position).getObject(),act);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    refresh();
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }else
            fatigue(coloredPossiblities.get(0).getObject(),act);
    }

    /* This method is the last of the three methods when act is triggered. This method opens the fatigue dialog such that the player might
     * adjust the fatigue for this action or reaction, most of the time he shoud just click ok. Further more this method also makes changes
     * which might arrise from the action. This is change the load state of a range weapon,
     */
    private void fatigue(Limb which, Action action){
        if(st.getUsable(which).getTyp() == UsableType.CROSSBOW  && action.is("Shoot"))
            ((Weapon)st.getUsable(which)).setLoad(false);
        if(st.getUsable(which).getTyp() == UsableType.CROSSBOW && action == Action.valueOf("LOADING"))
            ((Weapon)st.getUsable(which)).setLoad(true);

        Intent intent;
        intent = new Intent(getActivity().getApplication(), Dialog_fatigue.class);
        intent.putExtra("id",st.getId());
        intent.putExtra("which",which.name());
        intent.putExtra("action",action.getName());
        startActivityForResult(intent,act_end);
    }


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   Intent intent;
		 switch (item.getItemId()) {
	        case R.id.arsenal:
	        	intent = new Intent(getActivity().getApplication(), Act_Equipement.class);
	        	intent.putExtra("id", st.getId());
	    		startActivityForResult(intent, R.id.arsenal);
	    		return true;
	        case R.id.next_round:
	    		intent = new Intent(getActivity().getApplication(), Act_Mental.class);
  				intent.putExtra("State", st.getId());
  				startActivityForResult(intent, mental);
	    		return true;
	        case R.id.stamina:
	        	intent = new Intent(getActivity().getApplication(), Dialog_stamina.class);
	        	intent.putExtra("id",st.getId());
	    		startActivityForResult(intent, R.id.stamina); 
	    		return true;
	        case R.id.health:
	        	intent = new Intent(getActivity().getApplication(), Dialog_health.class);
	        	intent.putExtra("id",st.getId());
	    		startActivityForResult(intent, R.id.health); 
	    		return true;
             case R.id.reset_mental:
                 st.setMentalState(new Vector(0,0));
                 return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	// Result from Dialogs
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  	if(resultCode == Activity.RESULT_OK){
	  		switch(requestCode) {
                case hit:
                    Log.d(LOG, "bodyPart " + data.getIntExtra("bodyPart", -1)
                            + " hit " + data.getIntExtra("hit", 0)
                            + " direct " + data.getBooleanExtra("direct", false)
                            + " pierce " + data.getIntExtra("pierce", 0));
                    HitZone hitZone = HitZone.valueOf(data.getStringExtra("bodyPart"));
                    int hit = data.getIntExtra("hit", 0);
                    int pierce = data.getIntExtra("pierce", 0);
                    boolean direct = data.getBooleanExtra("direct", false);
                    st.takeHit(hitZone, hit, pierce, direct);
                    break;
                case act_end:
                    if (!st.act(data.getIntExtra("fatigue", 0)))
                        toTired();
                    break;
                case mental:
                    Intent intent = new Intent(getActivity().getApplication(), Dialog_fatigue.class);
                    intent.putExtra("id", st.getId());
                    intent.putExtra("stamina",0);
                    startActivityForResult(intent, new_round);
                    break;
                case new_round:
                    st.newRound();
                    if (!st.act(data.getIntExtra("fatigue", 0)))
                        toTired();
                    break;
            }
	  	}
	  	refresh();
	}
	
	protected void refresh(){
        healthJauge.setValues(st.getHealth(), 0, st.getHealthMax() - st.getHealth());
		staminaJauge.setValues(st.getStaminaNow(), 0,
                st.getStaminaMax() - st.getStaminaNow());
        ((TextView) getView().findViewById(R.id.mental_state)).setText(st.getMentalState().getStringId());
        ((TextView) getView().findViewById(R.id.race)).setText(st.getRace().getStringId());
    }

    private void toTired(){
        Toast.makeText(getActivity(),R.string.to_tired,Toast.LENGTH_SHORT).show();
    }
}
