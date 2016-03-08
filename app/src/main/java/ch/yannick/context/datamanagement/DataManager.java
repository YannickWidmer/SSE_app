package ch.yannick.context.datamanagement;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.RootApplication;
import ch.yannick.intern.items.Clothe;
import ch.yannick.intern.items.Item;
import ch.yannick.intern.personnage.Character;
import ch.yannick.intern.usables.UsableType;
import ch.yannick.intern.usables.Weapon;

public class DataManager {

    private SQLDBManager db;
    private RootApplication root;

    private String LOG = "DATAMANAGER";


    public DataManager(RootApplication context) {
        db = new SQLDBManager(context);
        root = context;
    }

    public ArrayList<Character> getAllCharacter() {
        // returns just name and ids
        return db.getAllCharacter();
    }

    public Character getCharacter(Long id) {
        Log.d(LOG, "Looking for character with id " + id);
        try {
            return db.getCharacter(id);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public void newCharacter(String name) {
        AsyncInsert as = new AsyncInsert(new Character(name, null));
        as.execute();
    }

    public void newUsable(String name, UsableType type) {
        Log.d(LOG, "new Usable " + name + " type " + type.name());
        AsyncInsert as = new AsyncInsert(new Weapon( type,name));
        as.execute();
    }

    public void newClothe(String name) {
        Log.d(LOG, "new clothe " + name);
        AsyncInsert as = new AsyncInsert(new Clothe(null, name));
        as.execute();
    }


    public void delete(Character p) {
        db.delete(p);
    }

    public void delete(Item w) {
        db.delete(w);
    }


    public ArrayList<Item> getAllItem(Long ownerId) {
        //returns just the name, ids and types
        return db.getAllItem(ownerId);
    }

    public Item getItem(Long id){
        return db.getItem(id);
    }

    public void pushPersonnage(Character p) { // add or update personnage
        new AsyncInsert(p).execute();
    }

    public void pushItem(Item item) {
        new AsyncInsert(item).execute();
    }

    public ArrayList<Weapon> getAllWeapon() {
        List<Item> list = db.getAllItem(null);
        ArrayList<Weapon> res = new ArrayList<>();
        for(Item it :list){
            if(it instanceof  Weapon)
                res.add((Weapon)it);
        }
        return res;
    }

    //////////// ASYNCQUERYS ////////////////////////////////////


    private class AsyncInsert extends AsyncTask<Void, Void, Long> {
        private Character p;
        private Item item;
        private int flag;

        public AsyncInsert(Character p) {
            super();
            this.p = p;
            if (p.getId() == null) {
                this.flag = MyBaseActivity.INSERTPERSONNAGE;
            } else {
                this.flag = MyBaseActivity.UPDATEPERSONNAGE;
            }
            Log.d(LOG, "Pushing Personnage, id:" + p.getId());
        }

        public AsyncInsert(Item item) {
            super();
            this.item = item;
            if (item.getId() == null) {
                this.flag = MyBaseActivity.INSERTWEAPON;
            } else {
                this.flag = MyBaseActivity.UPDATEWEAPON;
            }
            Log.d(LOG, "Pushing item, id:" + item.getId());
        }


        @Override
        protected Long doInBackground(Void... params) {
            switch (flag) {
                case (MyBaseActivity.INSERTPERSONNAGE):
                case (MyBaseActivity.UPDATEPERSONNAGE):
                    Log.d(LOG, "Insert character " + p.toString());
                    return db.pushCharacter(p);
                case (MyBaseActivity.INSERTWEAPON):
                case (MyBaseActivity.UPDATEWEAPON):
                    Log.d(LOG, "Insert item " + item.toString());
                    return db.pushItem(item);
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
