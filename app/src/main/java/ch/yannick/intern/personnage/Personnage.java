package ch.yannick.intern.personnage;

import android.util.SparseIntArray;

import java.util.HashMap;
import java.util.Map;

import ch.yannick.context.R;
import ch.yannick.intern.action_talent.Talent;

public class Personnage {

	private String name;
	private Race race;
	private int age;
	private Long id;
	private SparseIntArray attr;
	private Map<Talent,Integer> mTalents;
	
	public Long getId() {
		return id;
	}	
	
	public Personnage(String name, Long id){
		attr=new SparseIntArray(8);
		this.name=name;
		race = Race.HUMAN;
		this.id=id;
		mTalents = new HashMap<>();
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public void setAttr(Integer force,Integer agilite,Integer vitesse,
			Integer sagacite,Integer charme,Integer acuite,Integer constitution,
			Integer endurance,Integer will, Integer magie){
		attr.put(R.id.KEY_KA,force);
		attr.put(R.id.KEY_GK,agilite);
		attr.put(R.id.KEY_GW,vitesse);
		attr.put(R.id.KEY_SZ,sagacite);
		attr.put(R.id.KEY_CH, charme);
		attr.put(R.id.KEY_SS,acuite);
		attr.put(R.id.KEY_KB, constitution);
		attr.put(R.id.KEY_AU, endurance);
		attr.put(R.id.KEY_WI,will);
		attr.put(R.id.KEY_MA, magie);
	}

	public void setTalents(Map<Talent,Integer> talents){
		mTalents = talents;
	}

	public Map<Talent,Integer>  getTalents(){
		return mTalents;
	}

	public void setTalent(Talent tl, int value){
		mTalents.put(tl,value);
	}
	
	public SparseIntArray getAttr(){
		return attr;
	}
	
	public int getAttr(Attribute attribute){
		return attr.get(attribute.getId());
	}
	
	public void setAttr(Attribute attribute, int value){
		int attId = attribute.getId();
        if(value >= 0)
			attr.put(attId,value);
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public void setRasse(Race race){
		this.race=race;
	}
	
	public Race getRasse(){
		return race;
	}
	
	public void setAge(int age){
		this.age=age;
	}
	
	public int getAge(){
		return age;
	}

	public int getVie() {
		return attr.get(R.id.KEY_KB)*3;
	}
	
	public int getStamina(){
		return attr.get(R.id.KEY_AU)*4;
	}
}
