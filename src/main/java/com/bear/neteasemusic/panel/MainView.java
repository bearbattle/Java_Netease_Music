package com.bear.neteasemusic.panel;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.bear.neteasemusic.Main;
import com.bear.neteasemusic.api.GetUserPlaylistsRequest;
import com.bear.neteasemusic.api.GetUserPlaylistsResponse;
import com.bear.neteasemusic.api.NeteaseAPI;

import com.victorlaerte.asynctask.AsyncTask;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MainView {

    private Stage stage;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Tab tabSignature;

    @FXML
    private ImageView avatarView;

    @FXML
    private Label labelNickname;

    @FXML
    public ListView listPlaylists;

    @FXML
    void initialize() {
        assert tabSignature != null : "fx:id=\"tabSignature\" was not injected: check your FXML file 'MainView.fxml'.";
        assert avatarView != null : "fx:id=\"avatarView\" was not injected: check your FXML file 'MainView.fxml'.";
        assert labelNickname != null : "fx:id=\"labelNickname\" was not injected: check your FXML file 'MainView.fxml'.";


        TaskGetBasicInfo getBasicInfo = new TaskGetBasicInfo();
        getBasicInfo.getBasicInfo();

        TaskGetUserPlaylists getUserPlaylists = new TaskGetUserPlaylists();
        getUserPlaylists.execute();

    }

    class TaskGetBasicInfo {
        void getBasicInfo() {
            System.out.println("正在获取个人信息……");
            tabSignature.setText(Main.signature);
            labelNickname.setText(Main.nickName);
            avatarView.setFitWidth(150);
            avatarView.setFitHeight(150);
            avatarView.setImage(new Image(Main.avatarUrl.toString()));
        }
    }

    class TaskGetUserPlaylists extends AsyncTask<Void, Void, Boolean> {

        GetUserPlaylistsRequest playlistsRequest;
        GetUserPlaylistsResponse playlistsResponse;

        private int uid = Main.userId;
        private NeteaseAPI api = Main.api;

        @Override
        public void onPreExecute() {
            stage = (Stage) avatarView.getScene().getWindow();
            playlistsRequest = new GetUserPlaylistsRequest(uid);
            listPlaylists.setOrientation(Orientation.HORIZONTAL);
        }

        @Override
        public Boolean doInBackground(Void... params) {
            System.out.println("正在获取您的歌单～");
            try {
                playlistsResponse = GetUserPlaylistsResponse.parse(api.postRequest(playlistsRequest));
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("错误！");
                alert.setHeaderText(null);
                alert.setContentText(e.toString());
                alert.initOwner(stage);
                alert.showAndWait();
                alert.close();
                e.printStackTrace();
            }
            return playlistsResponse.isOk();
        }

        @Override
        public void onPostExecute(Boolean success) {
            int i;
            for (i = 0; i < Integer.MAX_VALUE; i++) {
                listPlaylists.setOpacity(1.0*i/Integer.MAX_VALUE);
            }
            for (i = Integer.MAX_VALUE; i > 0; i--) {
                listPlaylists.setOpacity(1.0*i/Integer.MAX_VALUE);
            }
            if (success) {
                listPlaylists.setItems((ObservableList) playlistsResponse.getLists());
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("不好意思！");
                alert.setHeaderText("嘤嘤嘤～歌单获取失败了啦～");
                alert.setContentText(playlistsResponse.errorReason());
                alert.initOwner(stage);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isEmpty()) {
                    alert.close();
                }
                // alert is exited, no button has been pressed.
                else if (result.get() == ButtonType.OK) {
                    this.doInBackground();
                }
                //oke button is pressed
                else if (result.get() == ButtonType.CANCEL) {
                    alert.close();
                }
                // cancel button is pressed
            }
        }

        @Override
        public void progressCallback(Void... params) {

        }
    }

}
