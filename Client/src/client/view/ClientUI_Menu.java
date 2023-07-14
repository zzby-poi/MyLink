package client.view;

import client.service.FileClientService;
import client.service.MessageClientService;
import client.service.OnlineUserList;
import client.service.UserClientService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;

class DragListener implements EventHandler<MouseEvent> {//实现无窗口标题栏的拖动操作
    private double xOffset = 0;
    private double yOffset = 0;
    private final Stage stage;
    public DragListener(Stage stage) {
        this.stage = stage;
    }
    @Override
    public void handle(MouseEvent event) {
        event.consume();
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            stage.setX(event.getScreenX() - xOffset);
            if(event.getScreenY() - yOffset < 0) {
                stage.setY(0);
            }else {
                stage.setY(event.getScreenY() - yOffset);
            }
        }
    }

    public void enableDrag(Node node) {
        node.setOnMousePressed(this);
        node.setOnMouseDragged(this);
    }
}
class DragUtil {
    public static void addDragListener(Stage stage,Node root) {
        new DragListener(stage).enableDrag(root);
    }
}

class ClientUI_Menu extends Application {

    private UserClientService userClientService;
    private MessageClientService messageClientService;
    private FileClientService fileClientService;
    String UserId="0";
    String UserName="用户名";
    String UserSig="helloworld";

    OnlineUserList onlineUserList;//在线列表
    Label user_name=new Label(UserName);
    Label user_sig=new Label(UserSig);

    public void ReSetName() throws IOException {
        File file=new File("MyInf.txt");
        FileReader fr=new FileReader(file);
        BufferedReader bufr=new BufferedReader(fr);
        String[] s=bufr.readLine().split(":");
        setUserName(s[0]);
        setUserSig(s[1]);
        System.out.println("set:"+bufr.readLine());
        bufr.close();
        fr.close();
        user_name.setText(UserName);
        user_sig.setText(UserSig);
    }


    public void Loading(OnlineUserList onlineUserList) throws IOException {
        this.onlineUserList=onlineUserList;
        onlineUserList.getOnlineUserList().setPrefWidth(430);
        onlineUserList.getOnlineUserList().setPrefHeight(600);
        setUserName(UserName);
        setUserSig(UserSig);
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserSig(String userSig) {
        UserSig = userSig;
    }

    public String getUserSig() {
        return UserSig;
    }

    public ClientUI_Menu(UserClientService userClientService, MessageClientService messageClientService, FileClientService fileClientService){
        this.userClientService=userClientService;
        this.messageClientService=messageClientService;
        this.fileClientService=fileClientService;
    }



    public void start(Stage primaryStage) throws Exception {


        GridPane menu=new GridPane();
        //primaryStage.setTitle("用户界面");
        Scene scene = new Scene(menu, 430, 800);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);//无窗口装饰

        /*-----状态栏设计-----*/
        HBox top=new HBox();//水平容器，状态栏
        top.setPrefSize(600,30);
        top.setPadding(new Insets(5,0,0,0));
        DragUtil.addDragListener(primaryStage, top);//设置拖动

        Text App_name=new Text("MyLink");//应用名称
        App_name.setFont(Font.font(20));
        top.getChildren().add(App_name);
        top.setMargin(App_name,new Insets(0,0,0,0));

        Text WName_text=new Text("用户界面");//标题文字
        WName_text.setFont(Font.font(20));//文字大小
        top.getChildren().add(WName_text);
        top.setMargin(WName_text,new Insets(0,100,0,100));

        Button minimize_btn=new Button("-");
        top.getChildren().add(minimize_btn);
        minimize_btn.setOnAction((ActionEvent e)->{//点击事件,最小化窗口
            primaryStage.setIconified(true);
        });

        minimize_btn.setStyle("-fx-background-color: rgba(255,122,0,0)");
        minimize_btn.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {//获得焦点
            //closeBtn.setEffect(shadow);
            minimize_btn.setStyle("-fx-background-color: rgb(194,194,194,1)");

        });
        minimize_btn.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {//失去焦点
            //closeBtn.setEffect(null);
            minimize_btn.setStyle("-fx-background-color: rgba(255,122,0,0)");
        });

        top.setMargin(minimize_btn,new Insets(0,0,0,0));

        Button close_btn=new Button("X");
        top.getChildren().add(close_btn);
        close_btn.setOnAction((ActionEvent e)->{//点击事件,关闭窗口
            try {
                userClientService.logout();//退出系统
                primaryStage.hide();
                //Thread.sleep(1000);
                //System.exit(0);//结束进程
            } catch (Exception exception) {
                exception.printStackTrace();
            }
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


        menu.add(top,0,0,1,1);
        /*-----结束-----*/

        /*-----用户卡片设计-----*/
        HBox user_card=new HBox();//水平容器，用户卡片
        user_card.setPrefSize(430,70);
        user_card.setPadding(new Insets(10,0,20,0));
        user_card.setSpacing(20);//水平间隔
        Button user_img_button=new Button();//用户头像
        user_img_button.setStyle(
                "-fx-background-image: url('/client/view/date/user_mini_img.png');"
                        +"-fx-background-size: 70px;"
                        +"-fx-background-radius: 75em; "
                        + "-fx-min-width: 70px; "
                        + "-fx-min-height: 70px; "
                        + "-fx-max-width: 70px; "
                        + "-fx-max-height: 70px;"
        );
        DropShadow shadow = new DropShadow();//阴影效果
        shadow.setRadius(10.0);//阴影范围
        shadow.setOffsetX(0.0);//偏移距离
        shadow.setOffsetY(0.0);//偏移距离
        shadow.setSpread(0.0);//模糊度
        shadow.setColor(Color.color(0.3, 0.9, 0.0));//阴影颜色
        user_img_button.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {//获得焦点
            user_img_button.setEffect(shadow);
        });
        user_img_button.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {//失去焦点
            user_img_button.setEffect(null);
        });
        user_card.getChildren().addAll(user_img_button);

        VBox user_inf_gui=new VBox();//垂直容器，用户信息
        user_inf_gui.setSpacing(10);

        //Label user_name=new Label(UserName);
        user_name.setFont(Font.font(null,28));
        user_inf_gui.getChildren().addAll(user_name);

        //user_sig.setY(100);
        user_sig.setFont(Font.font(null,16));
        user_sig.setText("Hello world");
        user_inf_gui.getChildren().addAll(user_sig);
        user_card.getChildren().addAll(user_inf_gui);
        menu.add(user_card,0,1);
        /*-----结束-----*/

        /*-----Link功能设计-----*/
        VBox link_menu=new VBox();//垂直容器，Link的操作菜单
        //link_menu.setVisible(false);//初始不可见

        TextField serch_box=new TextField();//搜索框
        link_menu.getChildren().add(serch_box);
        serch_box.setPrefSize(400,30);

        HBox link_btn_box=new HBox();
        Button message_btn=new Button("消息");
        message_btn.setPrefSize(200,50);

        Button friend_btn=new Button("在线用户");
        friend_btn.setPrefSize(200,50);
        link_btn_box.getChildren().addAll(message_btn,friend_btn);
        link_menu.getChildren().add(link_btn_box);

        menu.add(link_menu,0,2);

        //好友列表
        menu.add(onlineUserList.getOnlineUserList(),0,3);
        //ListView userList=new ListView();
        //OnlineUserList onlineUserList=new OnlineUserList(userList);//在线列表
        //userList.setPrefWidth(430);
        //userList.setPrefHeight(600);
        //点击事件
        friend_btn.setOnAction((ActionEvent e)->{//在线用户点击事件
            menu.getChildren().remove(onlineUserList.getOnlineUserList());

            userClientService.setMainStage(primaryStage);
            userClientService.onlineFriendList(onlineUserList);
            menu.add(onlineUserList.getOnlineUserList(),0,3);
            primaryStage.show();

        });
        /*-----结束-----*/

        primaryStage.show();
    }


}
