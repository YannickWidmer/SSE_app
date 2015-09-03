package ch.yannick.display.technical;

/**
 * Created by Yannick on 10.05.2015.
 */
public class ColoredHolder<T> {

    private int stringId, color;
    private String text;
    private T object;
    private boolean hasText;

    public ColoredHolder(T object, int string, int color){
        this.object = object;
        this.stringId = string;
        this.color = color;
        hasText = false;
    }

    public ColoredHolder(T object, String string, int color){
        this.object = object;
        this.text = string;
        this.color = color;
        hasText = true;
    }

    public boolean hasText(){
        return hasText;
    }

    public String getString(){
        return text;
    }

    public int getStringId(){
        return stringId;
    }

    public int getColor(){
        return color;
    }

    public T getObject(){
        return object;
    }

}
