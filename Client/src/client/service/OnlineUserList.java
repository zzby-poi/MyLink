package client.service;


import client.view.UserListCard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class OnlineUserList{
    ListView listView;
    String MyName;

    public void setMyName(String myName) {
        MyName = myName;
    }

    public String getMyName() {
        return MyName;
    }

    ObservableList<HBox> items = FXCollections.observableArrayList();
    int index=0;//行数

    public void clearList(){
        listView.getItems().clear();
    }


    public OnlineUserList(ListView listView) {
        this.listView=listView;
    }
    public void setOnlineUserList(ListView listView){
        this.listView=listView;
    }
    public ListView getOnlineUserList(){
        return listView;
    }
    public void addUser(HBox hBox){
        items.add(index++,hBox);
        listView.setItems(items);
    }
}
