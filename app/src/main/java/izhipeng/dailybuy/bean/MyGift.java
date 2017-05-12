package izhipeng.dailybuy.bean;

/**
 * Created by Administrator on 2016/11/8 0008.
 */
public class MyGift {

    public String createTime;
    public String expressNumber;
    public String giftImage;
    public String giftName;
    public int number;
    public String price;
    public int tradingStatus;
    public String expressage;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public String getGiftImage() {
        return giftImage;
    }

    public void setGiftImage(String giftImage) {
        this.giftImage = giftImage;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getTradingStatus() {
        return tradingStatus;
    }

    public void setTradingStatus(int tradingStatus) {
        this.tradingStatus = tradingStatus;
    }

    public String getExpressage() {
        return expressage;
    }

    public void setExpressage(String expressage) {
        this.expressage = expressage;
    }

    @Override
    public String toString() {
        return "MyGift{" +
                "createTime='" + createTime + '\'' +
                ", expressNumber='" + expressNumber + '\'' +
                ", giftImage='" + giftImage + '\'' +
                ", giftName='" + giftName + '\'' +
                ", number=" + number +
                ", price='" + price + '\'' +
                ", tradingStatus=" + tradingStatus +
                ", expressage='" + expressage + '\'' +
                '}';
    }
}
