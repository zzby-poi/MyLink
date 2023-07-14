package common;
import java.io.Serializable;
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userId;//用户Id
    private String name;//用户名
    private String passwd;//用户密码
    private String userSig;//用户签名
    private String imgType;//用户头像

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public User() {}
    public User(String userId, String passwd,String userSig,String imgType) {
        this.userId = userId;
        this.passwd = passwd;
        this.userSig=userSig;
        this.imgType=imgType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
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
