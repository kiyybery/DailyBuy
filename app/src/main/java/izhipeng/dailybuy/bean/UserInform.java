package izhipeng.dailybuy.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class UserInform implements Parcelable {

    private String userId;
    private String avatar;
    private String userName;
    private String address;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.avatar);
        dest.writeString(this.userName);
        dest.writeString(this.address);
    }

    public UserInform() {
    }

    protected UserInform(Parcel in) {
        this.userId = in.readString();
        this.avatar = in.readString();
        this.userName = in.readString();
        this.address = in.readString();
    }

    public static final Creator<UserInform> CREATOR = new Creator<UserInform>() {
        @Override
        public UserInform createFromParcel(Parcel source) {
            return new UserInform(source);
        }

        @Override
        public UserInform[] newArray(int size) {
            return new UserInform[size];
        }
    };
}
