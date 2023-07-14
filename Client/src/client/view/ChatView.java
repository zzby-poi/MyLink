package client.view;

import client.service.FileClientService;
import client.service.ManageClientConnectServerThread;
import client.service.MessageClientService;
import client.service.UserClientService;
import com.sun.javafx.application.PlatformImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.Socket;


public class ChatView extends Application {
    private UserClientService userClientService;
    private MessageClientService messageClientService;
    private FileClientService fileClientService;

    public void setServer(UserClientService a, MessageClientService b, FileClientService c){
        this.userClientService=a;
        this.messageClientService=b;
        this.fileClientService=c;
    }

    public MessageClientService getMessageClientService() {
        return messageClientService;
    }

    private String title;//标题即用户名
    private String userId;//用户Id即线程

    private String myId;//我的线程

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getMyId() {
        return myId;
    }
    //private Socket socket;

    public void setUserId(String userId) {//
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    private Stage chatStage;
    GridPane chatView=new GridPane();;
    Scene scene=new Scene(chatView, 600, 800);
    ScrollPane inSp=new ScrollPane();;//滚动面板
    VBox chatList=new VBox();;//垂直容器,存放所有消息气泡，并用滚动面板浏览


    public void setChatStage(Stage chatStage) {
        this.chatStage = chatStage;
    }

    public Stage getChatStage() {
        return chatStage;
    }

    public ChatView(){//初始化
        inSp.setPrefHeight(500);
        chatList.setPrefWidth(500);
        chatList.setSpacing(10);//垂直容器间距
        chatList.setPrefWidth(500);
        chatList.setSpacing(10);//垂直容器间距
        inSp.setContent(chatList);
        chatView.add(inSp,0,1,1,1);

        //chatList.getChildren().add(new ChatBubble("你吃了吗没吃吃点米").getChatBubble("sender"));
        //chatList.getChildren().add(new ChatBubble("一二三四五").getChatBubble("getter"));

    }
    public void inMessage(String message){//将接收的消息添加到聊天气泡列表
        chatList.getChildren().add(new ChatBubble(message).getChatBubble("sender"));
        inSp.setContent(chatList);
        chatView.getChildren().remove(inSp);
        chatView.add(inSp,0,1,1,1);
        //chatStage.show();
    }

    public void outMessage(String message){//将发送的消息添加到聊天气泡列表
        chatList.getChildren().add(new ChatBubble(message).getChatBubble("getter"));
        inSp.setContent(chatList);
        chatView.getChildren().remove(inSp);
        chatView.add(inSp,0,1,1,1);
        //chatStage.show();
    }

    public void setChatList(VBox chatList) {
        this.chatList = chatList;
    }
    public VBox getChatList() {
        return chatList;
    }
    TextArea outText=new TextArea();

    public void onKeyPressdeTextArea(KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.ENTER) {
            if(keyEvent.isControlDown()){//如果是Ctrl+Enter
                int caretPosition = this.outText.getCaretPosition();
                String text = this.outText.getText();// 获得输入文本，此文本不包含刚刚输入的换行符
                // 获得光标两边的文本
                String front = text.substring(0, caretPosition);
                String end = text.substring(caretPosition);
                this.outText.setText(front + System.lineSeparator() + end); // 在光标处插入换行符
                this.outText.positionCaret(caretPosition + 1);// 将光标移至换行符
            }
            else
            {
                String msg=this.outText.getText();
                if(msg.length()>0){//如果消息不为空
                    if(msg.substring(0,1).equals("\n")&&msg.length()>1){
                        if(!msg.substring(1,2).equals("\n")){
                            String trueMsg=msg.substring(1,msg.length());
                            outMessage(trueMsg);
                            System.out.println(trueMsg);
                            messageClientService.sendMessageToOne(trueMsg, myId, userId);
                        }
                    } else if(!msg.substring(0,1).equals("\n")){
                        System.out.println("msg:"+msg);
                        outMessage(msg);
                        System.out.println(msg);
                        messageClientService.sendMessageToOne(msg, myId, userId);
                    }
                    this.outText.clear();
                }
                else{
                    this.outText.clear();
                }
            }



        }



        // 如果按下了回车键

       /* if (keyEvent.getCode() == KeyCode.ENTER) {
            // 获得此时的光标位置。此位置为刚刚输入的换行符之后
            int caretPosition = this.outText.getCaretPosition();

            // 如果已经按下的按键中包含 Control 键
            if (!keyEvent.isControlDown()) { // 如果输入的不是组合键 `Ctrl+Enter`，去掉换行符，然后将文本发送
                // 获得输入文本，此文本包含刚刚输入的换行符
                String text = this.outText.getText();
                // 获得换行符两边的文本
                String f=text.substring(0, caretPosition);
                String front = text.substring(0, caretPosition - 1);
                String end = text.substring(caretPosition);
                this.outText.setText(front + end);

                outMessage(outText.getText());
                System.out.println(outText.getText());
                messageClientService.sendMessageToOne(outText.getText(), myId, userId);

                this.outText.clear();
                this.outText.setText("");

                //this.onActionButton(null); // 模拟发送
                System.out.println("output");
            } else {
                String text = this.outText.getText();// 获得输入文本，此文本不包含刚刚输入的换行符
                // 获得光标两边的文本
                String front = text.substring(0, caretPosition);
                String end = text.substring(caretPosition);
                this.outText.setText(front + System.lineSeparator() + end); // 在光标处插入换行符
                this.outText.positionCaret(caretPosition + 1);// 将光标移至换行符
            }
        }*/
    }



    public void start(Stage chatStage) throws Exception {
        this.chatStage=chatStage;
        chatStage.setTitle("用户界面");
        //Scene scene = new Scene(chatView, 600, 800);
        chatStage.setScene(scene);
        chatStage.initStyle(StageStyle.TRANSPARENT);//无窗口装饰

        HBox top=new HBox();//水平容器，状态栏
        top.setPrefSize(600,30);
        top.setPadding(new Insets(5,0,0,0));
        DragUtil.addDragListener(chatStage, top);//设置拖动


        Text WName_text=new Text(title);//标题文字
        WName_text.setFont(Font.font(20));//文字大小
        top.getChildren().add(WName_text);
        top.setMargin(WName_text,new Insets(0,240,0,200));


        Button close_btn=new Button("X");
        top.getChildren().add(close_btn);
        close_btn.setOnAction((ActionEvent e)->{//点击事件,关闭窗口

            try {
                //userClientService.logout();//退出系统
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            //chatView.setVisible(false);//设置可见性

            chatStage.close();
            //primaryStage.hide();
        });

        close_btn.setStyle("-fx-background-color: rgba(255,122,0,0)");
        close_btn.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {//获得焦点
            //closeBtn.setEffect(shadow);
            close_btn.setStyle("-fx-background-color: rgb(194,194,194,1)");

        });
        close_btn.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {//失去焦点
            //closeBtn.setEffect(null);
            close_btn.setStyle("-fx-background-color: rgba(255,122,0,0)");
        });

        top.setMargin(close_btn,new Insets(0,0,0,0));
        chatView.add(top,0,0,1,1);

        //聊天消息框,滚动面板


       /*ScrollPane inSp = new ScrollPane();//滚动面板
        inSp.setPrefHeight(500);

        VBox chatList=new VBox();//垂直容器,存放所有消息气泡，并用滚动面板浏览
        chatList.setPrefWidth(500);
        chatList.setSpacing(10);//垂直容器间距

        chatList.getChildren().add(new ChatBubble("你吃了吗没吃吃点米").getChatBubble("sender"));
        chatList.getChildren().add(new ChatBubble("一二三四五").getChatBubble("getter"));

        inSp.setContent(chatList);
        chatView.add(inSp,0,1,1,1);*/

        //输入框
        /*TextArea outText=new TextArea();//输入框*/
        outText.setPadding(new Insets(10,0,0,0));
        outText.setMaxHeight(200); // 设置多行输入框的最大高度
        //area.setMaxWidth(300); // 设置多行输入框的最大宽度
        outText.setPrefSize(500, 150); // 设置多行输入框的推荐宽高
        outText.setEditable(true); // 设置多行输入框能否编辑
        outText.setPromptText("请输入文字..."); // 设置多行输入框的提示语
        outText.setWrapText(true); // 设置多行输入框是否支持自动换行。true表示支持，false表示不支持。

        chatView.add(outText,0,2,1,1);

        outText.setOnKeyPressed(this::onKeyPressdeTextArea);

/*
        outText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("状态：当前字符数为：" + outText.getText().length()+"  字符："+outText.getText());
            }
        });
        outText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String msg=outText.getText();
                if(msg.substring(0).equals("\n")){
                    outText.clear();
                }
                if(msg.length()>1){//文本框非空
                    if(msg.substring(msg.length()-1).equals("\n")){
                        outMessage(outText.getText());
                        messageClientService.sendMessageToOne(outText.getText(), myId, userId);
                        outText.clear();
                        outText.setText("");
                    }
                }
            }
        });*/


        //功能按钮
        HBox hBox1=new HBox();
        hBox1.setPadding(new Insets(10,0,0,480));

        Button sendBtn=new Button("发送");
        sendBtn.setPrefSize(100,30);

        sendBtn.setOnAction((ActionEvent e)->{//发送消息
            //将消息发送给服务器端
            if(!outText.getText().equals("")){//当消息不为空时发送消息
                outMessage(outText.getText());
                System.out.println(outText.getText());
                messageClientService.sendMessageToOne(outText.getText(), myId, userId);
                outText.setText("");
            }
        });

        hBox1.getChildren().add(sendBtn);
        chatView.add(hBox1,0,3,1,1);
        //chatStage.show();
    }

}
