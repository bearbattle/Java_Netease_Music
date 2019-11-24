package com.bear.neteasemusic.panel;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.bear.neteasemusic.Main;
import com.bear.neteasemusic.api.*;

import com.victorlaerte.asynctask.AsyncTask;

import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainView {

    private Stage stage;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Tab tabSignature;

    @FXML
    private ListView<GetUserPlaylistsResponse.Playlist> listPlaylists;

    @FXML
    private TableView<GetPlaylistDetailResponse.Track> tablePlaylistTracks;

    @FXML
    private TableColumn<GetPlaylistDetailResponse.Track, Integer> columnNumber;

    @FXML
    private ImageView avatarView;

    @FXML
    private Label labelPlaylistDescription;

    @FXML
    private TableColumn<GetPlaylistDetailResponse.Track, String> columnArtistName;

    @FXML
    private TableColumn<GetPlaylistDetailResponse.Track, String> columnAlbumName;

    @FXML
    private Label labelNickname;

    @FXML
    private Label labelPlaylistTitle;

    @FXML
    private TableColumn<GetPlaylistDetailResponse.Track, String> columnName;

    @FXML
    void initialize() {
        assert tabSignature != null : "fx:id=\"tabSignature\" was not injected: check your FXML file 'MainView.fxml'.";
        assert listPlaylists != null : "fx:id=\"listPlaylists\" was not injected: check your FXML file 'MainView.fxml'.";
        assert tablePlaylistTracks != null : "fx:id=\"listPlaylistTracks\" was not injected: check your FXML file 'MainView.fxml'.";
        assert avatarView != null : "fx:id=\"avatarView\" was not injected: check your FXML file 'MainView.fxml'.";
        assert labelPlaylistDescription != null : "fx:id=\"labelPlaylistDescription\" was not injected: check your FXML file 'MainView.fxml'.";
        assert labelNickname != null : "fx:id=\"labelNickname\" was not injected: check your FXML file 'MainView.fxml'.";
        assert labelPlaylistTitle != null : "fx:id=\"labelPlaylistTitle\" was not injected: check your FXML file 'MainView.fxml'.";


        TaskGetBasicInfo getBasicInfo = new TaskGetBasicInfo();
        getBasicInfo.getBasicInfo();

        TaskGetUserPlaylists getUserPlaylists = new TaskGetUserPlaylists();
        getUserPlaylists.execute();

        columnNumber.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<GetPlaylistDetailResponse.Track, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<GetPlaylistDetailResponse.Track, Integer> param) {
                return new ReadOnlyObjectWrapper<Integer>(0);
            }
        });

        columnNumber.setCellFactory(new Callback<TableColumn<GetPlaylistDetailResponse.Track, Integer>, TableCell<GetPlaylistDetailResponse.Track, Integer>>() {
            @Override
            public TableCell<GetPlaylistDetailResponse.Track, Integer> call(TableColumn<GetPlaylistDetailResponse.Track, Integer> param) {
                return new TableCell<GetPlaylistDetailResponse.Track, Integer>(){
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);

                        if(this.getTableRow() != null && item != null){
                            setText(this.getTableRow().getIndex()+1+"");
                        }else{
                            setText("");
                        }
                    }
                };
            }
        });

        listPlaylists.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    TaskGetPlaylistDetail getPlaylistDetail = new TaskGetPlaylistDetail(newValue.id);
                    getPlaylistDetail.execute();
                }
        );
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
            listPlaylists.setOrientation(Orientation.VERTICAL);
            int i;

            for (i = 0; i < 10000; i++) {
                listPlaylists.setOpacity(1.0 * i / 10000);
            }
            for (i = 10000; i > 0; i--) {
                listPlaylists.setOpacity(1.0 * i / 10000);
            }
        }

        @Override
        public Boolean doInBackground(Void... params) {
            System.out.println("正在获取您的歌单～");
            try {
                playlistsResponse = GetUserPlaylistsResponse.parse(api.postRequest(playlistsRequest));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//                alert.setTitle("错误！");
//                alert.setHeaderText(null);
//                alert.setContentText(e.toString());
//                alert.initOwner(stage);
//                alert.showAndWait();
//                alert.close();
//                e.printStackTrace();
            }
            return playlistsResponse.isOk();
        }

        @Override
        public void onPostExecute(Boolean success) {
            if (success) {
                listPlaylists.setItems(FXCollections.observableArrayList(playlistsResponse.getLists()));
                listPlaylists.setOpacity(1.0);
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("不好意思！");
                alert.setHeaderText("嘤嘤嘤～歌单获取失败了啦～");
                alert.setContentText(playlistsResponse.errorReason());
                alert.initOwner(stage);

                Optional<ButtonType> result;
                result = alert.showAndWait();
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

    class TaskGetPlaylistDetail extends AsyncTask<Void, Void, Boolean> {

        GetPlaylistDetailRequest playlistDetailRequest;
        GetPlaylistDetailResponse playlistDetailResponse;

        private NeteaseAPI api = Main.api;

        private int playlistID;

        TaskGetPlaylistDetail(int playlistID) {
            this.playlistID = playlistID;
        }

        @Override
        public void onPreExecute() {
            labelPlaylistTitle.setText("正在载入歌单～");
            labelPlaylistDescription.setText("正在载入歌单～");
            columnName.setCellValueFactory(
                    cellData -> new ReadOnlyStringWrapper(cellData.getValue().name)
            );
            columnArtistName.setCellValueFactory(
                    cellData -> new ReadOnlyStringWrapper(cellData.getValue().artistName)
            );
            columnAlbumName.setCellValueFactory(
                    cellData -> new ReadOnlyStringWrapper(cellData.getValue().albumName)
            );
        }

        @Override
        public Boolean doInBackground(Void... params) {
            playlistDetailRequest = new GetPlaylistDetailRequest(playlistID);
            try {
                playlistDetailResponse = GetPlaylistDetailResponse.parse(api.postRequest(playlistDetailRequest));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return playlistDetailResponse.isOk();
        }

        @Override
        public void onPostExecute(Boolean success) {
            if (success) {
                labelPlaylistTitle.setText(playlistDetailResponse.getName());
//                labelPlaylistDescription.setText(playlistDetailResponse.get);
                tablePlaylistTracks.setItems(FXCollections.observableArrayList(playlistDetailResponse.getTracks()));
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("不好意思！");
                alert.setHeaderText("嘤嘤嘤～歌单详情获取失败了啦～");
                alert.setContentText(playlistDetailResponse.errorReason());
                alert.initOwner(stage);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isEmpty()) {
                    alert.close();
                }
                // alert is exited, no button has been pressed.
                else if (result.get() == ButtonType.OK) {
                    this.execute();
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
