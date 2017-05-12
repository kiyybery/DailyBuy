package izhipeng.dailybuy.bean;

/**
 * Created by Administrator on 2016/10/27 0027.
 */
public class AddressBean {

    public int id;
    public String address;
    public String name;
    public String pNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getpNum() {
        return pNum;
    }

    public void setpNum(String pNum) {
        this.pNum = pNum;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", pNum='" + pNum + '\'' +
                '}';
    }
}
