package ch.yannick.intern.items;

/**
 * Created by Yannick on 23.02.2016.
 */
public class Item {
    private Long mId, mServerId, mOwnerId;
    private String mName, mDescription;
    protected int mWeight;

    public Item(Item item){
        this.mId = item.mId;
        this.mServerId = item.mServerId;
        this.mOwnerId = item.mOwnerId;
        this.mName = item.mName;
        this.mDescription = item.mDescription;
        this.mWeight = item.mWeight;
    }

    public Item(Long id, Long serverId, Long ownerId,  String name, String description, int weight) {
        this.mId = id;
        this.mServerId = serverId;
        this.mOwnerId = ownerId;
        this.mName = name;
        this.mDescription = description;
        this.mWeight = weight;
    }

    public Long getServerId() {
        return mServerId;
    }

    public String getName() {
        return mName;
    }

    @Override
    public String toString(){
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int w){
        mWeight = w;
    }

    public Long getId() {
        return mId;
    }

    public Long getOwner() {
        return mOwnerId;
    }
}
