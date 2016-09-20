package izhipeng.dailybuy.bean;

import java.util.List;

/**
 * Created by xyb on 16/9/19.
 */
public class SignTimeList {

    /**
     * info : 获取信息成功
     * ret : 1
     * signTimeList : [1473836627000,1474267437000]
     */

    private String info;
    private int ret;
    private List<Long> signTimeList;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public List<Long> getSignTimeList() {
        return signTimeList;
    }

    public void setSignTimeList(List<Long> signTimeList) {
        this.signTimeList = signTimeList;
    }
}
