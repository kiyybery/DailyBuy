package izhipeng.dailybuy.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class CommentBean implements Serializable {

    public String commentContent;
    public int commentId;
    public String commentName;
    public String commentTime;
    public String portraitPath;

    @Override
    public String toString() {
        return "CommentBean{" +
                "commentContent='" + commentContent + '\'' +
                ", commentId=" + commentId +
                ", commentName='" + commentName + '\'' +
                ", commentTime='" + commentTime + '\'' +
                ", portraitPath='" + portraitPath + '\'' +
                '}';
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentName() {
        return commentName;
    }

    public void setCommentName(String commentName) {
        this.commentName = commentName;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getPortraitPath() {
        return portraitPath;
    }

    public void setPortraitPath(String portraitPath) {
        this.portraitPath = portraitPath;
    }
}
