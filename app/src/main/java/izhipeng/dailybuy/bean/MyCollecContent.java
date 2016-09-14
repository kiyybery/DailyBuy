package izhipeng.dailybuy.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/16 0016.
 */
public class MyCollecContent implements Serializable{

    public int commentNums;
    public int favorId;
    public String favorImage;
    public String favorTitle;
    public int likeNums;
    public String webUrl;
    public String publishName;
    public String publishPortrait;
    public int state;

    public String getPublishName() {
        return publishName;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }

    public String getPublishPortrait() {
        return publishPortrait;
    }

    public void setPublishPortrait(String publishPortrait) {
        this.publishPortrait = publishPortrait;
    }

    public int getCommentNums() {
        return commentNums;
    }

    public void setCommentNums(int commentNums) {
        this.commentNums = commentNums;
    }

    public int getFavorId() {
        return favorId;
    }

    public void setFavorId(int favorId) {
        this.favorId = favorId;
    }

    public String getFavorImage() {
        return favorImage;
    }

    public void setFavorImage(String favorImage) {
        this.favorImage = favorImage;
    }

    public String getFavorTitle() {
        return favorTitle;
    }

    public void setFavorTitle(String favorTitle) {
        this.favorTitle = favorTitle;
    }

    public int getLikeNums() {
        return likeNums;
    }

    public void setLikeNums(int likeNums) {
        this.likeNums = likeNums;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MyCollecContent{" +
                "commentNums=" + commentNums +
                ", favorId=" + favorId +
                ", favorImage='" + favorImage + '\'' +
                ", favorTitle='" + favorTitle + '\'' +
                ", likeNums=" + likeNums +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }
}
