package client.service;

import client.view.ChatView;
import client.view.UserListCard;
import com.sun.javafx.application.PlatformImpl;
import common.Message;
import common.MessageType;
import common.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

import client.view.ChatBubble;
import javafx.stage.Stage;

import javax.swing.*;
import javax.xml.validation.ValidatorHandler;





public class ClientConnectServerThread extends Thread {

    public void  newMessageBox(String title,String message){
        System.out.println(title);
        System.out.println(message);
        //JOptionPane.showMessageDialog(null, title, message, JOptionPane.ERROR_MESSAGE);
    }


    Stage MainStage=new Stage();

    public void setMainStage(Stage mainStage) {
        MainStage = mainStage;
    }
    public Stage getMainStage() {
        return MainStage;
    }

    public static void runLater(Runnable runnable) {
        PlatformImpl.runLater(runnable);
    }
    private UserClientService TempUserClientService;
    private MessageClientService TempMessageClientService;
    private FileClientService TempFileClientService;
    public void setServer(UserClientService a,MessageClientService b,FileClientService c){
        this.TempUserClientService=a;
        this.TempMessageClientService=b;
        this.TempFileClientService=c;
    }

    String banId;//ban掉自己的id
    User myInf;

    public static String name;

    public void setMyInf(User myInf){
        this.myInf=myInf;
    }
    public User getMyInf() {
        return myInf;
    }

    public void setBanId(String banId) {
        this.banId = banId;
    }

    public String getBanId() {
        return banId;
    }

    //该线程需要持有Socket
    private Socket socket;



    ObservableList<HBox> items = FXCollections.observableArrayList();
    OnlineUserList onlineUserList;
    //VBox chatList=new VBox();//垂直容器,存放所有消息气泡，并用滚动面板浏览
    //ChatView cv=new ChatView();
    //ChatView cv;//聊天窗口
    //VBox chatList;//消息列表

    public void setOnlineUserList(OnlineUserList onlineUserList){//在线用户列表
        this.onlineUserList=onlineUserList;
    }

    //构造器可以接受一个Socket对象
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    //
    @Override
    public void run() {
        HashMap<String,ChatView> HSuserList = new HashMap<>();//存放用户id与聊天窗口的对应哈希表
        while (true) {

            ObjectInputStream ois=null;
            try {
                System.out.println("客户端线程，等待从读取从服务器端发送的消息");
                try{
                    ois = new ObjectInputStream(socket.getInputStream());
                }catch(EOFException e){
                    System.exit(0);
                }

                //ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                //如果服务器没有发送Message对象,线程会阻塞在这里
                Message message = (Message) ois.readObject();

                //if(ois.readObject()==null) System.exit(0);


                //判断message类型，然后做相应的处理
                //如果是读取到的是 服务端返回的在线用户列表
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {//取出在线列表信息，并显示
                    String[] onlineUsers = message.getContent().split("/");//截取用户id
                    System.out.println("\n=======当前在线用户列表========");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        String[] onlineUserInf=onlineUsers[i].split(" ");//用户id 0  用户名称 1 用户签名 2 用户头像类型 3
                        if(onlineUserInf[0].equals(banId)) {
                            User inf=new User();
                            inf.setUserId(onlineUserInf[0]);
                            inf.setName(onlineUserInf[1]);
                            inf.setUserSig(onlineUserInf[2]);
                            inf.setImgType(onlineUserInf[3]);
                            setMyInf(inf);
                            name=onlineUserInf[1];

                            File file=new File("MyInf.txt");
                            file.delete();
                            file.createNewFile();
                            FileWriter fw=new FileWriter(file,true);
                            BufferedWriter bufw=new BufferedWriter(fw);
                            bufw.write(name+":"+onlineUserInf[2]);
                            bufw.close();
                            fw.close();

                            System.out.println("aa:"+name);
                        }
                        if(HSuserList.containsKey(onlineUserInf[0])==false&&(!onlineUserInf[0].equals(banId))){//如果当前用户为新用户
                            ChatView cv=new ChatView();

                            Platform.runLater(()->{
                                cv.setChatStage(new Stage());

                            });
                            cv.setUserId(onlineUserInf[0]);
                            HSuserList.put(onlineUserInf[0],cv);//创建哈希表<用户ID,cv>
                            System.out.println(onlineUserInf[0]+"<---->"+cv.getUserId());

                            UserListCard userListCard=new UserListCard//创建用户卡
                                    ("/client/view/date/user_mini_img.png",onlineUserInf[1],onlineUserInf[2],onlineUserInf[0]);
                            userListCard.setChatView(cv);
                            userListCard.setMyId(banId);
                            userListCard.setMainStage(getMainStage());
                            userListCard.setServer(TempUserClientService,TempMessageClientService,TempFileClientService);
                            onlineUserList.addUser(userListCard.getUserListCard());
                            //tempon.addUser(userListCard.getUserListCard());
                            System.out.println("用户: " + onlineUsers[i]);
                        }
                    }
                    //onlineUserList.setOnlineUserList(tempon.getOnlineUserList());



                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {//普通的聊天消息
                    //把从服务器转发的消息，显示到控制台
                    System.out.println("\n" + message.getSender()
                            + " 对 " + message.getGetter() + " 说: " + message.getContent());

                    ChatView cv=HSuserList.get(message.getSender());//取出用户ID对应的聊天窗口

                    System.out.println("当前线程名称:"+cv.getUserId());
                    System.out.println("线程列表:");
                    for(String key: HSuserList.keySet()) {
                        System.out.println(key+"<--->"+HSuserList.get(key).getUserId());
                    }
                    Platform.runLater(()->{
                        cv.inMessage(message.getContent());
                    });

                } else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)) {
                    //显示在客户端的控制台
                    System.out.println("\n" + message.getSender() + " 对大家说: " + message.getContent());
                    newMessageBox(message.getSender(),message.getContent());

                    //JOptionPane.showMessageDialog(null, message.getContent(),  message.getSender(), JOptionPane.PLAIN_MESSAGE);




                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {//如果是文件消息

                    //让用户指定保存路径。。。
                    System.out.println("\n" + message.getSender() + " 给 " + message.getGetter()
                            + " 发文件: " + message.getSrc() + " 到我的电脑的目录 " + message.getDest());

                    //取出message的文件字节数组，通过文件输出流写出到磁盘
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest(), true);
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("\n 保存文件成功~");

                }
                else if(message.getMesType().equals(MessageType.MESSAGE_GET_MY_INF)){//获取自己的信息

                }
                else {
                    System.out.println("是其他类型的message, 暂时不处理....");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    //为了更方便的得到Socket
    public Socket getSocket() {
        return socket;
    }
}
