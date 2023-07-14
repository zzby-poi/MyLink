package client.view;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ChatBubble {
    String imgPath;//图片路径
    String text;//消息内容
    int WordLength;//每行消息长度
    public ChatBubble(String text){
        this.imgPath="/client/view/date/user_mini_img.png";
        this.text=text;
        WordLength=20;
    }
    public ChatBubble(String imgPath,String text){
        this.imgPath=imgPath;
        this.text=text;
        WordLength=20;
    }
    public ChatBubble(String imgPath,String text,int WordLength){
        this.imgPath=imgPath;
        this.text=text;
        this.WordLength=WordLength;
    }

    public HBox getChatBubble(String type){//返回气泡 sender=发送者 getter=接收者
        HBox hBox=new HBox();//水平容器


        Image img = new Image(imgPath);
        ImageView imageView = new ImageView();
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setImage(img);
        hBox.setMargin(imageView,new Insets(0,0,0,10));
        //发送者消息
        VBox vBox=new VBox();//垂直容器，按行放置文字
        hBox.setMargin(vBox,new Insets(0,0,0,10));

        double width=0;//Text的宽度
        for(int i=0;i<text.length();i+=WordLength) {
            Text t=new Text();
            t.setFont(Font.font(20));

            if(i+WordLength<text.length()) {
                t.setText(text.substring(i,i+WordLength));
            }
            else {
                t.setText(text.substring(i,text.length()));
            }
            vBox.getChildren().add(t);
            if(i==0) width=t.getBoundsInLocal().getWidth();


        }
        if(type.equals("sender")){//发送者:图片在左
            hBox.getChildren().add(imageView);
            hBox.getChildren().add(vBox);
        }else{//接受者:图片在右
            hBox.setPadding(new Insets(0, 0, 0, 450-width+imageView.getFitWidth()));
            hBox.getChildren().add(vBox);
            hBox.getChildren().add(imageView);
        }
        return hBox;
    }
}
