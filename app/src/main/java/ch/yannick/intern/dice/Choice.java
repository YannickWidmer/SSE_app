package ch.yannick.intern.dice;

import java.util.HashMap;
import java.util.Map;

public class Choice {
	private Map<Dice,Integer> dices;
	private int sum,rest;

    public Choice(int sum, Dice half){
        dices = new HashMap<>();
        dices.put(half,1);

        setDices(dices,sum);
    }
	
	public Choice(Map<Dice,Integer> diceChoice,int sum){
		setDices(diceChoice,sum);
	}

    private void setDices(Map<Dice, Integer> diceChoice, int sum){
        this.dices = diceChoice;
        this.sum = sum;

        this.sum = sum;
        int sumOfDices=0;
        for(Dice d:Dice.values())
            if(dices.containsKey(d))
                sumOfDices+= d.getEyes()*dices.get(d);
        this.rest = sum-sumOfDices;
    }
	
	public Map<Dice,Integer> getDices(){
		return dices;
	}
	
	public int getRest(){
		return rest;
	}
	
	@Override
	public String toString(){
		String res ="";
		for(Dice dice:Dice.values()){
            for(int i=0;i<dices.get(dice);++i)
    			res += dice.getEyes()+":";
		}
		return res+rest;
	}

    public boolean next(Map<Dice,Integer> max, Dice half){

        // Make Copy
        Map<Dice,Integer> newDices = new HashMap<Dice,Integer>();
        for(Dice d:Dice.values())
            newDices.put(d,dices.containsKey(d)?dices.get(d):0);

        // Compute next
        boolean isLast = true;
        for(Dice d:Dice.values()){
            if(newDices.get(d)<max.get(d)) {
                newDices.put(d,newDices.get(d) + 1);
                isLast = false;
                break;
            }else{
                newDices.put(d,0);
            }
        }

        setDices(newDices,sum);
        return !isLast;
    }
}
