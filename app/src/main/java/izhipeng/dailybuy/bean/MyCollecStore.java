package izhipeng.dailybuy.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/17 0017.
 */
public class MyCollecStore implements Serializable{

    public int favorId;
    public int storeId;
    public String storeImage;
    public String storeName;

    public int getFavorId() {
        return favorId;
    }

    public void setFavorId(int favorId) {
        this.favorId = favorId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
