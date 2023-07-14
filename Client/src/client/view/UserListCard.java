package client.view;

import client.service.FileClientService;
import client.service.MessageClientService;
import client.service.UserClientService;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class UserListCard{
    String ImgPath;//图片地址
    String Name;//用户名称
    String Sig;//用户签名
    String Id;//用户id


    String myId;//我的id，方便查找线程

    private UserClientService TempUserClientService;
    private MessageClientService TempMessageClientService;
    private FileClientService TempFileClientService;
    public void setServer(UserClientService a,MessageClientService b,FileClientService c){
        this.TempUserClientService=a;
        this.TempMessageClientService=b;
        this.TempFileClientService=c;
    }
    Stage MainStage;

    public void setMainStage(Stage mainStage) {
        MainStage = mainStage;
    }

    public Stage getMainStage() {
        return MainStage;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getMyId() {
        return myId;
    }

    ChatView chatView;//用户聊天窗口

    public void setChatView(ChatView chatView) {
        this.chatView = chatView;
    }

    public ChatView getChatView() {
        return chatView;
    }

    public void setUserId(String id) {
        Id = id;
    }

    public String geUsertId() {
        return Id;
    }

    public UserListCard(String ImgPath, String Name, String Sig,String Id){//图片地址，用户名称，用户签名,用户Id
        this.ImgPath=ImgPath;
        this.Name=Name;
        this.Sig=Sig;
        this.Id=Id;
    }
    public void setUserListCard(String ImgPath, String Name, String Sig){//图片地址，用户名称，用户签名
        this.ImgPath=ImgPath;
        this.Name=Name;
        this.Sig=Sig;
    }
    int openCount=0;

    public int getOpenCount() {
        return openCount;
    }

    public void setOpenCount(int openCount) {
        this.openCount = openCount;
    }

    public HBox getUserListCard(){
        //用户头像
        HBox UserCard=new HBox();
        UserCard.setPrefSize(400,50);

        Image img = new Image(ImgPath);
        ImageView imageView = new ImageView();
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setImage(img);
        UserCard.getChildren().add(imageView);

        //用户名称
        VBox UserInf=new VBox();

        Text UserName=new Text(Name);
        UserName.setFont(Font.font(20));
        VBox.setMargin(UserName,new Insets(0,0,0,5));
        //用户签名
        Text UserSig=new Text(Sig);
        UserSig.setFont(Font.font(15));
        VBox.setMargin(UserSig,new Insets(5,0,0,5));

        UserInf.getChildren().addAll(UserName,UserSig);
        UserCard.getChildren().add(UserInf);

        //点击事件
        UserCard.setOnMousePressed(new EventHandler<MouseEvent>() {//点击某好友后，打开聊天窗口
            @Override
            public void handle(MouseEvent event) {
                System.out.println(UserName.getText());
                try {
                    chatView.setServer(TempUserClientService,TempMessageClientService,TempFileClientService);
                    chatView.setMyId(myId);
                    chatView.setTitle(Name+"("+Id+")");
                    //chatView.stop();
                    //chatView.getChatStage().hide();

                    if(openCount==0) {
                        chatView.start(chatView.getChatStage());//打开聊天窗口
                        setOpenCount(1);
                    }
                    chatView.getChatStage().show();

                    System.out.println(chatView.getChatStage().getX());
                    System.out.println(chatView.getChatStage().getY());
                    System.out.println(getMainStage().getX());
                    System.out.println(getMainStage().getY());

                    chatView.getChatStage().setX(getMainStage().getX()+440);
                    chatView.getChatStage().setY(getMainStage().getY());

                    //chatView.start(stage);//打开聊天窗口

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                //primaryStage.hide();//点开新的界面后，关闭此界面
            }
        });

        return UserCard;
    }
}
