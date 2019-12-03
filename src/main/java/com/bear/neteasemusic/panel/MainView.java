package com.bear.neteasemusic.panel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import com.bear.neteasemusic.Lyric;
import com.bear.neteasemusic.Main;
import com.bear.neteasemusic.api.*;
import com.victorlaerte.asynctask.AsyncTask;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

public class MainView {

    @FXML
    public ImageView imgTrack;

    @FXML
    public TableView<TrackInfo> tableSearchTrack;

    @FXML
    public TableColumn<TrackInfo, Integer> columnNumber1;

    @FXML
    public TableColumn<TrackInfo, String> columnName1;

    @FXML
    public TableColumn<TrackInfo, String> columnArtistName1;

    @FXML
    public TableColumn<TrackInfo, String> columnAlbumName1;

    @FXML
    public TextField textKeyword;

    @FXML
    public TabPane tabPane;

    @FXML
    public Tab tabInfo;

    @FXML
    public Tab tabPlayer;

    @FXML
    public Tab tabSearch;


    private Stage stage;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="listPlaylists"
    private ListView<GetUserPlaylistsResponse.Playlist> listPlaylists; // Value injected by FXMLLoader

    @FXML // fx:id="tablePlaylistTracks"
    private TableView<TrackInfo> tablePlaylistTracks; // Value injected by FXMLLoader

    @FXML // fx:id="avatarView"
    private ImageView avatarView; // Value injected by FXMLLoader

    @FXML // fx:id="labelPlaylistDescription"
    private Label labelPlaylistDescription; // Value injected by FXMLLoader

    @FXML // fx:id="columnArtistName"
    private TableColumn<TrackInfo, String> columnArtistName; // Value injected by FXMLLoader

    @FXML // fx:id="labelDetail"
    private Label labelDetail; // Value injected by FXMLLoader

    @FXML // fx:id="labelTotalTime"
    private Label labelTotalTime; // Value injected by FXMLLoader

    @FXML // fx:id="iconNext"
    private SVGPath iconNext; // Value injected by FXMLLoader

    @FXML // fx:id="labelNickname"
    private Label labelNickname; // Value injected by FXMLLoader

    @FXML // fx:id="iconPrev"
    private SVGPath iconPrev; // Value injected by FXMLLoader

    @FXML // fx:id="iconPlay"
    private SVGPath iconPlay; // Value injected by FXMLLoader

    @FXML // fx:id="tabSignature"
    private Tab tabSignature; // Value injected by FXMLLoader

    @FXML // fx:id="iconMute"
    private SVGPath iconMute; // Value injected by FXMLLoader

    @FXML // fx:id="columnNumber"
    private TableColumn<TrackInfo, Integer> columnNumber; // Value injected by FXMLLoader

    @FXML // fx:id="labelCurTime"
    private Label labelCurTime; // Value injected by FXMLLoader

    @FXML // fx:id="columnAlbumName"
    private TableColumn<TrackInfo, String> columnAlbumName; // Value injected by FXMLLoader

    @FXML // fx:id="coverView"
    private ImageView coverView; // Value injected by FXMLLoader

    @FXML // fx:id="labelPlaylistTitle"
    private Label labelPlaylistTitle; // Value injected by FXMLLoader

    @FXML // fx:id="columnName"
    private TableColumn<TrackInfo, String> columnName; // Value injected by FXMLLoader

    @FXML // fx:id="labelTrackName"
    private Label labelTrackName; // Value injected by FXMLLoader

    @FXML
    public Slider progressBar;

    @FXML
    private Slider volumeBar;

    @FXML
    private Label labelLyric;

    private static String pauseSvg = "M28,21 L28,39 C28,39.553 27.552,40 27,40 C26.448,40 26,39.553 26,39 L26,22 L22,22 L22,38 L23,38 C23.552,38 24,38.447 24,39 C24,39.553 23.552,40 23,40 L21,40 C20.448,40 20,39.553 20,39 L20,21 C20,20.447 20.448,20 21,20 L27,20 C27.552,20 28,20.447 28,21 M40,21 L40,39 C40,39.553 39.552,40 39,40 C38.448,40 38,39.553 38,39 L38,22 L34,22 L34,38 L35,38 C35.552,38 36,38.447 36,39 C36,39.553 35.552,40 35,40 L33,40 C32.448,40 32,39.553 32,39 L32,21 C32,20.447 32.448,20 33,20 L39,20 C39.552,20 40,20.447 40,21 M30,58 C14.561,58 2,45.439 2,30 C2,14.561 14.561,2 30,2 C45.439,2 58,14.561 58,30 C58,45.439 45.439,58 30,58 M30,0 C13.458,0 0,13.458 0,30 C0,46.542 13.458,60 30,60 C46.542,60 60,46.542 60,30 C60,13.458 46.542,0 30,0";
    private static String playSvg = "M40,30 C40,30.34 39.827,30.657 39.541,30.841 L29.531,37.276 C29.065,37.574 28.448,37.44 28.149,36.977 C27.851,36.512 27.985,35.894 28.449,35.595 L37.151,30 L26,22.832 L26,39 C26,39.553 25.552,40 25,40 C24.448,40 24,39.553 24,39 L24,21 C24,20.634 24.2,20.298 24.521,20.122 C24.841,19.947 25.232,19.959 25.541,20.159 L39.541,29.159 C39.827,29.343 40,29.66 40,30 M30,58 C14.561,58 2,45.439 2,30 C2,14.561 14.561,2 30,2 C45.439,2 58,14.561 58,30 C58,45.439 45.439,58 30,58 M30,0 C13.458,0 0,13.458 0,30 C0,46.542 13.458,60 30,60 C46.542,60 60,46.542 60,30 C60,13.458 46.542,0 30,0";
    private static String muteSvg = "M30,25.7v8.5c0,0.2-0.1,0.3-0.2,0.4c-0.1,0-0.1,0.1-0.2,0.1c-0.1,0-0.2,0-0.3-0.1    l-2.8-1.9c-0.2-0.1-0.3-0.4-0.1-0.7c0.1-0.2,0.4-0.3,0.7-0.1l2.1,1.4v-6.7L27,28c-0.1,0.1-0.2,0.1-0.3,0.1h-1.4v4.3    c0,0.3-0.2,0.5-0.5,0.5s-0.5-0.2-0.5-0.5v-4.7c0-0.3,0.2-0.5,0.5-0.5h1.7l2.7-1.8c0.1-0.1,0.3-0.1,0.5,0    C29.9,25.4,30,25.6,30,25.7 M35.5,28.4L34,30l1.6,1.6c0.2,0.2,0.2,0.5,0,0.7c-0.1,0.1-0.2,0.1-0.3,0.1s-0.2,0-0.3-0.1l-1.6-1.6    l-1.6,1.6c-0.1,0.1-0.2,0.1-0.3,0.1s-0.2,0-0.3-0.1c-0.2-0.2-0.2-0.5,0-0.7l1.6-1.6l-1.6-1.6c-0.2-0.2-0.2-0.5,0-0.7    c0.2-0.2,0.5-0.2,0.7,0l1.6,1.6l1.6-1.6c0.2-0.2,0.5-0.2,0.7,0C35.7,28,35.7,28.3,35.5,28.4 M30,43.2c-7.3,0-13.2-5.9-13.2-13.2    S22.7,16.8,30,16.8S43.2,22.7,43.2,30S37.3,43.2,30,43.2 M30,15.8c-7.8,0-14.2,6.4-14.2,14.2S22.2,44.2,30,44.2S44.2,37.8,44.2,30    S37.8,15.8,30,15.8";
    private static String unmuteSvg = "M33.6,25c1.3,1.3,2.1,3.1,2.1,5c0,1.9-0.7,3.7-2.1,5c-0.1,0.1-0.2,0.1-0.3,0.1    c-0.1,0-0.2,0-0.3-0.1c-0.2-0.2-0.2-0.5,0-0.7c1.2-1.2,1.8-2.7,1.8-4.3c0-1.6-0.6-3.2-1.8-4.3c-0.2-0.2-0.2-0.5,0-0.7    C33.1,24.8,33.4,24.8,33.6,25 M32.8,30c0,1.1-0.4,2.2-1.2,3c-0.1,0.1-0.2,0.1-0.3,0.1c-0.1,0-0.2,0-0.3-0.1    c-0.2-0.2-0.2-0.5,0-0.7c0.6-0.6,1-1.5,1-2.3c0-0.9-0.3-1.7-1-2.3c-0.2-0.2-0.2-0.5,0-0.7c0.2-0.2,0.5-0.2,0.7,0    C32.4,27.8,32.8,28.9,32.8,30 M30,25.7v8.5c0,0.2-0.1,0.3-0.3,0.4c-0.1,0-0.1,0.1-0.2,0.1c-0.1,0-0.2,0-0.3-0.1l-2.8-1.9    c-0.2-0.1-0.3-0.4-0.1-0.7c0.1-0.2,0.4-0.3,0.7-0.1l2.1,1.4v-6.7L27,28c-0.1,0.1-0.2,0.1-0.3,0.1h-1.4v4.3c0,0.3-0.2,0.5-0.5,0.5    c-0.3,0-0.5-0.2-0.5-0.5v-4.7c0-0.3,0.2-0.5,0.5-0.5h1.7l2.7-1.8c0.1-0.1,0.3-0.1,0.5,0C29.9,25.4,30,25.6,30,25.7 M30,43.2    c-7.3,0-13.2-5.9-13.2-13.2c0-7.3,5.9-13.2,13.2-13.2c7.3,0,13.2,5.9,13.2,13.2C43.2,37.3,37.3,43.2,30,43.2 M30,15.8    c-7.8,0-14.2,6.4-14.2,14.2S22.2,44.2,30,44.2S44.2,37.8,44.2,30S37.8,15.8,30,15.8";

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert listPlaylists != null : "fx:id=\"listPlaylists\" was not injected: check your FXML file 'MainView.fxml'.";
        assert tablePlaylistTracks != null : "fx:id=\"tablePlaylistTracks\" was not injected: check your FXML file 'MainView.fxml'.";
        assert avatarView != null : "fx:id=\"avatarView\" was not injected: check your FXML file 'MainView.fxml'.";
        assert labelPlaylistDescription != null : "fx:id=\"labelPlaylistDescription\" was not injected: check your FXML file 'MainView.fxml'.";
        assert columnArtistName != null : "fx:id=\"columnArtistName\" was not injected: check your FXML file 'MainView.fxml'.";
        assert labelDetail != null : "fx:id=\"labelDetail\" was not injected: check your FXML file 'MainView.fxml'.";
        assert labelTotalTime != null : "fx:id=\"labelTotalTime\" was not injected: check your FXML file 'MainView.fxml'.";
        assert iconNext != null : "fx:id=\"iconNext\" was not injected: check your FXML file 'MainView.fxml'.";
        assert labelNickname != null : "fx:id=\"labelNickname\" was not injected: check your FXML file 'MainView.fxml'.";
        assert iconPrev != null : "fx:id=\"iconPrev\" was not injected: check your FXML file 'MainView.fxml'.";
        assert iconPlay != null : "fx:id=\"iconPlay\" was not injected: check your FXML file 'MainView.fxml'.";
        assert tabSignature != null : "fx:id=\"tabSignature\" was not injected: check your FXML file 'MainView.fxml'.";
        assert iconMute != null : "fx:id=\"iconMute\" was not injected: check your FXML file 'MainView.fxml'.";
        assert columnNumber != null : "fx:id=\"columnNumber\" was not injected: check your FXML file 'MainView.fxml'.";
        assert labelCurTime != null : "fx:id=\"labelCurTime\" was not injected: check your FXML file 'MainView.fxml'.";
        assert columnAlbumName != null : "fx:id=\"columnAlbumName\" was not injected: check your FXML file 'MainView.fxml'.";
        assert coverView != null : "fx:id=\"coverView\" was not injected: check your FXML file 'MainView.fxml'.";
        assert labelPlaylistTitle != null : "fx:id=\"labelPlaylistTitle\" was not injected: check your FXML file 'MainView.fxml'.";
        assert columnName != null : "fx:id=\"columnName\" was not injected: check your FXML file 'MainView.fxml'.";
        assert labelTrackName != null : "fx:id=\"labelTrackName\" was not injected: check your FXML file 'MainView.fxml'.";


        TaskGetBasicInfo getBasicInfo = new TaskGetBasicInfo();
        getBasicInfo.getBasicInfo();

        TaskGetUserPlaylists getUserPlaylists = new TaskGetUserPlaylists();
        getUserPlaylists.execute();

        columnNumber.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<TrackInfo, Integer> param) {
                return new ReadOnlyObjectWrapper<Integer>(0);
            }
        });

        columnNumber.setCellFactory(new Callback<>() {
            @Override
            public TableCell<TrackInfo, Integer> call(TableColumn<TrackInfo, Integer> param) {
                return new TableCell<TrackInfo, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && item != null) {
                            setText(this.getTableRow().getIndex() + 1 + "");
                        } else {
                            setText("");
                        }
                    }

                };
            }
        });

        columnNumber1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TrackInfo, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<TrackInfo, Integer> param) {
                return new ReadOnlyObjectWrapper<Integer>(0);
            }
        });

        columnNumber1.setCellFactory(new Callback<TableColumn<TrackInfo, Integer>, TableCell<TrackInfo, Integer>>() {
            @Override
            public TableCell<TrackInfo, Integer> call(TableColumn<TrackInfo, Integer> param) {
                return new TableCell<TrackInfo, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && item != null) {
                            setText(this.getTableRow().getIndex() + 1 + "");
                        } else {
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

        tablePlaylistTracks.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.isFee){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("收费歌曲无法播放");
                        alert.initOwner(stage);
                        alert.showAndWait();
                        return;
                    }
                    TaskGetTrackUrl getTrackUrl = new TaskGetTrackUrl(newValue);
                    getTrackUrl.execute();
                    new TaskGetTrackCover(newValue).execute();
                    new TaskGetLyric(newValue).execute();
                    tabPane.getSelectionModel().select(tabPlayer);
                }
        );

        tableSearchTrack.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.isFee){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("收费歌曲无法播放");
                        alert.initOwner(stage);
                        alert.showAndWait();
                        return;
                    }
                    TaskGetTrackUrl getTrackUrl = new TaskGetTrackUrl(newValue);
                    getTrackUrl.execute();
                    new TaskGetTrackCover(newValue).execute();
                    new TaskGetLyric(newValue).execute();
                    tabPane.getSelectionModel().select(tabPlayer);
                }
        );
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        Main.logoutStatus = true;
        Main.prop.setProperty("autoLogin", "false");
        FileOutputStream fout = new FileOutputStream(Main.propFile);
        Main.prop.store(fout, "");
        fout.close();
        stage.close();
    }

    public void searchTrack(ActionEvent actionEvent) {
        class TaskSearch extends AsyncTask<Void, Void, Boolean> {
            private NeteaseAPI api = Main.api;

            SearchRequest searchRequest;
            SearchResponse searchResponse;

            @Override
            public void onPreExecute() {
                columnName1.setCellValueFactory(
                        cellData -> new ReadOnlyStringWrapper(cellData.getValue().name + (cellData.getValue().isFee ? String.format(" (收费 %d)", cellData.getValue().feeValue)  : ""))
                );
                columnArtistName1.setCellValueFactory(
                        cellData -> new ReadOnlyStringWrapper(cellData.getValue().artistName)
                );
                columnAlbumName1.setCellValueFactory(
                        cellData -> new ReadOnlyStringWrapper(cellData.getValue().albumName)
                );
            }

            @Override
            public Boolean doInBackground(Void... params) {
                searchRequest = new SearchRequest(textKeyword.getText());
                try {
                    searchResponse = SearchResponse.parse(api.postRequest(searchRequest));
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                return searchResponse.isOk();
            }

            @Override
            public void onPostExecute(Boolean params) {
                if (params) {
                    tableSearchTrack.setItems(FXCollections.observableArrayList(searchResponse.getTracks()));
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("不好意思！");
                    alert.setHeaderText("嘤嘤嘤～歌单获取失败了啦～");
                    alert.setContentText(searchResponse.errorReason());
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

        new TaskSearch().execute();
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

        private long playlistID;

        TaskGetPlaylistDetail(long playlistID) {
            this.playlistID = playlistID;
        }

        @Override
        public void onPreExecute() {
            labelPlaylistTitle.setText("正在载入歌单～");
            labelPlaylistDescription.setText("正在载入歌单～");
            columnName.setCellValueFactory(
                    cellData -> new ReadOnlyStringWrapper(cellData.getValue().name + (cellData.getValue().isFee ? String.format(" (收费 %d)", cellData.getValue().feeValue) : ""))
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
                return false;
            }
            return playlistDetailResponse.isOk();
        }

        @Override
        public void onPostExecute(Boolean success) {
            if (success) {
                labelPlaylistTitle.setText(playlistDetailResponse.getName());
                labelPlaylistDescription.setText(playlistDetailResponse.getDescription() == null ? "无" : playlistDetailResponse.getDescription());
                tablePlaylistTracks.setItems(FXCollections.observableArrayList(playlistDetailResponse.getTracks()));
                coverView.setImage(new Image(playlistDetailResponse.getCoverUrl()));
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

    Media music;
    MediaPlayer player;

    ChangeListener<? super Number> progressBarListener;

    class TaskGetTrackUrl extends AsyncTask<Void, Void, Boolean> {

        GetTrackUrlRequest trackUrlRequest;
        GetTrackUrlResponse trackUrlResponse;

        private NeteaseAPI api = Main.api;

        TrackInfo track;
        private long trackID;

        public void setTrack(TrackInfo track) {
            this.track = track;
        }

        public TaskGetTrackUrl(TrackInfo track) {
            setTrack(track);
            this.trackID = track.id;
        }

        @Override
        public void onPreExecute() {
            labelTrackName.setText("正在载入歌曲～");
            labelDetail.setText("正在载入歌曲～");
        }

        @Override
        public Boolean doInBackground(Void... params) {
            trackUrlRequest = new GetTrackUrlRequest(trackID, GetTrackUrlRequest.Rate.mp3_320);
            try {
                trackUrlResponse = GetTrackUrlResponse.parse(api.postRequest(trackUrlRequest));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return trackUrlResponse.isOk();
        }

        @Override
        public void onPostExecute(Boolean success) {
            if (success) {
                if (player != null) {
                    player.dispose();
                    player = null;
                }

                if (progressBarListener != null) {
                    progressBar.valueProperty().removeListener(progressBarListener);
                    progressBarListener = null;
                }

                System.out.println(trackUrlResponse.getUrl());

                try {
                    music = new Media(trackUrlResponse.getUrl());
                    player = new MediaPlayer(music);
                } catch (Exception e) {
                    e.printStackTrace();
                    labelTrackName.setText("载入歌曲失败了1551～");
                    labelDetail.setText("载入歌曲失败了1551～");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("不好意思！");
                    alert.setHeaderText("载入歌曲失败了1551～");
                    alert.setContentText(trackUrlResponse.errorReason() + "点击确定按钮重试！～");
                    alert.initOwner(stage);
                    alert.showAndWait();
                }
                player.volumeProperty().bindBidirectional(volumeBar.valueProperty());
                AtomicBoolean isSeeking = new AtomicBoolean(true);
                player.totalDurationProperty().addListener(
                        (observable, oldValue, newValue) -> {
                            labelTotalTime.setText(durationToString(newValue));
                        }
                );

                player.statusProperty().addListener(
                        (observable, oldValue, newValue) -> {
                            System.out.println(newValue);
                            switch (newValue) {
                                case PLAYING:
                                    iconPlay.setContent(pauseSvg);
                                    break;
                                default:
                                    iconPlay.setContent(playSvg);
                            }
                        }
                );
                player.currentTimeProperty().addListener(
                        (observable, oldValue, newValue) -> {
                            String curLyric;
                            if (lyric != null && lyric.isPureMusic()) {
                                curLyric = "纯音乐，请欣赏";
                            } else if (lyric != null && lyric.isLyricMissing()) {
                                curLyric = "当前歌曲暂无歌词";
                            } else if (parsedLyric != null) {
                                curLyric = parsedLyric.GetCurrentLyric((int) newValue.toMillis());
                            } else {
                                curLyric = "获取歌词失败";
                            }
                            labelLyric.setText(curLyric);
                            labelCurTime.setText(durationToString(newValue));
                            isSeeking.set(false);
                            progressBar.setValue(newValue.toMillis() / player.getTotalDuration().toMillis() * 1000);
                            isSeeking.set(true);
                        }
                );
                progressBarListener = (observable, oldValue, newValue) -> {
                    if (isSeeking.get())
                        player.seek(Duration.millis(newValue.doubleValue() / 1000 * player.getTotalDuration().toMillis()));
                };
                progressBar.valueProperty().addListener(progressBarListener);

                labelTrackName.setText(track.name);
                labelDetail.setText(track.artistName + "-" + track.albumName);
                player.setAutoPlay(true);
                player.errorProperty().addListener((f,u,c)->{
                    c.printStackTrace();
                });
                try {
                    player.play();
                    System.out.println(player);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                player.setOnStalled(
                        () -> {
                            new Alert(Alert.AlertType.WARNING).show();
                        }
                );
            } else {
                labelTrackName.setText("载入歌曲失败了1551～");
                labelDetail.setText("载入歌曲失败了1551～");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("不好意思！");
                alert.setHeaderText("载入歌曲失败了1551～");
                alert.setContentText(trackUrlResponse.errorReason() + "点击确定按钮重试！～");
                alert.initOwner(stage);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isEmpty()) {
                    alert.close();
                }
                // alert is exited, no button has been pressed.
                else if (result.get() == ButtonType.OK) {

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

    public void PlayOrPause(MouseEvent mouseEvent) {
        if (player.getStatus() == MediaPlayer.Status.PLAYING) {
            player.pause();
        } else if (player.getStatus() == MediaPlayer.Status.PAUSED || player.getStatus() == MediaPlayer.Status.HALTED) {
            player.play();
        }
    }

    private String durationToString(Duration d) {
        return String.format("%02d:%02d", (int) d.toMinutes(), (int) (d.toSeconds() - (int) (d.toMinutes()) * 60));
    }

    public void muteOrUnmute(MouseEvent mouseEvent) {
        if (player != null) {
            if (player.isMute()) {
                player.setMute(false);
                iconMute.setContent(unmuteSvg);
            } else {
                player.setMute(true);
                iconMute.setContent(muteSvg);
            }
        }
    }


    GetLyricsResponse lyric;
    Lyric parsedLyric;

    class TaskGetLyric extends AsyncTask<Void, Void, Boolean> {

        GetLyricsRequest lyricsRequest;
        GetLyricsResponse lyricsResponse;

        private NeteaseAPI api = Main.api;

        TrackInfo track;
        private long trackID;

        public void setTrack(TrackInfo track) {
            this.track = track;
        }

        public TaskGetLyric(TrackInfo track) {
            setTrack(track);
            this.trackID = track.id;
        }

        @Override
        public void onPreExecute() {
            labelLyric.setText("正在获取歌词");
        }

        @Override
        public Boolean doInBackground(Void... params) {
            lyricsRequest = new GetLyricsRequest(trackID);
            try {
                lyricsResponse = GetLyricsResponse.parse(api.postRequest(lyricsRequest));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return lyricsResponse.isOk();
        }

        @Override
        public void onPostExecute(Boolean success) {
            if (success) {
                lyric = lyricsResponse;
                if (!lyric.isLyricMissing() && !lyric.isPureMusic()) {
                    parsedLyric = new Lyric(lyric.getLyric());
                } else {
                    parsedLyric = null;
                }
            } else {
                lyric = null;
            }
        }

        @Override
        public void progressCallback(Void... params) {

        }
    }


    class TaskGetTrackCover extends AsyncTask<Void, Void, Boolean> {

        TrackInfo info;

        public TaskGetTrackCover(TrackInfo info) {
            this.info = info;
        }

        @Override
        public void onPreExecute() {

        }

        @Override
        public Boolean doInBackground(Void... params) {
            Image img = new Image(info.albumCoverUrl);
            imgTrack.setImage(img);
            return true;
        }

        @Override
        public void onPostExecute(Boolean params) {

        }

        @Override
        public void progressCallback(Void... params) {

        }
    }
}
