package izhipeng.dailybuy.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class MyPublish implements Serializable {

    public String img;
    public String title;
    public String c_time;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getC_time() {
        return c_time;
    }

    public void setC_time(String c_time) {
        this.c_time = c_time;
    }
}
