package ch.yannick.display.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ch.yannick.context.R;
import ch.yannick.intern.dice.Choice;

public class DiceAdapter extends ArrayAdapter<Choice> {

    private boolean silent=false;

    public void changeSilent(){
        silent = !silent;
    }
	
	public DiceAdapter(Context context, ArrayList<Choice> choices){
		super(context,0,choices);
	}
	
	 @Override
     public View getView(int position, View convertView, ViewGroup parent) {
		 
         if (null == convertView){
             convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_choice, parent, false);
         }
         ((DiceDisplayer)convertView).changeSilent(silent);
         ((DiceDisplayer)convertView).set(getItem(position));
         return convertView;
     }

    public boolean isSilent() {
        return silent;
    }
}
