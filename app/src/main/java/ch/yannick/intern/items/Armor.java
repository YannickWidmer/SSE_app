package ch.yannick.intern.items;

import ch.yannick.intern.personnage.HitZone;


public class Armor {

    private static String LOG = "Weapon";

    protected HitZone mPart;
    protected String mName;
	protected Long mId;
    protected int mProtection, mWeight, mWeatherProtection;

    public Armor(Long id, String name){
        mId = id;
        mName = name;
        mPart = HitZone.ARMS;
    }

    public Armor(Long id, String name, HitZone part, int protection, int weight){
		mName=name;
		mId=id;
        mPart = part;
        mProtection = protection;
        mWeight = weight;
    }

	public Long getId(){
		return mId;
	}
	
	public String toString(){
		return mName;
	}


    // Editing methods$
    public void setmName(String name){
        mName = name;
    }

    public void setPart(HitZone part){
        mPart = part;
    }

    public void setProtection(int protection){
        mProtection = protection;
    }

    public void setWeight(int weight){
        mWeight = weight;
    }

    // getters
	public String getName(){
		return mName;
	}

    public int getProtection(){
        return mProtection;
    }

    public int getWeight(){
        return mWeight;
    }

    public HitZone getPart(){
        return mPart;
    }

    public void setWeatherProtection(int protection){
        mWeatherProtection = protection;
    }

    public int getWeatherProtection(){
        return mWeatherProtection;
    }
}