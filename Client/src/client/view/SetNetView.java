package client.view;

import client.service.UserClientService;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.*;

public class SetNetView extends Application {
    private UserClientService userClientService;
    public void setUserClientService(UserClientService userClientService) {
        this.userClientService = userClientService;
    }
    public void start(Stage primaryStage) throws Exception {
        BorderPane root=new BorderPane();
        primaryStage.setScene(new Scene(root, 400, 230));
        primaryStage.initStyle(StageStyle.TRANSPARENT);//无窗口装饰

        VBox vb=new VBox();


        //标题栏设计
        HBox top=new HBox();
        DragUtil.addDragListener(primaryStage, top);//设置拖动
        top.setPrefSize(400,30);

        Label appName=new Label("网络设置");
        appName.setFont(Font.font(18));
        appName.setPadding(new Insets(5,277,0,10));
        top.getChildren().add(appName);

        Button closeBtn=new Button("X");
        closeBtn.setPrefSize(40,30);
        top.setMargin(closeBtn,new Insets(0,0,0,0));
        top.getChildren().add(closeBtn);
        closeBtn.setOnAction((ActionEvent e)->{//点击事件,关闭窗口
            primaryStage.close();
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

        vb.getChildren().add(top);

        Separator separator=new Separator();//分隔符
        vb.getChildren().add(separator);


        TextField ipField=new TextField();
        ipField.setPromptText("IP"); // 设置输入框的提示语
        ipField.setPrefSize(200,40);
        ipField.setMaxWidth(300);
        vb.setMargin(ipField,new Insets(20,0,0,50));
        vb.getChildren().add(ipField);

        TextField portField=new TextField();
        portField.setPromptText("端口"); // 设置输入框的提示语
        portField.setPrefSize(200,40);
        portField.setMaxWidth(300);
        vb.setMargin(portField,new Insets(20,0,0,50));
        vb.getChildren().add(portField);

        Button btn=new Button("确定");
        btn.setPrefSize(100,40);
        vb.setMargin(btn,new Insets(20,0,0,150));
        vb.getChildren().add(btn);

        btn.setOnAction((ActionEvent e) -> {//保存ip和端口号并关闭窗口
            if((!ipField.getText().equals(""))&&(!portField.getText().equals("")))
            {
                SetNetView net=new SetNetView();
                try {
                    userClientService.setNet(ipField.getText(),portField.getText());
                    //存入setting.txt
                    File file=new File("setting.txt");
                    file.delete();
                    file.createNewFile();
                    FileWriter fw=new FileWriter(file,true);
                    BufferedWriter bufw=new BufferedWriter(fw);
                    bufw.write(ipField.getText()+":"+portField.getText());
                    System.out.println(ipField.getText()+":"+portField.getText());
                    bufw.close();
                    fw.close();
                    primaryStage.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        root.setCenter(vb);
        //primaryStage.show();
    }
}
