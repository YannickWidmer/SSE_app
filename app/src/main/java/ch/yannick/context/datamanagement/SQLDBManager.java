package ch.yannick.context.datamanagement;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import ch.yannick.context.RootApplication;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.items.Clothe;
import ch.yannick.intern.items.Item;
import ch.yannick.intern.personnage.Attribute;
import ch.yannick.intern.personnage.Character;
import ch.yannick.intern.personnage.HitZone;
import ch.yannick.intern.personnage.Race;
import ch.yannick.intern.usables.Weapon;
import ch.yannick.intern.usables.UsableInterface;
import ch.yannick.intern.usables.UsableType;

public class SQLDBManager extends SQLiteOpenHelper {

	private static final String LOG="SQLManager";
	
	  // All Static variables related to db
    // Database Version
    private static final int DATABASE_VERSION = 12;
 
    // Database Name
    private static final String DATABASE_NAME = "SSE";
 
    // table names
    private static final String
            TABLE_CHARACTER = "character", TABLE_ATTRIBUTE = "attribute", TABLE_TALENT = "talent", TABLE_STATE = "state",
            TABLE_ITEM= "item", TABLE_USABLE = "usable", TABLE_CLOTHE ="clothe", TABLE_SPELL ="spell", TABLE_MANA_COUNT = "mana_count",
            TABLE_ATTRIBUTE_ACTION ="attribute_action";

    //Table columns for all tables
    private static final String KEY_NAME = "name", KEY_DESCRIPTION = "description",
            KEY_ID="id", KEY_SERVER_ID = "server_id",
            KEY_VALUE = "value";

    // Foreign Keys
    private static final String KEY_CHARACTER_ID = "character_id",KEY_ITEM_ID = "item_id",KEY_STATE_ID = "state_id";



    // Character Table Columns names
    private static final String  KEY_RACE = "race",
                                KEY_CLASS = "class";

    // attirbute and talent Table columns
    //NONe


    //item TABLE columsn
    private static final String KEY_WEIGHT = "weight";

    // usable Table Columns names
    private static final String KEY_TYPE= "type";

    // clothes table columns
    private static final String KEY_WHEATER_PROTECTION = "wheater_protection",
            KEY_PROTECTION  = "protection",
            KEY_BODYPART ="bodypart";

    // mana count table columns
    private static final String  KEY_MANA = "mana";

	// action Table Columns
    private static final String  KEY_ATTRIBUTES="attributes",
            KEY_FATIGUE="fatigue", KEY_MODIFIER = "modifier", KEY_TICKS ="ticks",
            KEY_RESULT="resultValue", KEY_SKILL_ENHANCER = "enhancer";

    // state table colums
    private static final String KEY_LIFE_MAX = "life_max", KEY_LIFE_NOW ="life_now",
            KEY_STAMINA_MAX ="stamina_max", KEY_STAMINA_NOW = "stamina_now",
            KEY_PERIOD_STAMINA_MAX = "period_stamina_max", KEY_PERIOD_STAMINA_NOW= "period_stamina_now", KEY_PERIOD_STAMINA_USED = "period_stamina_used",
            KEY_MENTALSTATE_X = "mentalstate_x", KEY_MENTALSTATE_Y = "mentalstate_y", KEY_MENTALSTATE = "mentalstate";


    private SQLiteDatabase db;
   
    public SQLDBManager(RootApplication context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CHARACTER_TABLE = "CREATE TABLE " + TABLE_CHARACTER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SERVER_ID + " LONG,"
                + KEY_RACE + " STRING,"
                + KEY_CLASS + " STRING"
                + ");";

        String CREATE_ATTRIBUTE_TABLE = "CREATE TABLE " + TABLE_ATTRIBUTE + "("
                + KEY_CHARACTER_ID + " INTEGER,"
                + KEY_NAME + " VARCHAR,"
                + KEY_VALUE + " INTEGER,"
                + " FOREIGN KEY ("+KEY_CHARACTER_ID+") REFERENCES "+TABLE_CHARACTER+" ("+KEY_ID+") ON DELETE CASCADE);";

        String CREATE_TALENT_TABLE=" CREATE TABLE "+ TABLE_TALENT+"("
                + KEY_CHARACTER_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " VARCHAR,"
                + KEY_VALUE + " INTEGER,"
                + " FOREIGN KEY ("+KEY_CHARACTER_ID+") REFERENCES "+TABLE_CHARACTER+" ("+KEY_ID+") ON DELETE CASCADE);";

        String CREATE_STATE_TABLE = "CREATE TABLE "+ TABLE_STATE +"("
                + KEY_CHARACTER_ID + " INTEGER PRIMARY KEY,"
                + KEY_LIFE_MAX + " INTEGER,"
                + KEY_LIFE_NOW + " INTEGER,"
                + KEY_STAMINA_MAX + " INTEGER,"
                + KEY_STAMINA_NOW + " INTEGER,"
                + KEY_PERIOD_STAMINA_MAX + " INTEGER,"
                + KEY_PERIOD_STAMINA_NOW+ " INTEGER,"
                + KEY_PERIOD_STAMINA_USED + " INTEGER,"
                + KEY_MENTALSTATE + " VARCHAR(255),"
                + KEY_MENTALSTATE_X + " INTEGER,"
                + KEY_MENTALSTATE_Y+ " INTEGER,"
                + " FOREIGN KEY ("+KEY_CHARACTER_ID+") REFERENCES "+TABLE_CHARACTER+" ("+KEY_ID+") ON DELETE CASCADE);";

        String CREATE_TABLE_ITEM = " CREATE TABLE "+ TABLE_ITEM +"("
                +KEY_ID + " INTEGER PRIMARY KEY,"
                +KEY_NAME + " STRING,"
                +KEY_SERVER_ID + " LONG,"
                +KEY_CHARACTER_ID+ " INTEGER,"
                +KEY_DESCRIPTION + " TEXT,"
                +KEY_WEIGHT + " INTEGER,"
                + " FOREIGN KEY ("+KEY_CHARACTER_ID+") REFERENCES "+TABLE_CHARACTER+" ("+KEY_ID+") ON DELETE CASCADE);";

        String CREATE_USABLE_TABLE=" CREATE TABLE "+ TABLE_USABLE +"("
        		+ KEY_ITEM_ID + " INTEGER,"
                + KEY_TYPE +" STRING,"
                + " FOREIGN KEY ("+KEY_ITEM_ID+") REFERENCES "+TABLE_ITEM+" ("+KEY_ID+") ON DELETE CASCADE);";

        String CREATE_TABLE_CLOTHE = "CREATE TABLE "+ TABLE_CLOTHE+"("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_ITEM_ID + " INTEGER,"
                + KEY_BODYPART + " VARCHAR(255),"
                + KEY_WHEATER_PROTECTION +" INTEGER,"
                + KEY_PROTECTION +" INTEGER,"
                + " FOREIGN KEY ("+KEY_ITEM_ID+") REFERENCES "+TABLE_ITEM+" ("+KEY_ID+") ON DELETE CASCADE);";


        String CREATE_TABLE_SPELL = "CREATE TABLE " + TABLE_SPELL +"("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_CHARACTER_ID + " INTEGER,"
                + " FOREIGN KEY ("+KEY_CHARACTER_ID+") REFERENCES "+TABLE_CHARACTER+" ("+KEY_ID+") ON DELETE CASCADE);";

        String CREATE_TABLE_MANA_COUNT = "CREATE TABLE "+ TABLE_MANA_COUNT + "("
                + KEY_ITEM_ID + " INTEGER,"
                + KEY_STATE_ID + " INTEGER,"
                + KEY_MANA + " INTEGER,"
                + " FOREIGN KEY ("+KEY_ITEM_ID+") REFERENCES "+TABLE_ITEM+" ("+KEY_ID+") ON DELETE CASCADE"
                + " FOREIGN KEY ("+KEY_STATE_ID+") REFERENCES "+TABLE_STATE+" ("+KEY_ID+") ON DELETE CASCADE);";

        String CREATE_TABLE_ACTION = "CREATE TABLE "+ TABLE_ATTRIBUTE_ACTION +"("
                + KEY_ITEM_ID + " INTEGER,"
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " STRING,"
                + KEY_ATTRIBUTES +" VARCHAR,"
                + KEY_FATIGUE + " INTEGER,"
                + KEY_TICKS + " INTEGER,"
                + KEY_SKILL_ENHANCER + " INTEGER,"
                + KEY_MODIFIER + " INTEGER,"
                + KEY_RESULT +" VARCHAR,"
                + " FOREIGN KEY ("+KEY_ITEM_ID+") REFERENCES "+TABLE_ITEM+" ("+KEY_ID+") ON DELETE CASCADE);";

        db.execSQL(CREATE_CHARACTER_TABLE);
        db.execSQL(CREATE_ATTRIBUTE_TABLE);
        db.execSQL(CREATE_STATE_TABLE);
        db.execSQL(CREATE_TABLE_ACTION);
        db.execSQL(CREATE_TABLE_CLOTHE);
        db.execSQL(CREATE_TABLE_MANA_COUNT);
        db.execSQL(CREATE_TALENT_TABLE);
        db.execSQL(CREATE_USABLE_TABLE);
        db.execSQL(CREATE_TABLE_ITEM);
        db.execSQL(CREATE_TABLE_SPELL);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTRIBUTE_ACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTRIBUTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MANA_COUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPELL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARACTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLOTHE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TALENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USABLE);
        // Create tables again
        onCreate(db);
    }

    //////////// PERSONAGES //////////////////////////////////

    public synchronized Long pushCharacter(Character p){ // add or update personnage
    	ContentValues c = new ContentValues();
    	c.put(KEY_NAME, p.toString());
        c.put(KEY_RACE, p.getRasse().name());
        c.put(KEY_SERVER_ID, p.getServerId());
        c.put(KEY_CLASS, p.getJob());

        long id = pushData(TABLE_CHARACTER,p.getId(),c);
        Log.d(LOG," Character pushed and id is "+id);
        p.setId(id);
        deleteEntry(TABLE_TALENT,KEY_CHARACTER_ID,id);
        for(Talent tl:p.getTalents().keySet()){
            Log.d(LOG,"pushing talent "+ tl.getName());
            c = new ContentValues();
            c.put(KEY_CHARACTER_ID,id);
            c.put(KEY_NAME,tl.getName());
            c.put(KEY_VALUE,p.getTalents().get(tl));
            pushData(TABLE_TALENT,null,c);
        }

        deleteEntry(TABLE_ATTRIBUTE,KEY_CHARACTER_ID,id);
        for(Attribute attribute:p.getAttributes()){
            Log.d(LOG,"pushing attribute "+attribute.name());
            c = new ContentValues();
            c.put(KEY_CHARACTER_ID,id);
            c.put(KEY_NAME,attribute.name());
            c.put(KEY_VALUE,p.getAttr(attribute));
            pushData(TABLE_ATTRIBUTE,null,c);
        }
    	return id;
    }

    public synchronized Character getCharacter(Long id) throws Exception{
        db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHARACTER, new String[]{ KEY_ID, KEY_NAME, KEY_RACE}, KEY_ID + "=?",
                new String[]{String.valueOf(id) }, null,null,null,null);

        if(cursor.moveToFirst()){
            Log.d(LOG, "adding Pers to list with id:" + cursor.getLong(0) + " Name:" + cursor.getString(1));

            Character p = new Character(cursor.getString(1),cursor.getLong(0));
            p.setRasse(Race.valueOf(getString(cursor, KEY_RACE)));
            cursor.close();

            // Attributes
            cursor = db.query(TABLE_ATTRIBUTE, new String[]{KEY_CHARACTER_ID,KEY_NAME,KEY_VALUE},
                    KEY_CHARACTER_ID + "=?",new String[]{String.valueOf(id) }, null,null,null,null);
            if(cursor.moveToFirst()){
                do{
                    Log.d(LOG, "Adding attribute " + getString(cursor,KEY_NAME));
                    p.setAttr(Attribute.valueOf(getString(cursor, KEY_NAME)), get(cursor, KEY_VALUE));
                }while(cursor.moveToNext());
            }
            cursor.close();

            // Talents
            cursor = db.query(TABLE_TALENT, new String[]{KEY_CHARACTER_ID,KEY_NAME,KEY_VALUE},
                    KEY_CHARACTER_ID + "=?",new String[]{String.valueOf(id) }, null,null,null,null);
            if(cursor.moveToFirst()){
                do{
                    Log.d(LOG, "Adding talent " + getString(cursor,KEY_NAME));
                    p.setTalent(Talent.getTalent(getString(cursor, KEY_NAME)),get(cursor,KEY_VALUE));
                }while(cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return p;
        }else{
            cursor.close();
            db.close();
            throw(new Exception("Personnage not in the DataBase"));
        }
    }

    public synchronized ArrayList<Character> getAllCharacter(){
    	ArrayList<Character> list= new ArrayList<>();
    	db = getReadableDatabase();
    	Cursor cursor = db.query(TABLE_CHARACTER,new String[]{KEY_NAME,KEY_ID},null,null,null,null,null);

    	if(cursor.moveToFirst()){
    		do{
                Log.d(LOG,"adding character with id " + cursor.getLong(1));
    			list.add(new Character(cursor.getString(0),cursor.getLong(1)));
    		}while(cursor.moveToNext());
    	}
    	db.close();
    	cursor.close();
    	return list;
    }

    public synchronized void delete(Character p){
		 deleteEntry(SQLDBManager.TABLE_CHARACTER, p.getId());
	 }

    //////////// ITEMS  //////////////////////////////////

    public synchronized Long pushItem(Item w){
    	ContentValues c = new ContentValues();

        delete(w);
    	c.put(KEY_NAME,w.getName());
        c.put(KEY_SERVER_ID,w.getServerId());
        c.put(KEY_CHARACTER_ID,w.getOwner());
        c.put(KEY_DESCRIPTION,w.getDescription());
        c.put(KEY_WEIGHT,w.getWeight());
        Long id = pushData(TABLE_ITEM,w.getId(),c); // in case it is first time

        if(w instanceof UsableInterface){
            ActionData data;
            for(Action action:((UsableInterface) w).getActions()) {
                Log.d(LOG,"" +action);
                data = ((UsableInterface) w).getData(action);
                Log.d(LOG,"" +data);
                if(!data.isRemake) {
                    c = new ContentValues();
                    c.put(KEY_ITEM_ID, id);
                    c.put(KEY_NAME, action.getName());
                    c.put(KEY_ATTRIBUTES, data.getAttributesString());
                    c.put(KEY_FATIGUE, data.fatigue);
                    c.put(KEY_TICKS,data.ticks);
                    c.put(KEY_SKILL_ENHANCER, data.enhancer);
                    c.put(KEY_RESULT, data.getResult());
                    pushData(TABLE_ATTRIBUTE_ACTION, null, c);
                }
            }
        }

        if(w instanceof Weapon){
            c = new ContentValues();
            c.put(KEY_ITEM_ID,id);
            c.put(KEY_TYPE,((Weapon) w).getTyp().name());
            pushData(TABLE_USABLE,null,c);
        }

        if(w instanceof Clothe){
            c = new ContentValues();
            c.put(KEY_ITEM_ID,id);
            c.put(KEY_BODYPART,((Clothe) w).getPart().name());
            c.put(KEY_WHEATER_PROTECTION,((Clothe) w).getWeatherProtection());
            c.put(KEY_PROTECTION,((Clothe) w).getProtection());
            pushData(TABLE_CLOTHE,null,c);
        }

        return id;
    }
    
	public synchronized ArrayList<Item> getAllItem(Long ownerId) {
		ArrayList<Item> list= new ArrayList<>();
    	db = getReadableDatabase();
    	Cursor cursor = db.query(TABLE_ITEM, new String[]{KEY_ID, KEY_NAME, KEY_SERVER_ID, KEY_CHARACTER_ID, KEY_DESCRIPTION, KEY_WEIGHT},
                null,null, null, null, null, null);

        Item item;
        Cursor cSub;
    	if(cursor.moveToFirst()){
    		do{
    			Log.d(LOG,"adding item to list with id:"+cursor.getLong(0)+" Name:"+cursor.getString(1));
                item = new Item(getLong(cursor,KEY_ID),getLong(cursor,KEY_SERVER_ID),getLong(cursor,KEY_CHARACTER_ID),getString(cursor,KEY_NAME),
                        getString(cursor,KEY_DESCRIPTION),get(cursor,KEY_WEIGHT));

                cSub = db.query(TABLE_USABLE, new String[]{KEY_TYPE,KEY_ITEM_ID},KEY_ITEM_ID +"=?",
                        new String[]{String.valueOf(getLong(cursor,KEY_ID))},null,null,null,null);
                if(cSub.moveToFirst())
                    item = new Weapon(item,UsableType.valueOf(getString(cSub, KEY_TYPE)));
                cSub.close();
                cSub = db.query(TABLE_CLOTHE, new String[]{KEY_ITEM_ID, KEY_WHEATER_PROTECTION, KEY_PROTECTION, KEY_BODYPART}, KEY_ITEM_ID +"=?",
                        new String[]{String.valueOf(getLong(cursor,KEY_ID))},null,null,null,null);
                if(cSub.moveToFirst())
                    item = new Clothe(item, HitZone.valueOf(getString(cSub, KEY_BODYPART)),get(cSub,KEY_PROTECTION),get(cSub,KEY_WHEATER_PROTECTION));
                cSub.close();

                if(item instanceof Weapon){
                    cSub = db.query(TABLE_ATTRIBUTE_ACTION,new String[]{KEY_ITEM_ID,KEY_NAME,KEY_ATTRIBUTES,KEY_FATIGUE,KEY_TICKS,KEY_SKILL_ENHANCER,KEY_MODIFIER,KEY_RESULT},
                            KEY_ITEM_ID+"=?",new String[]{String.valueOf(getLong(cursor,KEY_ID))},null,null,null,null);
                    if(cSub.moveToFirst()){
                        do{
                            ((Weapon) item).addData(Action.valueOf(getString(cSub, KEY_NAME)),
                                    new ActionData(Action.valueOf(getString(cSub, KEY_NAME)),get(cSub, KEY_FATIGUE), get(cSub, KEY_SKILL_ENHANCER), get(cSub, KEY_MODIFIER), get(cSub, KEY_TICKS),getString(cSub,KEY_ATTRIBUTES), getString(cSub, KEY_RESULT)));
                        }while (cSub.moveToNext());
                    }
                }
                list.add(item);
            }while(cursor.moveToNext());
    	}
        cursor.close();
        db.close();
    	return list;
	}

    public synchronized Item getItem(Long id) {
        db = getReadableDatabase();
        Cursor cursor;
        if(id != null)
        cursor = db.query(TABLE_ITEM, new String[]{KEY_ID, KEY_NAME, KEY_SERVER_ID, KEY_CHARACTER_ID, KEY_DESCRIPTION, KEY_WEIGHT},
                KEY_ID +"=?",new String[]{String.valueOf(id)}, null, null, null, null);
        else
            cursor = db.query(TABLE_ITEM, new String[]{KEY_ID, KEY_NAME, KEY_SERVER_ID, KEY_CHARACTER_ID, KEY_DESCRIPTION, KEY_WEIGHT},
                    null,null, null, null, null, null);

        Item item = null;
        Cursor cSub;
        if(cursor.moveToFirst()){

            Log.d(LOG,"adding item to list with id:"+cursor.getLong(0)+" Name:"+cursor.getString(1));
            item = new Item(getLong(cursor,KEY_ID),getLong(cursor,KEY_SERVER_ID),getLong(cursor,KEY_CHARACTER_ID),getString(cursor,KEY_NAME),
                    getString(cursor,KEY_DESCRIPTION),get(cursor,KEY_WEIGHT));

            cSub = db.query(TABLE_USABLE, new String[]{KEY_TYPE,KEY_ITEM_ID},KEY_ITEM_ID +"=?",
                    new String[]{String.valueOf(getLong(cursor,KEY_ID))},null,null,null,null);
            if(cSub.moveToFirst())
                item = new Weapon(item,UsableType.valueOf(getString(cSub, KEY_TYPE)));
            cSub.close();
            cSub = db.query(TABLE_CLOTHE, new String[]{KEY_ITEM_ID, KEY_WHEATER_PROTECTION, KEY_PROTECTION, KEY_BODYPART}, KEY_ITEM_ID +"=?",
                    new String[]{String.valueOf(getLong(cursor,KEY_ID))},null,null,null,null);
            if(cSub.moveToFirst())
                item = new Clothe(item, HitZone.valueOf(getString(cSub, KEY_BODYPART)),get(cSub,KEY_PROTECTION),get(cSub,KEY_WHEATER_PROTECTION));
            cSub.close();

            if(item instanceof Weapon){
                cSub = db.query(TABLE_ATTRIBUTE_ACTION,new String[]{KEY_ITEM_ID,KEY_NAME,KEY_ATTRIBUTES,KEY_FATIGUE,KEY_TICKS,KEY_SKILL_ENHANCER,KEY_MODIFIER,KEY_RESULT},
                        KEY_ITEM_ID+"=?",new String[]{String.valueOf(getLong(cursor,KEY_ID))},null,null,null,null);
                if(cSub.moveToFirst()){
                    do{
                        ((Weapon) item).addData(Action.valueOf(getString(cSub, KEY_NAME)),
                                new ActionData(Action.valueOf(getString(cSub, KEY_NAME)),get(cSub,KEY_FATIGUE), get(cSub,KEY_SKILL_ENHANCER),get(cSub,KEY_MODIFIER), get(cSub,KEY_TICKS),getString(cSub,KEY_ATTRIBUTES),getString(cSub,KEY_RESULT)));
                    }while (cSub.moveToNext());
                }
            }
        }
        cursor.close();
        db.close();
        return item;
    }

    public synchronized void delete(Item item) {
        deleteEntry(TABLE_ITEM, item.getId());
		deleteEntry(TABLE_USABLE, KEY_ITEM_ID, item.getId());
        deleteEntry(TABLE_SPELL,KEY_ITEM_ID, item.getId());
        deleteEntry(TABLE_CLOTHE,KEY_ITEM_ID, item.getId());
        deleteEntry(TABLE_ATTRIBUTE_ACTION,KEY_ITEM_ID, item.getId());
    }

    // State
    //TODO


	// Help methods
	
	private int get(Cursor c,String key){
		return c.getInt(c.getColumnIndexOrThrow(key));
	}

    private boolean getBool(Cursor c, String key) {
        return c.getInt(c.getColumnIndex(key))==1;
    }

    private long getLong(Cursor c, String key) {
        return c.getLong(c.getColumnIndexOrThrow(key));
    }

    private String getString(Cursor c,String key){
		return c.getString(c.getColumnIndexOrThrow(key));
	}
	
	private long pushData(String table,Long id, ContentValues values){
	    	db = getWritableDatabase();
			if(id == null){
				Log.d(LOG,"inserting ");
				id=db.insert(table, null,values);
			}else{
				Log.d(LOG,"updating ");
				db.update(table, values, KEY_ID+" = ?", new String[]{String.valueOf(id)});
			}
	    	db.close();
	    	return id;
	    }
	    
	private void deleteEntry(String table,Long id){
		deleteEntry(table,KEY_ID,id);
	}

    private void deleteEntry(String table,String key_column,Long id){
        if(id != null)
            getWritableDatabase().delete(table,key_column + " = ?",
                new String[]{String.valueOf(id)});
    }
}
