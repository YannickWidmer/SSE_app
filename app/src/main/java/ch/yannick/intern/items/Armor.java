package ch.yannick.intern.items;

import ch.yannick.intern.personnage.HitZone;


public class Armor {

    private static String LOG = "Weapon";
    int mWeight;
    private HitZone mPart;
    private String mName;
	private Long mId;
    private int mProtection;
    private int mWeatherProtection;

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

    // getters
	public String getName(){
		return mName;
	}

    public int getProtection(){
        return mProtection;
    }

    public void setProtection(int protection){
        mProtection = protection;
    }

    public int getWeight(){
        return mWeight;
    }

    public void setWeight(int weight){
        mWeight = weight;
    }

    public HitZone getPart(){
        return mPart;
    }

    public void setPart(HitZone part){
        mPart = part;
    }

    public int getWeatherProtection(){
        return mWeatherProtection;
    }

    public void setWeatherProtection(int protection){
        mWeatherProtection = protection;
    }
}