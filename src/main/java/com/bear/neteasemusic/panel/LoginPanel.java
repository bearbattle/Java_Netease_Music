package com.bear.neteasemusic.panel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import com.bear.neteasemusic.api.LoginRequest;
import com.bear.neteasemusic.api.LoginResponse;
import com.bear.neteasemusic.Main;

import com.victorlaerte.asynctask.AsyncTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginPanel {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private CheckBox checkAutoLogin;

    @FXML
    private PasswordField textPassword;

    @FXML
    private Button buttonLogin;

    @FXML
    private TextField textUsername;

    @FXML
    private CheckBox checkRememberPassword;

    @FXML
    private AnchorPane paneLogin;

    private String username;
    private String password;
    private boolean autoLogin;

    private Stage fatherStage;
    private Scene waitScene, loginScene;

    LoginResponse resp = null;

    @FXML
    public void initialize() {
        username = Main.prop.getProperty("username");
        password = Main.prop.getProperty("password");
        autoLogin = Main.prop.getProperty("autoLogin", "false").equals("true");

        if (!username.isEmpty()) {
            textUsername.setText(username);
        }
        if (!password.isEmpty()) {
            textPassword.setText(password);
            checkRememberPassword.setSelected(true);
        }

        if (autoLogin) {
            checkAutoLogin.setSelected(true);
            actionLogin(null);
        }
        URL url_load = getClass().getClassLoader().getResource("icons/load.gif");
        Image load = new Image(url_load.toString());
        ImageView view = new ImageView(load);
        view.setFitHeight(50);
        view.setFitWidth(50);
        Label label = new Label("登录中……请稍候……");
        label.setFont(Font.font(20));
        label.setGraphic(view);
        AnchorPane pane = new AnchorPane();
        pane.getChildren().add(label);
        waitScene = new Scene(pane);
        Main.addStylesheet(waitScene);
    }

    @FXML
    public void actionLogin(ActionEvent actionEvent) {
        LoginTask loginTask = new LoginTask();
        loginTask.setDaemon(true);
        loginTask.execute();
    }

    class LoginTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        public void onPreExecute() {
            loginScene = buttonLogin.getScene();
            fatherStage = (Stage) loginScene.getWindow();
            fatherStage.setScene(waitScene);
        }

        @Override
        public Boolean doInBackground(Void... params) {
            autoLogin = checkAutoLogin.isSelected();
            username = textUsername.getText();
            password = textPassword.getText();
            System.out.println("登录中……");
            LoginRequest req = new LoginRequest(username, password);
            String reason = null;
            try {
                resp = LoginResponse.parse(Main.api.postRequest(req));
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("错误！");
                alert.setHeaderText(null);
                alert.setContentText(e.toString());
                alert.showAndWait();
                alert.close();
            }
            return resp.isOk();
        }

        @Override
        public void onPostExecute(Boolean success) {
            if (success) {
                fatherStage.close();
                try {
                    Main.avatarUrl = new URL(resp.getAvatarUrl());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Main.nickName = resp.getNickName();
                Main.signature = resp.getSignature();
                Main.prop.setProperty("username", username);
                Main.prop.setProperty("autoLogin", String.valueOf(autoLogin));
                if (checkRememberPassword.isSelected()) {
                    Main.prop.setProperty("password", password);
                    Main.prop.setProperty("rememberPassword", String.valueOf(true));
                } else {
                    Main.prop.setProperty("password", "");
                    Main.prop.setProperty("rememberPassword", String.valueOf(false));
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("错误！");
                alert.setHeaderText(null);
                alert.setContentText(resp.errorReason());
                alert.initOwner(fatherStage);
                alert.showAndWait();
                fatherStage.setScene(loginScene);
                Main.prop.setProperty("autoLogin", String.valueOf(false));
            }
            try {
                FileOutputStream fout = new FileOutputStream(Main.propFile);
                Main.prop.store(fout, "");
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void progressCallback(Void... params) {

        }
    }
}