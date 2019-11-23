package com.bear.neteasemusic.panel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.bear.neteasemusic.api.LoginRequest;
import com.bear.neteasemusic.api.LoginResponse;
import com.bear.neteasemusic.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.junit.Assert;

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
    private TextField testUsername;

    @FXML
    private CheckBox checkRememberPassword;

    @FXML
    private AnchorPane paneLogin;

    private String username;
    private String password;

    public void actionLogin(ActionEvent actionEvent){
        System.out.println("登录中……");
        username = testUsername.getText();
        password = textPassword.getText();
        LoginRequest loginReq = new LoginRequest(username, password);
        try{
            LoginRequest req = new LoginRequest(username, password);
            var resp = LoginResponse.parse(Main.api.postRequest(req));
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("错误！");
            alert.setHeaderText("我们有麻烦了：");
            alert.setContentText("你干啥了？");
            alert.showAndWait();
        }
        if (checkRememberPassword.isSelected()) {

        }
    }

    public static void main(String[] args) {

    }
}
