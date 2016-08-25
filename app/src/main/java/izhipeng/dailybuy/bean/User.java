package izhipeng.dailybuy.bean;


import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class User implements Serializable {

    public String id;
    public String uPhone;
    public String uPwd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public String getuPwd() {
        return uPwd;
    }

    public void setuPwd(String uPwd) {
        this.uPwd = uPwd;
    }
}
