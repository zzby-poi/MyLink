package common;

public class UserInf {
    private String userId;//用户id
    private String userName;//用户名
    private String userSig;//用户签名
    private String imgType;//用户头像


    public UserInf(String userId, String userName, String userSig,String imgType) {
        this.userId = userId;
        this.userName = userName;
        this.userSig = userSig;
        this.imgType=imgType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }
}
