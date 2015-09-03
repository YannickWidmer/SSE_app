package ch.yannick.context.datamanagement;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.RootApplication;
import ch.yannick.intern.items.WaffenTyp;
import ch.yannick.intern.items.Armor;
import ch.yannick.intern.personnage.Personnage;
import ch.yannick.intern.items.Weapon;

public class DataManager {

    private SQLDBManager db;
    private RootApplication root;

    private String LOG = "DATAMANAGER";


    public DataManager(RootApplication context) {
        db = new SQLDBManager(context);
        root = context;
    }

    public ArrayList<Personnage> getAllPersonnage() {
        // returns just name and ids
        return db.getAllPersonnage();
    }

    public Personnage getPersonnage(Long id) {
        Log.d(LOG, "Looking for personnage with id " + id);
        try {
            return db.getPersonnage(id);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public void newPersonnage(String name) {
        AsyncInsert as = new AsyncInsert(new Personnage(name, null));
        as.execute();
    }

    public void newWeapon(String name, WaffenTyp type) {
        Log.d(LOG, "new Weapon " + name + " type " + type.name());
        AsyncInsert as = new AsyncInsert(new Weapon(null, name, type));
        as.execute();
    }

    public void newArmor(String name) {
        Log.d(LOG, "new armor " + name);
        AsyncInsert as = new AsyncInsert(new Armor(null, name));
        as.execute();
    }


    public void delete(Personnage p) {
        db.delete(p);
    }

    public void delete(Weapon w) {
        db.delete(w);
    }

    public void delete(Armor armor) {
        db.delete(armor);
    }

    public ArrayList<Weapon> getAllWeapon() {
        //returns just the name, ids and types
        return db.getAllWeapon();
    }

    public Weapon getWeapon(Long id) throws Exception {
        Log.d(LOG, "Looking for weapon with id " + id);
        return db.getWeapon(id);

    }

    public ArrayList<Armor> getAllArmor() {
        return db.getAllArmor();
    }

    public Armor getArmor(Long id) throws Exception{
        Log.d(LOG, "Looking for armor with id " + id);
        return db.getArmor(id);
    }

    public void pushPersonnage(Personnage p) { // add or update personnage
        new AsyncInsert(p).execute();
    }

    public void pushWeapon(Weapon w) {
        new AsyncInsert(w).execute();
    }

    public void pushArmor(Armor arm){
        new AsyncInsert(arm).execute();
    }


    //////////// ASYNCQUERYS ////////////////////////////////////


    private class AsyncInsert extends AsyncTask<Void, Void, Long> {
        private Personnage p;
        private Weapon w;
        private Armor arm;
        private int flag;

        public AsyncInsert(Personnage p) {
            super();
            this.p = p;
            if (p.getId() == null) {
                this.flag = MyBaseActivity.INSERTPERSONNAGE;
            } else {
                this.flag = MyBaseActivity.UPDATEPERSONNAGE;
            }
            Log.d(LOG, "Pushing Personnage, id:" + p.getId());
        }

        public AsyncInsert(Weapon w) {
            super();
            this.w = w;
            if (w.getId() == null) {
                this.flag = MyBaseActivity.INSERTWEAPON;
            } else {
                this.flag = MyBaseActivity.UPDATEWEAPON;
            }
            Log.d(LOG, "Pushing Weapon, id:" + w.getId());
        }

        public AsyncInsert(Armor a) {
            super();
            this.arm = a;
            if (arm.getId() == null) {
                this.flag = MyBaseActivity.INSERTARMOR;
            } else {
                this.flag = MyBaseActivity.UPDATEARMOR;
            }
            Log.d(LOG, "Pushing Armor, id:" + arm.getId());
        }

        @Override
        protected Long doInBackground(Void... params) {
            switch (flag) {
                case (MyBaseActivity.INSERTPERSONNAGE):
                case (MyBaseActivity.UPDATEPERSONNAGE):
                    Log.d(LOG, "Insert personnage " + p.toString());
                    return db.pushPersonnage(p);
                case (MyBaseActivity.INSERTWEAPON):
                case (MyBaseActivity.UPDATEWEAPON):
                    Log.d(LOG, "Insert weapon " + w.toString());
                    return db.pushWeapon(w);
                case (MyBaseActivity.INSERTARMOR):
                case (MyBaseActivity.UPDATEARMOR):
                    Log.d(LOG, "Insert armor " + arm.toString());
                    return db.pushArmor(arm);
                default:
                    Log.w(LOG, " AsyncInsert got unattended Flag");
                    return (long) -1;// should never happen
            }
        }

        @Override
        protected void onPostExecute(Long id) {
            Log.d(LOG, "onPostExecute");
            root.react(String.valueOf(id), flag, id);
        }
    }
}
