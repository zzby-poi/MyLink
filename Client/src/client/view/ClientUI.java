package client.view;

import client.view.ClientUI_Menu;
import client.service.*;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import common.User;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.*;
import java.util.Optional;


public class ClientUI extends Application {
    int netViewStageCount=0;
    int aboutViewStageMod=0;//0模态 1非模态
    int netViewStageMod=0;//0模态 1非模态


    private UserClientService userClientService = new UserClientService();//对象是用于登录服务/注册用户
    private MessageClientService messageClientService = new MessageClientService();//对象用户私聊/群聊.
    private FileClientService fileClientService = new FileClientService();//该对象用户传输文件

    public ClientUI() throws Exception {
    }

    public void start(Stage primaryStage) throws Exception{
        BorderPane root=new BorderPane();
        //BorderPane root=FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.initStyle(StageStyle.TRANSPARENT);//无窗口装饰

        //primaryStage.setTitle("登录");
        primaryStage.setScene(new Scene(root, 460, 330));

        VBox vb1=new VBox();//垂直容器

        //标题栏设计
        HBox top=new HBox();
        DragUtil.addDragListener(primaryStage, top);//设置拖动
        top.setPrefSize(460,30);
        Button closeBtn=new Button("X");
        closeBtn.setPrefSize(40,30);
        top.setMargin(closeBtn,new Insets(0,0,0,420));
        top.getChildren().add(closeBtn);
        closeBtn.setOnAction((ActionEvent e)->{//点击事件,关闭窗口
            try {
                primaryStage.hide();
                System.exit(0);//结束进程
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            primaryStage.hide();
        });


        closeBtn.setStyle("-fx-background-color: rgba(255,122,0,0)");
        closeBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {//获得焦点
            //closeBtn.setEffect(shadow);
            closeBtn.setStyle("-fx-background-color: rgb(194,194,194,1)");

        });
        closeBtn.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {//失去焦点
            //closeBtn.setEffect(null);
            closeBtn.setStyle("-fx-background-color: rgba(255,122,0,0)");
        });

        vb1.getChildren().add(top);


        Label appName=new Label("MyLink");
        appName.setFont(Font.font(50));
        appName.setPadding(new Insets(0,0,10,145));
        vb1.getChildren().add(appName);

        Separator separator=new Separator();//分隔符
        vb1.getChildren().add(separator);

        Color red = Color.rgb(255,50,100);
        VBox inBox=new VBox();
        TextField textField1=new TextField();
        textField1.setPromptText("账号"); // 设置输入框的提示语
        textField1.setPrefSize(250,40);
        //textField1.setPrefColumnCount(10);
        textField1.setMaxWidth(300);

        Text textTips=new Text("");
        textTips.setFill(red);
        textTips.setFont(Font.font(12));
        inBox.setMargin(textTips,new Insets(0,0,5,300-textTips.getBoundsInLocal().getWidth()));
        textField1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(textField1.getText().length()>=10||textField1.getText().length()<3){
                    textTips.setText("请输入3位到10位之间的账号");
                    inBox.setMargin(textTips,new Insets(0,0,5,300-textTips.getBoundsInLocal().getWidth()));
                }
                else {
                    textTips.setText("");
                    inBox.setMargin(textTips,new Insets(0,0,5,300-textTips.getBoundsInLocal().getWidth()));
                }
            }
        });

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("密码"); // 设置输入框的提示语
        passwordField.setPrefSize(250,40);
        passwordField.setMaxWidth(300);

        Text passTips=new Text("");
        passTips.setFill(red);
        passTips.setFont(Font.font(12));
        inBox.setMargin(passTips,new Insets(0,0,5,300-passTips.getBoundsInLocal().getWidth()));
        passwordField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(passwordField.getText().length()>=18||passwordField.getText().length()<6){
                    passTips.setText("请输入6位到18位之间的密码");
                    inBox.setMargin(passTips,new Insets(0,0,5,300-passTips.getBoundsInLocal().getWidth()));
                }
                else {
                    passTips.setText("");
                    inBox.setMargin(passTips,new Insets(0,0,5,300-passTips.getBoundsInLocal().getWidth()));
                }
            }
        });

        inBox.getChildren().addAll(textField1,textTips,passwordField,passTips);
        inBox.setPadding(new Insets(20,0,0,80));
        vb1.getChildren().add(inBox);

        Button loginBtn=new Button("登录");
        loginBtn.setPrefSize(150,45);
        vb1.setMargin(loginBtn,new Insets(0,0,0,155));
        vb1.getChildren().add(loginBtn);
        HBox bottom=new HBox();
        vb1.getChildren().add(bottom);

        Hyperlink about = new Hyperlink("关于");
        Stage aboutViewStage=new Stage();
        AboutView aboutView=new AboutView();
        //aboutViewStage.initModality(Modality.APPLICATION_MODAL);//设置为模态窗口
        aboutView.start(aboutViewStage);
        about.setOnAction((ActionEvent e) -> {
            aboutViewStage.setX(primaryStage.getX()+470);
            aboutViewStage.setY(primaryStage.getY());
            aboutViewStage.show();
        });
        bottom.getChildren().add(about);
        bottom.setMargin(about,new Insets(0,0,0,10));


        Hyperlink setting = new Hyperlink("设置");
        Stage netViewStage=new Stage();
        SetNetView net=new SetNetView();
        netViewStage.initModality(Modality.APPLICATION_MODAL);//设置为模态窗口
        setting.setOnAction((ActionEvent e) -> {
            //SetNetView net=new SetNetView();
            try {
                net.setUserClientService(userClientService);
                if(netViewStageCount==0) {
                    net.start(netViewStage);
                    netViewStageCount=1;
                }
                netViewStage.setX(primaryStage.getX()+470);
                netViewStage.setY(primaryStage.getY());
                netViewStage.show();


            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        bottom.setMargin(setting,new Insets(0,0,0,355));
        bottom.getChildren().add(setting);
        root.setCenter(vb1);

        Stage menuStage=new Stage();

        loginBtn.setOnAction((ActionEvent e)->{//登录点击事件

            if((!textField1.getText().equals(""))&&(!passwordField.getText().equals("")))
            {
                if (userClientService.checkUser(textField1.getText(), passwordField.getText())){
                    System.out.println("登录成功");
                    ClientUI_Menu menu  = new ClientUI_Menu(userClientService,messageClientService,fileClientService);//创建menu窗口
                    userClientService.setServer(userClientService,messageClientService,fileClientService);
                    menu.setUserId(textField1.getText());
                    OnlineUserList onlineUserList=new OnlineUserList(new ListView());
                    userClientService.setMainStage(menuStage);
                    userClientService.setBanId(textField1.getText());
                    userClientService.onlineFriendList(onlineUserList);
                    //menu.Loading(onlineUserList);
                    //userClientService.onlineFriendList(new OnlineUserList(new ListView()));
                    try {
                        menu.Loading(onlineUserList);
                        Thread.sleep(1000);
                        //menu.start(new Stage());//打开menu窗口
                        menu.start(menuStage);
                        menu.ReSetName();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    primaryStage.hide();//点开新的界面后，关闭此界面
                }
                else System.out.println("登录失败");
            }
        });

        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}


