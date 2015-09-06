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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.yannick.context.R;
import ch.yannick.display.activityMental.Act_Mental;
import ch.yannick.display.activityMental.Vector;
import ch.yannick.display.technical.AdapterColored;
import ch.yannick.display.technical.ColoredHolder;
import ch.yannick.display.technical.EnumAdapter;
import ch.yannick.display.views.JaugeView;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.personnage.HitZone;
import ch.yannick.intern.personnage.Limb;
import ch.yannick.intern.state.State;

public class Frag_PlayControl extends Fragment implements AdapterView.OnItemClickListener {
	private static final String LOG = "frag:Control";
	private static final int act_end = 0, react_end = 2, new_round = 3, mental = 4;
	private State st;
    private Frag_PlayAttributes mAttr;
    private Frag_Displayer mDisplayer;
    private LinearLayout mLeftWeaponLayout, mRightWeaponLayout;
    private TextView mWeaponLeftName, mWeaponRightName;
    private JaugeView healthJauge, staminaJauge;
    private View loaded_left,unloaded_left, loaded_right, unLoaded_right;
    private EnumAdapter<Action> mAdapterLeft,mAdapterRight, freeAction;
    private ArrayList<Action> mActionArrayLeft, mActionArrayRight, mActionArrayFree;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(LOG, "onCreateView");
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.frag_play_control, container, false);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.menu_play_control, menu);
	    super.onCreateOptionsMenu(menu, inflater);
	}
	
	public void setState(State state) {
		st = state;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(LOG, "onActivityCreated");

        getActivity().setTitle(st.getName());

        mAttr = (Frag_PlayAttributes) getActivity().getFragmentManager().findFragmentById(R.id.attributes);
        mDisplayer = (Frag_Displayer) getActivity().getFragmentManager().findFragmentById(R.id.display);

        View v = getView();

        healthJauge = (JaugeView) v.findViewById(R.id.vie);
        staminaJauge = (JaugeView) v.findViewById(R.id.endurance);
        healthJauge.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), Dialog_stamina.class);
                intent.putExtra("id", st.getId());
                startActivityForResult(intent, R.id.stamina);
            }
        });

        staminaJauge.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), Dialog_health.class);
                intent.putExtra("id",st.getId());
                startActivityForResult(intent, R.id.health);
            }
        });

        mLeftWeaponLayout = (LinearLayout) v.findViewById(R.id.weapon_left);
        mRightWeaponLayout = (LinearLayout) v.findViewById(R.id.weapon_right);

        mWeaponLeftName = (TextView) v.findViewById(R.id.weapon_left_name);
        mWeaponRightName = (TextView) v.findViewById(R.id.weapon_right_name);

        loaded_left = getView().findViewById(R.id.loaded_left);
        unloaded_left = getView().findViewById(R.id.unloaded_left);
        loaded_right =  getView().findViewById(R.id.loaded_right);
        unLoaded_right = getView().findViewById(R.id.unloaded_right);

		v.findViewById(R.id.act).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                act(false);
            }
        });
		v.findViewById(R.id.react).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                act(true);
            }
        });
		v.findViewById(R.id.takehit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                hit();
            }
        });

        mActionArrayLeft = new ArrayList<>();
        mActionArrayRight = new ArrayList<>();
        mActionArrayFree = new ArrayList<>();

        mAdapterLeft = new EnumAdapter<>(getActivity(), mActionArrayLeft, R.layout.row_centered_text);
        mAdapterRight = new EnumAdapter<>(getActivity(), mActionArrayRight, R.layout.row_centered_text);
        freeAction = new EnumAdapter<>(getActivity(),mActionArrayFree,R.layout.row_centered_text);

        ((ListView)v.findViewById(R.id.left_actions)).setAdapter(mAdapterLeft);
        ((ListView)v.findViewById(R.id.left_actions)).setOnItemClickListener(this);
        ((ListView)v.findViewById(R.id.right_actions)).setAdapter(mAdapterRight);
        ((ListView)v.findViewById(R.id.right_actions)).setOnItemClickListener(this);
        ((ListView)v.findViewById(R.id.middle_actions)).setAdapter(freeAction);
        ((ListView)v.findViewById(R.id.middle_actions)).setOnItemClickListener(this);
        refresh();
	}

    private void hit() {
		Intent intent = new Intent(getActivity().getApplication(), Dialog_hit.class);
		startActivityForResult(intent,R.id.degats);
	}

	private void act(final boolean reaction) {
		if(st.canAct(0,reaction)){
            List<ColoredHolder<Action>> actionsColored = new ArrayList<>();
            for(Action action:st.getActions(Limb.ALL)){
                actionsColored.add(new ColoredHolder<Action>(action,action.getStringId(),
                        st.canAct(action, Limb.ALL, reaction)?R.color.white:R.color.grey));
                Log.d(LOG,action.name() +" canAct "+st.canAct(action, Limb.ALL, reaction));
            }

            final AdapterColored<Action> actionAdapter = new AdapterColored<Action>(getActivity(),actionsColored);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(reaction?R.string.react:R.string.act);
            builder.setSingleChoiceItems(actionAdapter, 1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position) {
                    Action action = actionAdapter.getItem(position).getObject();
                    act(action,reaction);
                    refresh();
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

    private void act(final Action act,final boolean reaction){
        final List<ColoredHolder<Limb>> coloredPossiblities = new ArrayList<>();

        for(Limb limb:Limb.values()){
            if(st.hasWeapon(limb) && st.getActions(limb).contains(act)){
                coloredPossiblities.add(new ColoredHolder<>(limb,limb.getStringId(),
                        st.canAct(act, limb, reaction)?R.color.white:R.color.grey));
            }
        }


        if(coloredPossiblities.size()>1){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(reaction ? R.string.act : R.string.react);
            AdapterColored<Limb> adapterColored = new AdapterColored<Limb>(getActivity(),coloredPossiblities);
            builder.setSingleChoiceItems(adapterColored, 1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position) {
                    fatigue(coloredPossiblities.get(position).getObject(),act,reaction);
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
        }else
            fatigue(coloredPossiblities.get(0).getObject(),act,reaction);
    }

    private void fatigue(Limb which, Action action, boolean reaction){
        Intent intent;
        intent = new Intent(getActivity().getApplication(), Dialog_fatigue.class);
        intent.putExtra("id",st.getId());
        intent.putExtra("which",which.name());
        intent.putExtra("action",action.name());
        intent.putExtra("reaction",reaction);
        startActivityForResult(intent,reaction?react_end:act_end);
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
                case R.id.degats:
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
                    if (!st.act(data.getIntExtra("fatigue", 0), false))
                        toTired();
                    break;
                case react_end:
                    if (!st.act(data.getIntExtra("fatigue", 0), true))
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
                    if (!st.act(data.getIntExtra("fatigue", 0), true))
                        toTired();
                    break;
            }
	  	}
	  	refresh();
	}
	
	private void refresh(){
        mLeftWeaponLayout.setVisibility(View.GONE);
        mRightWeaponLayout.setVisibility(View.GONE);
        if(st.hasWeapon(Limb.LEFTHAND)) {
            mWeaponLeftName.setText(st.getWeapon(Limb.LEFTHAND).getName());
            mActionArrayLeft.clear();
            mActionArrayLeft.addAll(st.getActions(Limb.LEFTHAND));
            loaded_left.setVisibility(View.GONE);
            unloaded_left.setVisibility(View.GONE);
            if(st.getWeapon(Limb.LEFTHAND).getType().isLoadable()){
                if(st.getWeapon(Limb.LEFTHAND).getIsLoaded()){
                    loaded_left.setVisibility(View.VISIBLE);
                }else{
                    unloaded_left.setVisibility(View.VISIBLE);
                }
            }
            mLeftWeaponLayout.setVisibility(View.VISIBLE);
        }
        if(st.hasWeapon(Limb.RIGHTHAND)){
            mWeaponRightName.setText(st.getWeapon(Limb.RIGHTHAND).getName());
            mActionArrayRight.clear();
            mActionArrayRight.addAll(st.getActions(Limb.RIGHTHAND));
            loaded_right.setVisibility(View.GONE);
            unLoaded_right.setVisibility(View.GONE);
            if(st.getWeapon(Limb.RIGHTHAND).getType().isLoadable()){
                if(st.getWeapon(Limb.RIGHTHAND).getIsLoaded()){
                    loaded_right.setVisibility(View.VISIBLE);
                }else{
                    unLoaded_right.setVisibility(View.VISIBLE);
                }
            }
            mRightWeaponLayout.setVisibility(View.VISIBLE);
        }

        mActionArrayFree.clear();
        mActionArrayFree.addAll(st.getActions(Limb.BAREHANDS));
        mActionArrayFree.remove(Action.OTHER);

        healthJauge.setValues(st.getHealth(), 0, st.getHealthMax() - st.getHealth());
		staminaJauge.setValues(st.getStaminaNow(), st.getStaminaUsed(),
                st.getStaminaMax() - st.getStaminaNow() - st.getStaminaUsed());

        ((TextView) getView().findViewById(R.id.mental_state)).setText(st.getMentalState().getStringId());
        ((TextView) getView().findViewById(R.id.race)).setText(st.getRace().getStringId());
    }


    // Actions in listviews
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mDisplayer.setModif(0);
        mDisplayer.setAlter(0);
        mDisplayer.hideDegats();

        Action action;
        Limb which;
        switch(parent.getId()) {
            case R.id.left_actions:
                action = mActionArrayLeft.get(position);
                which = Limb.LEFTHAND;
                break;
            case R.id.right_actions:
                action = mActionArrayRight.get(position);
                which = Limb.RIGHTHAND;
                break;
            default:
                action = mActionArrayFree.get(position);
                which = Limb.BAREHANDS;
        }

        mAttr.setSelection(st.getAttribute(action,which));
        mDisplayer.setAlter(st.getSkillEnhancer(action, which));
        mDisplayer.setModif(st.getSkillModifier(action,which));
        if(action.isAttack())
            mDisplayer.setDegats(st.getDegatsDice(which,action),st.getDegats(which,action),st.getPenetration(which,action),st.getWeapon(which).isDirect(action));
        mDisplayer.refresh();
    }

    private void toTired(){
        Toast.makeText(getActivity(),R.string.to_tired,Toast.LENGTH_SHORT).show();
    }
}
