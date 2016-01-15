package ch.yannick.context.datamanagement;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.SparseIntArray;

import java.util.ArrayList;

import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.dice.Dice;
import ch.yannick.intern.items.Armor;
import ch.yannick.intern.usables.UsableType;
import ch.yannick.intern.usables.Weapon;
import ch.yannick.intern.personnage.Attribute;
import ch.yannick.intern.personnage.HitZone;
import ch.yannick.intern.personnage.Personnage;
import ch.yannick.intern.personnage.Race;

public class SQLDBManager extends SQLiteOpenHelper {

	private static final String LOG="SQLManager";
	
	  // All Static variables related to db
    // Database Version
    private static final int DATABASE_VERSION = 8;
 
    // Database Name
    private static final String DATABASE_NAME = "SSE";
 
    // table names
    private static final String
            TABLE_PERSONAGE = "personages";
    private static final String TABLE_TALENTS = "talents";
    private static final String TABLE_WEAPONS="weapons";
    private static final String TABLE_SCHADEN="resultValue";
    private static final String TABLE_WEAPON_ACTION="weapon_action";
    private static final String TABLE_ARMOR = "armor";
 
    //Table columns for all tables
    private static final String KEY_NAME = "name", KEY_ID="id";
    
    // Personage Table Columns names
    private static final String  KEY_KA = "force", KEY_GK = "agility", KEY_GW = "speed",
    		KEY_SZ = "saga", KEY_CH = "charme", KEY_SS = "Acuity", KEY_KB = "constitution", KEY_AU = "endurance",
            KEY_WI = "will", KEY_MA = "magic", KEY_RACE = "race";

    // weapon Table Columns names
    private static final String KEY_TYPE= "type";
    		
	// Schaden Table Columns
    private static final String KEY_VALUE="value",KEY_ACTION = "action";

	// Weapon action Table Columns
    private static final String KEY_ATTRIBUTE1="first_attribute",
            KEY_ATTRIBUTE2="second_attribute",KEY_WEIGHT="weight",
            KEY_FATIGUE="fatigue",  KEY_PENETRATION="penetration", KEY_DIRECT="isDirect",
            KEY_SCHADEN="resultValue", KEY_ENHANCER = "enhancer";

    // armor table columns
    private static final String KEY_BODYPART="body_part", KEY_PROTECTION="protection", KEY_HEAT = "heat_protection";

    private SQLiteDatabase db;
   
    public SQLDBManager(RootApplication context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PERS_TABLE = "CREATE TABLE " + TABLE_PERSONAGE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_RACE + " STRING,"
                + KEY_KA + " INTEGER,"
                + KEY_GK + " INTEGER,"
                + KEY_GW + " INTEGER,"
                + KEY_SZ + " INTEGER,"
                + KEY_CH + " INTEGER,"
                + KEY_SS + " INTEGER,"
                + KEY_KB + " INTEGER,"
                + KEY_AU + " INTEGER,"
                + KEY_WI + " INTEGER,"
                + KEY_MA + " INTEGER" 
                + ");";

        String CREATE_TALENTS_TABLE=" CREATE TABLE "+ TABLE_TALENTS+"("
                + KEY_ID + " LONG,"
                + KEY_NAME + " STRING,"
                + KEY_VALUE + " INTEGER,"
                + " FOREIGN KEY ("+KEY_ID+") REFERENCES "+TABLE_PERSONAGE+" ("+KEY_ID+") ON DELETE CASCADE);";

        String CREATE_WEAPONS_TABLE=" CREATE TABLE "+ TABLE_WEAPONS+"("
        		+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT,"
        		+ KEY_TYPE +" STRING,"
                + KEY_WEIGHT +" INTEGER"+");";
        		
        String CREATE_SCHADEN_TABLE=" CREATE TABLE "+ TABLE_SCHADEN+"("
        		+ KEY_ID + " LONG,"
        		+ KEY_NAME + " STRING,"
                + KEY_ACTION + " STRING, "
        		+ " FOREIGN KEY ("+KEY_ID+") REFERENCES "+TABLE_WEAPONS+" ("+KEY_ID+") ON DELETE CASCADE);";

        String CREATE_WEAPON_ACTION_TABLE=" CREATE TABLE "+TABLE_WEAPON_ACTION+" ("
                + KEY_ID + " LONG,"
                + KEY_NAME + " STRING,"
                + KEY_ATTRIBUTE1 + " STRING,"
                + KEY_ATTRIBUTE2 + " STRING,"
                + KEY_ENHANCER + " INTEGER,"
                + KEY_FATIGUE + " INTEGER,"
                + KEY_PENETRATION +" INTEGER,"
                + KEY_SCHADEN+" INTEGER,"
                + KEY_DIRECT +" BOOLEAN,"
                + " FOREIGN KEY ("+KEY_ID+") REFERENCES "+TABLE_WEAPONS+" ("+KEY_ID+") ON DELETE CASCADE);";

        String CREATE_ARMOR_TABLE=" CREATE TABLE "+ TABLE_ARMOR+"("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT,"
                + KEY_WEIGHT + " INTEGER,"
                + KEY_HEAT + " INTEGER,"
                + KEY_BODYPART + " STRING,"
                + KEY_PROTECTION + " INTEGER);";

        db.execSQL(CREATE_PERS_TABLE);
        db.execSQL(CREATE_TALENTS_TABLE);
        db.execSQL(CREATE_WEAPONS_TABLE);
        db.execSQL(CREATE_SCHADEN_TABLE);
        db.execSQL(CREATE_WEAPON_ACTION_TABLE);
        db.execSQL(CREATE_ARMOR_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TALENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEAPONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHADEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEAPON_ACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARMOR);
        // Create tables again
        onCreate(db);
    }
    
    
    //////////// PERSONAGES //////////////////////////////////

    public synchronized Long pushPersonnage(Personnage p){ // add or update personnage
    	SparseIntArray attr = p.getAttr();
    	ContentValues c = new ContentValues();
    	c.put(KEY_NAME, p.toString());
    	c.put(KEY_AU, ""+attr.get(R.id.KEY_AU));
    	c.put(KEY_CH, ""+attr.get(R.id.KEY_CH));
    	c.put(KEY_GK, ""+attr.get(R.id.KEY_GK));
    	c.put(KEY_GW, ""+attr.get(R.id.KEY_GW));
    	c.put(KEY_KA, ""+attr.get(R.id.KEY_KA));
    	c.put(KEY_KB, ""+attr.get(R.id.KEY_KB));
        c.put(KEY_WI, ""+attr.get(R.id.KEY_WI));
        c.put(KEY_MA, ""+attr.get(R.id.KEY_MA));
    	c.put(KEY_SS, ""+attr.get(R.id.KEY_SS));
    	c.put(KEY_SZ, ""+attr.get(R.id.KEY_SZ));
        c.put(KEY_RACE, p.getRasse().name());

        long id = pushData(TABLE_PERSONAGE,p.getId(),c);
        deleteEntry(TABLE_TALENTS,id);
        for(Talent tl:p.getTalents().keySet()){
            Log.d(LOG,"pushing talent "+Talent.getName(tl));
            c = new ContentValues();
            c.put(KEY_ID,id);
            c.put(KEY_NAME,Talent.getName(tl));
            c.put(KEY_VALUE,p.getTalents().get(tl));
            pushData(TABLE_TALENTS,null,c);
        }

    	return id;
    }
    
    public synchronized ArrayList<Personnage> getAllPersonnage(){
    	ArrayList<Personnage> list= new ArrayList<Personnage>();
    	db = getReadableDatabase();
    	Cursor cursor = db.query(TABLE_PERSONAGE,new String[]{KEY_NAME,KEY_ID},null,null,null,null,null);
    	
    	if(cursor.moveToFirst()){
    		do{
    			list.add(new Personnage(cursor.getString(0),cursor.getLong(1)));
    		}while(cursor.moveToNext());
    	}
    	db.close();
    	cursor.close();
    	return list;
    }
    
    public synchronized Personnage getPersonnage(Long id) throws Exception{
    	db = getReadableDatabase();
    	Cursor cursor = db.query(TABLE_PERSONAGE, new String[]{ KEY_ID, KEY_NAME, KEY_RACE, KEY_KA, KEY_GK,KEY_GW, KEY_SZ, KEY_CH, KEY_SS, KEY_KB, KEY_AU, KEY_WI, KEY_MA}, KEY_ID + "=?",
    			new String[]{String.valueOf(id) }, null,null,null,null);
 
    	if(cursor.moveToFirst()){
			Log.d(LOG,"adding Pers to list with id:"+cursor.getLong(0)+" Name:"+cursor.getString(1));
			Log.d(LOG, "Attrs" + get(cursor, KEY_KA) + get(cursor, KEY_GK) + get(cursor, KEY_GW) + get(cursor, KEY_SZ) +
                    get(cursor, KEY_CH) + get(cursor, KEY_SS) + get(cursor, KEY_KB) + get(cursor, KEY_AU) +get(cursor,KEY_WI)+ get(cursor, KEY_MA));
				 
    		Personnage p = new Personnage(cursor.getString(1),cursor.getLong(0));
    		p.setAttr(get(cursor,KEY_KA),get(cursor,KEY_GK),get(cursor,KEY_GW),get(cursor,KEY_SZ),
    				get(cursor,KEY_CH),get(cursor,KEY_SS),get(cursor,KEY_KB),get(cursor,KEY_AU),get(cursor,KEY_WI),get(cursor,KEY_MA));
            p.setRasse(Race.valueOf(getString(cursor,KEY_RACE)));
    		cursor.close();

            // Talents
            cursor = db.query(TABLE_TALENTS, new String[]{KEY_ID,KEY_NAME,KEY_VALUE},
                    KEY_ID + "=?",new String[]{String.valueOf(id) }, null,null,null,null);
            Log.d(LOG, cursor.toString());
            if(cursor.moveToFirst()){
                do{
                    Log.d(LOG, "Adding talent" + getString(cursor,KEY_NAME));
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
    
    public synchronized void delete(Personnage p){
		 deleteEntry(SQLDBManager.TABLE_PERSONAGE,p.getId());
	 }

    //////////// WEAPONS  //////////////////////////////////

    public synchronized Long pushWeapon(Weapon w){
    	ContentValues c = new ContentValues();
    	
    	// The Weapon
    	c.put(KEY_NAME,w.toString());
    	c.put(KEY_TYPE, ""+w.getType().name());
    	c.put(KEY_WEIGHT,""+w.getWeight());
    	Long id = pushData(TABLE_WEAPONS,w.getId(),c); // in case it is first time
    	// Damage dices
    	deleteEntry(TABLE_SCHADEN,id);
        for(Action action:w.getBase_actions()){
            if(action.isAttack()){
                for(Dice dice:w.getData(action).resultDice) {
                    c = new ContentValues();
                    c.put(KEY_ID,id);
                    c.put(KEY_ACTION,action.name());
                    c.put(KEY_NAME,dice.name());
                    pushData(TABLE_SCHADEN,null,c);
                }
            }
        }

        // Actions
        deleteEntry(TABLE_WEAPON_ACTION,id);
        for(Action action:w.getType().getActions()){
            Weapon.ActionData actionData = w.getData(action);
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_ID,id);
            contentValues.put(KEY_NAME,action.name());
            contentValues.put(KEY_ATTRIBUTE1,actionData.firstAttribute.name());
            contentValues.put(KEY_ATTRIBUTE2, actionData.secondAttribute.name());
            contentValues.put(KEY_ENHANCER,actionData.enhancer);
            contentValues.put(KEY_FATIGUE, actionData.fatigue);
            contentValues.put(KEY_PENETRATION,actionData.penetration);
            contentValues.put(KEY_DIRECT,actionData.isDirect);
            contentValues.put(KEY_SCHADEN,actionData.schaden);
            pushData(TABLE_WEAPON_ACTION, null, contentValues);
        }
    	return id;
    }
    
	public synchronized ArrayList<Weapon> getAllWeapon() {
		ArrayList<Weapon> list= new ArrayList<Weapon>();
    	db = getReadableDatabase();
    	Cursor cursor = db.query(TABLE_WEAPONS, new String[]{KEY_ID, KEY_NAME, KEY_TYPE}, null, null, null, null, null);
    	
    	if(cursor.moveToFirst()){
    		do{
    			Log.d(LOG,"adding Weapon to list with id:"+cursor.getLong(0)+" Name:"+cursor.getString(1)+" type:"+ cursor.getInt(2));
                list.add(new Weapon(cursor.getLong(0), cursor.getString(1), UsableType.valueOf(cursor.getString(2))));
    		}while(cursor.moveToNext());
    	}

        // To lazy to do the rest but I only need this function when showing lists and reaload the weapon afterwards
    	db.close();
    	cursor.close();
    	return list;
	}

	public synchronized Weapon getWeapon(Long id) throws Exception {
		Weapon w;
    	db = getReadableDatabase();
    	Cursor c = db.query(TABLE_WEAPONS, new String[]{KEY_ID,KEY_NAME,KEY_TYPE,KEY_WEIGHT},
    			KEY_ID + "=?",new String[]{String.valueOf(id) }, null,null,null,null);
    	if(c.moveToFirst()){
			Log.d(LOG,"The result of the query is (name="+c.getString(1)+"/id:"+ c.getInt(0)+"/type:"+c.getString(2)+"weight"+
					c.getInt(3)+")");


            w =  new Weapon(getLong(c,KEY_ID),getString(c,KEY_NAME), UsableType.valueOf(getString(c, KEY_TYPE)));
            w.setWeight(get(c,KEY_WEIGHT));
			c.close();

            // Schaden
            c = db.query(TABLE_SCHADEN, new String[]{KEY_ID,KEY_NAME,KEY_ACTION},
				KEY_ID + "=?",new String[]{String.valueOf(id) }, null,null,null,null);
	    	Log.d(LOG,c.toString());
	    	if(c.moveToFirst()){
	    		do{
	    			Log.d(LOG,"Adding dice" + get(c,KEY_NAME));
	    			w.addDice(Action.valueOf(getString(c, KEY_ACTION)), Dice.valueOf(getString(c, KEY_NAME)));
	    		}while(c.moveToNext());
	    	}   		
	    	c.close();

            //Actions
            c = db.query(TABLE_WEAPON_ACTION, new String[]{KEY_ID,KEY_NAME,KEY_ATTRIBUTE1,KEY_ATTRIBUTE2,KEY_ENHANCER,KEY_FATIGUE
                    ,KEY_SCHADEN,KEY_PENETRATION,KEY_DIRECT},
                    KEY_ID + "=?",new String[]{String.valueOf(id) }, null,null,null,null);
            Log.d(LOG,c.toString());
            if(c.moveToFirst()){
                do{
                    Log.d(LOG, "Adding action " + getString(c, KEY_NAME)
                            + " (" + getString(c, KEY_ATTRIBUTE1) + "," + getString(c, KEY_ATTRIBUTE2) + ","
                            + get(c, KEY_ENHANCER) + "," + get(c, KEY_FATIGUE));
                    Action  action =Action.valueOf(getString(c, KEY_NAME));
                    Weapon.ActionData actionData = w.getData(action);
                    // Damage are set allready
                    actionData.firstAttribute = Attribute.valueOf(getString(c,KEY_ATTRIBUTE1));
                    actionData.secondAttribute = Attribute.valueOf(getString(c,KEY_ATTRIBUTE2));
                    actionData.enhancer =  get(c,KEY_ENHANCER);
                    actionData.fatigue = get(c,KEY_FATIGUE);
                    if(action.isAttack()){
                        actionData.schaden = get(c,KEY_SCHADEN);
                        actionData.penetration = get(c,KEY_PENETRATION);
                        actionData.isDirect = getBool(c,KEY_DIRECT);
                    }
                }while(c.moveToNext());
            }
            c.close();

	    	db.close();
	    	return w;
   		}else { throw(new Exception("Weapon not in the Data Base"));}
   	}



    public synchronized void delete(Weapon w) {
        deleteEntry(SQLDBManager.TABLE_WEAPONS, w.getId());
		deleteEntry(SQLDBManager.TABLE_SCHADEN, w.getId());
	}


    //////////// ARMOR  //////////////////////////////////

    public synchronized Long pushArmor(Armor armor){
        ContentValues c = new ContentValues();

        // The Weapon
        c.put(KEY_NAME,armor.toString());
        c.put(KEY_BODYPART, ""+armor.getPart().name());
        c.put(KEY_WEIGHT, "" + armor.getWeight());
        c.put(KEY_HEAT,""+armor.getWeatherProtection());
        c.put(KEY_PROTECTION,""+armor.getProtection());
        return pushData(TABLE_ARMOR,armor.getId(),c); // in case it is first time
    }

    public synchronized ArrayList<Armor> getAllArmor(){
        ArrayList<Armor> list= new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.query(TABLE_ARMOR,new String[]{KEY_ID, KEY_NAME, KEY_PROTECTION,KEY_HEAT,KEY_WEIGHT,KEY_BODYPART},null,null,null,null,null);
        Armor temp;
        if(c.moveToFirst()){
            do{
                temp = new Armor(getLong(c,KEY_ID),getString(c,KEY_NAME), HitZone.valueOf(getString(c, KEY_BODYPART)),get(c, KEY_PROTECTION),get(c, KEY_WEIGHT));
                temp.setWeatherProtection(get(c,KEY_HEAT));
                list.add(temp);
            }while(c.moveToNext());
        }
        db.close();
        c.close();
        return list;
    }

    public synchronized Armor getArmor(Long id) throws Exception{
        db = getReadableDatabase();
        Cursor c = db.query(TABLE_ARMOR, new String[]{ KEY_ID, KEY_NAME, KEY_PROTECTION,KEY_HEAT,KEY_WEIGHT,KEY_BODYPART}, KEY_ID + "=?",
                new String[]{String.valueOf(id) }, null,null,null,null);

        if(c.moveToFirst()){

            Armor armor = new Armor(getLong(c,KEY_ID),getString(c,KEY_NAME), HitZone.valueOf(getString(c, KEY_BODYPART)),get(c,KEY_PROTECTION),get(c,KEY_WEIGHT));
            armor.setWeatherProtection(get(c,KEY_HEAT));
            c.close();
            db.close();
            return armor;
        }else{
            c.close();
            db.close();
            throw(new Exception("Armor not in the DataBase"));
        }
    }

    public synchronized void delete(Armor amor){
        deleteEntry(SQLDBManager.TABLE_ARMOR,amor.getId());
    }

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
		getWritableDatabase().delete(table,KEY_ID + " = ?",
				new String[]{String.valueOf(id)});
	}
}
