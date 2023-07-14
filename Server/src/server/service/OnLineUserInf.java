package server.service;

import common.UserInf;

import java.util.ArrayList;
import java.util.List;

public class OnLineUserInf {

    List<String> key=new ArrayList();
    List<UserInf> userInf=new ArrayList();
    public void addUser(String userId, String userName, String userSig,String imgType){
        System.out.println("01添加在线用户信息"+userId+userName+userSig+imgType);
        userInf.add(new UserInf(userId,userName,userSig,imgType));
        key.add(userId);
    }

    public void remove(String userId){
        userInf.remove(key.indexOf(userId));//删除指定索引的元素
        key.remove( key.indexOf(userId));
    }

    public UserInf getUserInf(String userId) {
        return userInf.get(key.indexOf(userId));
    }
}
