package izhipeng.dailybuy.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class MyPrefire implements Serializable {

    public int img;
    public String text;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
