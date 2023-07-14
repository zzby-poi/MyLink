package client.view;

import client.service.FileClientService;
import client.service.MessageClientService;
import client.service.UserClientService;
import client.utils.Utility;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author 韩顺平
 * @version 1.0
 * 客户端的菜单界面
 */
public class QQView extends Application {
    private boolean loop = true; //控制是否显示菜单
    private String key = ""; // 接收用户的键盘输入
    private UserClientService userClientService = new UserClientService();//对象是用于登录服务/注册用户
    private MessageClientService messageClientService = new MessageClientService();//对象用户私聊/群聊.
    private FileClientService fileClientService = new FileClientService();//该对象用户传输文件

    public QQView() throws Exception {
    }

    public static void main(String[] args) {
        launch(args);
        /*new QQView().mainMenu();
        System.out.println("客户端退出系统.....");*/
    }

    public void start(Stage primaryStage) throws Exception{
        BorderPane root=new BorderPane();
        primaryStage.setTitle("登录");
        primaryStage.setScene(new Scene(root, 450, 300));

        VBox vb1=new VBox();//垂直容器
        vb1.setSpacing(20);
        vb1.setPadding(new Insets(30,0,0,10));

        HBox hb1=new HBox();//水平容器 账号
        Label label1=new Label("账号");
        TextField textField1=new TextField();
        hb1.getChildren().addAll(label1,textField1);
        hb1.setSpacing(10);

        HBox hb2=new HBox();//水平容器 密码
        Label label2=new Label("密码");
        PasswordField passwordField = new PasswordField();
        hb2.getChildren().addAll(label2,passwordField);
        hb2.setSpacing(10);

        Button btn1=new Button("登录");

        btn1.setOnAction((ActionEvent e)->{//点击事件
            if (userClientService.checkUser(textField1.getText(), passwordField.getText())){
                System.out.println("登录成功");

                ClientUI_Menu menu  = new ClientUI_Menu(userClientService,messageClientService,fileClientService);//创建menu窗口
                try {
                    menu.start(new Stage());//打开menu窗口
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                primaryStage.hide();//点开新的界面后，关闭此界面
            }
            else System.out.println("登录失败");


            /*ClientUI_Menu menu  = new ClientUI_Menu();//创建menu窗口
            try {
                menu.start(new Stage());//打开menu窗口
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            primaryStage.hide();//点开新的界面后，关闭此界面*/
        });

        DropShadow shadow = new DropShadow();//阴影效果
        shadow.setRadius(5.0);//阴影范围
        shadow.setOffsetX(0.0);//偏移距离
        shadow.setOffsetY(0.0);//偏移距离
        shadow.setSpread(0.0);//模糊度
        shadow.setColor(Color.color(0.0, 0.9, 1));//阴影颜色
        btn1.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {//获得焦点
            btn1.setEffect(shadow);
        });
        btn1.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {//失去焦点
            btn1.setEffect(null);
        });

        vb1.getChildren().addAll(hb1,hb2,btn1);
        root.setCenter(vb1);

        //primaryStage.initStyle(StageStyle.TRANSPARENT);//无窗口装饰
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"))
        primaryStage.show();
    }


    //显示主菜单
    private void mainMenu() {







        /*while (loop) {

            System.out.println("===========欢迎登录网络通信系统===========");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入你的选择: ");
            key = Utility.readString(1);

            //根据用户的输入，来处理不同的逻辑
            switch (key) {
                case "1":
                    System.out.print("请输入用户号: ");
                    String userId = Utility.readString(50);
                    System.out.print("请输入密  码: ");
                    String pwd = Utility.readString(50);
                    //这里就比较麻烦了, 需要到服务端去验证该用户是否合法
                    //这里有很多代码, 我们这里编写一个类 UserClientService[用户登录/注册]
                    if (userClientService.checkUser(userId, pwd)) { //还没有写完, 先把整个逻辑打通....
                        System.out.println("===========欢迎 (用户 " + userId + " 登录成功) ===========");
                        //进入到二级菜单
                        while (loop) {
                            System.out.println("\n=========网络通信系统二级菜单(用户 " + userId + " )=======");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.print("请输入你的选择: ");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    //这里老师准备写一个方法，来获取在线用户列表
                                    //userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("请输入想对大家说的话: ");
                                    String s = Utility.readString(100);
                                    messageClientService.sendMessageToAll(s, userId);
                                    break;
                                case "3":
                                    System.out.print("请输入想聊天的用户号(在线): ");
                                    String getterId = Utility.readString(50);
                                    System.out.print("请输入想说的话: ");
                                    String content = Utility.readString(100);
                                    //编写一个方法，将消息发送给服务器端
                                    messageClientService.sendMessageToOne(content, userId, getterId);
                                    break;
                                case "4":
                                    System.out.print("请输入你想把文件发送给的用户(在线用户): ");
                                    getterId = Utility.readString(50);
                                    System.out.print("请输入发送文件的路径(形式 d:\\xx.jpg)");
                                    String src = Utility.readString(100);
                                    System.out.print("请输入把文件发送到对应的路径(形式 d:\\yy.jpg)");
                                    String dest = Utility.readString(100);
                                    fileClientService.sendFileToOne(src,dest,userId,getterId);
                                    break;
                                case "9":
                                    //调用方法，给服务器发送一个退出系统的message
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }

                        }
                    } else { //登录服务器失败
                        System.out.println("=========登录失败=========");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
            }

        }*/

    }
}
