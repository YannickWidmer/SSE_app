package ch.yannick.activityMental;

import android.graphics.RectF;

/**
 * Created by Yannick on 05.07.2015.
 */
public class Vector {
    private double x,y;

    public Vector(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector(Vector v){
        this.x = v.getX();
        this.y = v.getY();
    }

    public String toString(){
        return "Vector x:"+x+" y:" + y;
    }
    public Vector addThis(Vector vec){
        x += vec.x;
        y += vec.y;
        return this;
    }

    public Vector addThis(Vector vec, double scale){
        x += vec.x*scale;
        y += vec.y*scale;
        return this;
    }

    public Vector add(Vector vec,double lm){
        Vector res = new Vector(vec.getX()*lm,lm*vec.getY());
        return res.addThis(this);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector copy() {
        return new Vector(x,y);
    }

    public RectF getRect(double v) {
        return new RectF((int)(x-v),(int)(y-v),(int)(x+v),(int)(y+v));
    }

    public double distance(Vector vec){
        return Math.sqrt(Math.pow(Math.abs(x - vec.x), 2)+Math.pow(Math.abs(y-vec.y),2));
    }

    public double size(){
        return Math.sqrt(Math.pow(Math.abs(x ), 2)+Math.pow(Math.abs(y),2));
    }

    public Vector subs(Vector vec){
        return new Vector(x-vec.x,y-vec.y);
    }

    public Vector multThis(double forceScale) {
        x *= forceScale;
        y *= forceScale;
        return this;
    }

    public Vector mult(double forceScale) {
        return new Vector(x*forceScale,y*forceScale);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
