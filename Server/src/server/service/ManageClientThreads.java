package server.service;

import common.User;
import common.UserInf;

import java.util.HashMap;
import java.util.Iterator;

public class ManageClientThreads {
    private static OnLineUserInf onLineUserInf;

    public static void setOnLineUserInf(OnLineUserInf onLineUserInf) {
        ManageClientThreads.onLineUserInf = onLineUserInf;
    }


    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();

    public static HashMap<String, ServerConnectClientThread> getHm() { //返回 hm
        return hm;
    }

    //添加线程对象到 hm 集合
    public static void addClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);
    }

    //根据userId 返回ServerConnectClientThread线程
    public static ServerConnectClientThread getServerConnectClientThread(String userId) {
        return hm.get(userId);
    }

    //增加一个方法，从集合中，移除某个线程对象
    public static void removeServerConnectClientThread(String userId) {
        hm.remove(userId);
    }

    //这里编写方法，可以返回在线用户列表
    public static String getOnlineUser() {
        //集合遍历 ，遍历 hashmap的key
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList = "";
        while (iterator.hasNext()) {
            String userId=iterator.next().toString();//获取用户id
            UserInf userInf=onLineUserInf.getUserInf(userId);
            onlineUserList += userId +" "+userInf.getUserName()+" "+userInf.getUserSig()+" "+userInf.getImgType()+"/";

            /*onlineUserList += iterator.next().toString() + " ";*/
        }
        return  onlineUserList;
    }
}
