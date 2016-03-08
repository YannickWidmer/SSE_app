package ch.yannick.intern.items;

import ch.yannick.intern.personnage.HitZone;


public class Clothe extends  Item{

    private static String LOG = "Weapon";
    private HitZone mPart= HitZone.ARMS;
    private int mProtection;
    private int mWeatherProtection;

    public Clothe(Long id,Long serverId, Long ownerId,String  name, String description,int weight,
                  HitZone part, int protection,int wheaterProtection){
        super(id, serverId,ownerId, name,description, weight);
        mPart = part;
        mProtection = protection;
        mWeatherProtection = wheaterProtection;
    }

    public Clothe(Item item,
                  HitZone part, int protection,int wheaterProtection){
        super(item);
        mPart = part;
        mProtection = protection;
        mWeatherProtection = wheaterProtection;
    }

    public Clothe(Long id, String name){
        super(id, null, null, name, "",0);
    }


    // getters
	public int getProtection(){
        return mProtection;
    }

    public void setProtection(int protection){
        mProtection = protection;
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