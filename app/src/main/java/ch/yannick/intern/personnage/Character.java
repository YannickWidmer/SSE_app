package ch.yannick.intern.personnage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.items.Equipement;

public class Character {

	private String name, mJob;
	private Race race;
	private int age;
	private Long id,mserverId;
	private Map<Attribute,Integer> mAttributes = new HashMap<>();
	private Map<Talent,Integer> mTalents = new HashMap<>();
	
	public Long getId() {
		return id;
	}

	public Long getServerId(){
		return mserverId;
	}

	public void setServerId(Long id){
		mserverId = id;
	}
	
	public Character(String name, Long id){
		this.name=name;
		race = Race.HUMAN;
		this.id=id;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public void setAttr(Integer force,Integer agilite,Integer vitesse,
			Integer sagacite,Integer charme,Integer acuite,Integer constitution,
			Integer endurance,Integer will, Integer magie){
        mAttributes.put(Attribute.FORCE,force);
        mAttributes.put(Attribute.AGILITY,agilite);
        mAttributes.put(Attribute.SPEED,vitesse);
        mAttributes.put(Attribute.ASTUTENESS,sagacite);
        mAttributes.put(Attribute.CHARM,charme);
        mAttributes.put(Attribute.ACUITY,acuite);
        mAttributes.put(Attribute.PHYSIQUE,constitution);
        mAttributes.put(Attribute.STAMINA,endurance);
        mAttributes.put(Attribute.WILL,will);
        mAttributes.put(Attribute.MAGIC,magie);
	}

	public void setTalents(Map<Talent,Integer> talents){
		mTalents = talents;
	}

	public Map<Talent,Integer>  getTalents(){
		return mTalents;
	}

	public void setTalent(Talent tl, int value) {
        mTalents.put(tl, value);
    }

	public int getAttr(Attribute attribute){
		if(mAttributes.containsKey(attribute))
    		return mAttributes.get(attribute);
        return 0;
	}
	
	public void setAttr(Attribute attribute, int value){
        if(value >= 0)
			mAttributes.put(attribute,value);
	}

    public void removeAttribute(Attribute a){
        mAttributes.remove(a);
    }

    public Set<Attribute> getAttributes(){
        return mAttributes.keySet();
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

	public String getJob(){
		return mJob;
	}

	public void setJob(String job){
		mJob = job;
	}

	public void setAge(int age){
		this.age=age;
	}
	
	public int getAge(){
		return age;
	}

	public int getVie() {
		return getAttr(Attribute.PHYSIQUE);
	}
	
	public int getStamina(){
		return getAttr(Attribute.STAMINA);
	}

	public void setMalus(Equipement malus) {
		 // TODO
	}

	public void setId(Long id) {
		this.id = id;
	}
}
