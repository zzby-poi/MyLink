package client.view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AboutView extends Application {
    public void start(Stage primaryStage) throws Exception {
        BorderPane root=new BorderPane();
        primaryStage.setScene(new Scene(root, 400, 230));
        //primaryStage.initStyle(StageStyle.TRANSPARENT);//无窗口装饰

        /*root.setStyle("-fx-border-radius: 0.5");
        root.setStyle("-fx-border-color: black");*/
        //"-box-shadow: 10px 10px 20px 10px rgba(255,255,0,0.5), -10px 10px 10px 10px rgba(255,255,255,0.5)"

        /*DropShadow dropshadow = new DropShadow();// 阴影向外
        dropshadow.setRadius(10);// 颜色蔓延的距离
        dropshadow.setOffsetX(0);// 水平方向，0则向左右两侧，正则向右，负则向左
        dropshadow.setOffsetY(0);// 垂直方向，0则向上下两侧，正则向下，负则向上
        dropshadow.setSpread(0.1);// 颜色变淡的程度
        dropshadow.setColor(Color.BLACK);// 设置颜色
        root.setEffect(dropshadow);// 绑定指定窗口控件*/
        primaryStage.initStyle(StageStyle.TRANSPARENT);//无窗口装饰


        VBox vb=new VBox();
        //标题栏设计
        HBox top=new HBox();
        DragUtil.addDragListener(primaryStage, top);//设置拖动
        top.setPrefSize(400,30);

        Label appName=new Label("关于");
        appName.setFont(Font.font(18));
        appName.setPadding(new Insets(5,313,0,10));
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

        VBox infBox=new VBox();
        Label label1=new Label("MyLink");
        label1.setFont(Font.font(25));
        infBox.getChildren().add(label1);
        label1.setPadding(new Insets(0,0,20,0));


        Label label2=new Label("Made By:zzby");
        label2.setFont(Font.font(18));
        infBox.getChildren().add(label2);

        Label label3=new Label("QQ:392080607(zzby1100@qq.com)");
        label3.setFont(Font.font(18));
        infBox.getChildren().add(label3);

        infBox.setPadding(new Insets(10,0,0,20));
        vb.getChildren().add(infBox);

        root.setCenter(vb);
    }
}
