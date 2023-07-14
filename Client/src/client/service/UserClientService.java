package client.service;

import client.view.ChatView;
import client.view.UserListCard;
import common.Message;
import common.MessageType;
import common.User;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.stream.Stream;

public class UserClientService {
    private String IP="127.0.0.1";
    private int port=9999;
    Stage MainStage;
    public void setMainStage(Stage mainStage) {
        MainStage = mainStage;
    }
    public Stage getMainStage() {
        return MainStage;
    }

    public void setNet(String ip, String port){
        this.IP=ip;
        this.port=Integer.valueOf(port);
    }

    public UserClientService() throws Exception{
        File file=new File("setting.txt");
        FileReader fr=new FileReader(file);
        BufferedReader bufr=new BufferedReader(fr);
        String temp=bufr.readLine();
        System.out.println("Aset:"+temp);
        String[] s=temp.split(":");
        setNet(s[0],s[1]);
        bufr.close();
        fr.close();
    }


    private UserClientService TempUserClientService;
    private MessageClientService TempMessageClientService;
    private FileClientService TempFileClientService;
    public void setServer(UserClientService a,MessageClientService b,FileClientService c){
        this.TempUserClientService=a;
        this.TempMessageClientService=b;
        this.TempFileClientService=c;
    }



    //因为我们可能在其他地方用使用user信息, 因此作出成员属性
    private User u = new User();
    String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    //因为Socket在其它地方也可能使用，因此作出属性
    private Socket socket;
    String banId;

    public void setBanId(String banId) {
        this.banId = banId;
    }

    public String getBanId() {
        return banId;
    }

    //根据userId 和 pwd 到服务器验证该用户是否合法
    public boolean checkUser(String userId, String pwd) {
        boolean b = false;
        //创建User对象
        u.setUserId(userId);
        u.setPasswd(pwd);
        try {
            //连接到服务端，发送u对象
            socket = new Socket(InetAddress.getByName(IP), port);//101.43.88.177
            //得到ObjectOutputStream对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);//发送User对象

            //读取从服务器回复的Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();

            if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {//登录OK

                //创建一个和服务器端保持通信的线程-> 创建一个类 ClientConnectServerThread
                ClientConnectServerThread clientConnectServerThread =
                        new ClientConnectServerThread(socket);
                //启动客户端的线程
                clientConnectServerThread.start();
                //这里为了后面客户端的扩展，我们将线程放入到集合管理
                ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectServerThread);


                b = true;
            } else {
                //如果登录失败, 我们就不能启动和服务器通信的线程, 关闭socket
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
    //向服务器端请求在线用户列表
    public void onlineFriendList(OnlineUserList onlineUserList){
        //发送一个Message , 类型MESSAGE_GET_ONLINE_FRIEND
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());
        //发送给服务器
        try {
            //从管理线程的集合中，通过userId, 得到这个线程对象
            ClientConnectServerThread clientConnectServerThread =
                    ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId());

            clientConnectServerThread.setServer(TempUserClientService,TempMessageClientService,TempFileClientService);

            clientConnectServerThread.setMainStage(getMainStage());
            clientConnectServerThread.setBanId(banId);
            clientConnectServerThread.setOnlineUserList(onlineUserList);//关联UI 在线用户列表


            //通过这个线程得到关联的socket
            Socket socket = clientConnectServerThread.getSocket();
            //得到当前线程的Socket 对应的 ObjectOutputStream对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message); //发送一个Message对象，向服务端要求在线用户列表

            /*System.out.println("nn:"+clientConnectServerThread.name);
            this.name=clientConnectServerThread.name;*/

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //编写方法，退出客户端，并给服务端发送一个退出系统的message对象
    public void logout() throws Exception{
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());//一定要指定我是哪个客户端id

        //发送message
        try {
            //ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream
                            (ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);

            System.out.println(u.getUserId() + " 退出系统 ");
            System.exit(0);//结束进程

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


