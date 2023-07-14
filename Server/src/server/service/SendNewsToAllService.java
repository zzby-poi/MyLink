package server.service;

import common.Message;
import common.MessageType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.*;

public class SendNewsToAllService implements Runnable {

    private boolean flag=true;//服务器控制台是否输入exit
    public void setFlag(boolean flag) throws IOException {
        this.flag = flag;
    }
    public boolean getFlag(){
        return flag;
    }
    @Override
    public void run() {
        //为了可以推送多次新闻，使用while
        while (true) {
            System.out.println("请输入服务器要推送的新闻/消息[输入exit表示退出推送服务线程]");
            String news = Utility.readString(100);
            //System.out.println("news:"+news);
            if("exit".equals(news)) {//退出
                try {
                    setFlag(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            //构建一个消息 , 群发消息
            Message message = new Message();
            message.setSender("服务器");
            message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
            message.setContent(news);
            message.setSendTime(new Date().toString());
            System.out.println("服务器推送消息给所有人 说: " + news);

            //遍历当前所有的通信线程，得到socket,并发送message

            HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();

            Iterator<String> iterator = hm.keySet().iterator();
            while (iterator.hasNext()) {
                String onLineUserId = iterator.next().toString();
                try {
                    ObjectOutputStream oos =
                            new ObjectOutputStream(hm.get(onLineUserId).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
