package izhipeng.dailybuy.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class SerachContentBean implements Serializable {

    public int checkNums;
    public int infoId;
    public String infoImg;
    public String infoOwner;
    public String infoPeriod;
    public String infoTitle;
    public String label;
    public int likeNums;
    public int likeStatus;
    public String nickName;
    public String throughUrl;
    public String userPotrait;
    public String webUrl;

    @Override
    public String toString() {
        return "SerachContentBean{" +
                "checkNums=" + checkNums +
                ", infoId=" + infoId +
                ", infoImg='" + infoImg + '\'' +
                ", infoOwner='" + infoOwner + '\'' +
                ", infoPeriod='" + infoPeriod + '\'' +
                ", infoTitle='" + infoTitle + '\'' +
                ", label='" + label + '\'' +
                ", likeNums=" + likeNums +
                ", likeStatus=" + likeStatus +
                ", nickName='" + nickName + '\'' +
                ", throughUrl='" + throughUrl + '\'' +
                ", userPotrait='" + userPotrait + '\'' +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }

    public int getCheckNums() {
        return checkNums;
    }

    public void setCheckNums(int checkNums) {
        this.checkNums = checkNums;
    }

    public int getInfoId() {
        return infoId;
    }

    public void setInfoId(int infoId) {
        this.infoId = infoId;
    }

    public String getInfoImg() {
        return infoImg;
    }

    public void setInfoImg(String infoImg) {
        this.infoImg = infoImg;
    }

    public String getInfoOwner() {
        return infoOwner;
    }

    public void setInfoOwner(String infoOwner) {
        this.infoOwner = infoOwner;
    }

    public String getInfoPeriod() {
        return infoPeriod;
    }

    public void setInfoPeriod(String infoPeriod) {
        this.infoPeriod = infoPeriod;
    }

    public String getInfoTitle() {
        return infoTitle;
    }

    public void setInfoTitle(String infoTitle) {
        this.infoTitle = infoTitle;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getLikeNums() {
        return likeNums;
    }

    public void setLikeNums(int likeNums) {
        this.likeNums = likeNums;
    }

    public int getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getThroughUrl() {
        return throughUrl;
    }

    public void setThroughUrl(String throughUrl) {
        this.throughUrl = throughUrl;
    }

    public String getUserPotrait() {
        return userPotrait;
    }

    public void setUserPotrait(String userPotrait) {
        this.userPotrait = userPotrait;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
