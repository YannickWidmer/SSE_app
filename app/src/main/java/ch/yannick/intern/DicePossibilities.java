package ch.yannick.intern;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.yannick.enums.Dice;

public class DicePossibilities {
    private static String LOG="DicePossibilities";
	int length,mSum;
	private Map<Dice,Integer> mMaxes;
	private ArrayList<Choice> mResult;
	private Choice mChoice;
	private Dice mHalfDice;
	private boolean reversed;

	
	public DicePossibilities(){
        Log.d(LOG, "constructor");
		length= Dice.values().length;
		mResult = new ArrayList<Choice>();
		mMaxes = new HashMap<>();
	}

	public ArrayList<Choice> getResult(){
		if(!reversed) {
			Collections.reverse(mResult);
			reversed = false;
		}
		return mResult;
	}
	
	public boolean set(int skill, int modif){
		//initialize
		mResult.clear();
		mSum = skill;
		mMaxes.clear();
		reversed = false;
		
		//Variables
		// max[i] c'est le nombre de dee i qui depasse
		int maximum;
		for(Dice d:Dice.values()){
            maximum = mSum/d.getEyes()+1;
            if(maximum>3) maximum =3;
			mMaxes.put(d,maximum);
		}
		
		//Quelle est le plus petit des pour la regle du plus grand des
		mHalfDice = Dice.getSmallest();
		for(Dice d:Dice.values()) {
            if (2 * d.getEyes() >= mSum) {
                mHalfDice = d;
                break;
            }
        }
		
		mChoice = new Choice(mSum,mHalfDice);
		mSum += modif;
		return next();
	}

	public boolean next(){
		if(mChoice.getRest()<=0 && -mChoice.getRest()<Dice.getSmallest().getEyes()){
			mResult.add(new Choice(mChoice.getDices(), mSum));
		}
		return mChoice.next(mMaxes,mHalfDice);
	}
}
