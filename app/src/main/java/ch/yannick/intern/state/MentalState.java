package ch.yannick.intern.state;

import ch.yannick.context.R;
import ch.yannick.display.activityMental.Vector;

/**
 * Created by Yannick on 05.07.2015.
 */
public enum MentalState {
    NEUTRAL(new Vector(0,0),5,1000, R.string.mental_neutral),
    FRENETIC(new Vector(-25,15),15,20,R.string.frenetic),
    AGRESSIVE(new Vector(-45,25),25,20,R.string.agressive),
    BERSERK(new Vector(-25,45),800,10,R.string.berserk),
    FOCUSED(new Vector(20,6),15,20,R.string.focused),
    IMMERSED(new Vector(44,16),25,20,R.string.immersed),
    ECSTASY(new Vector(40,40),-30,15,R.string.ecstasy),
    CONCENTRATED(new Vector(-15,-15),15,20,R.string.concentrated),
    METAPHASE(new Vector(-30,-25),25,25,R.string.metaphase),
    SYNESTHESIA(new Vector(-10,-45),500,10,R.string.SYNESTHESIA),
    DELIRIUM(new Vector(5,35),5,30,R.string.delirium),
    FEAR(new Vector(30,-30),10,30,R.string.fear),
    SHOCK(new Vector(-50,-10),50,20,R.string.shock);


    public final static int halfWidth = 60, halfHeight = 50;
    private static String LOG = "MentalState";
    private int gravity, radius, stringId;
    private Vector position;
    MentalState(Vector vec,int gravity, int radius, int stringId){
        this.position = vec;
        this.gravity = gravity;
        this.radius = radius;
        this.stringId = stringId;
    }

    public static MentalState getState(Vector vec){
        MentalState res = NEUTRAL;
        double distance = vec.distance(NEUTRAL.position);
        double tempDistance = 0;
        for(MentalState ms:MentalState.values()){
            tempDistance = vec.distance(ms.position);
            if(tempDistance < distance){
                distance = tempDistance;
                res = ms;
            }
        }
        return res;
    }

    public static Vector getForce(Vector vec){
        Vector res = new Vector(0,0);
        for(MentalState ms:MentalState.values()){
            if(vec.distance(ms.position)<ms.radius){{
                res.addThis(ms.position.subs(vec),ms.gravity); // (position - mentalstate)*gravity
            }}
        }
        return res.multThis(-1);
    }

    public int getRadius(){
        return this.radius;
    }

    public Vector getPosition(){
        return position;
    }

    public int getStringId() {
        return stringId;
    }
}
