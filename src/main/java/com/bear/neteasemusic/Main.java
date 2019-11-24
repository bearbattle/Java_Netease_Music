package com.bear.neteasemusic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.bear.neteasemusic.api.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;


public class Main extends Application {

    private URL url_icon = getClass().getClassLoader().getResource("icons/ncm_icon.png");

    public static NeteaseAPI api = new NeteaseAPI();
    public static boolean loginStatus = false;
    public static Properties prop = new Properties();
    public static File propFile = new File(".", "config.properties");

    public static String signature;
    public static String nickName;
    public static int userId;
    public static URL avatarUrl;

    public static void addStylesheet(Scene scene) {
        URL url0 = Main.class.getClassLoader().getResource("com/sun/javafx/scene/control/skin/modena/modena.css");
        if (url0 == null) throw new AssertionError();
        URL url1 = Main.class.getClassLoader().getResource("com/sun/javafx/scene/control/skin/modena/modena-no-transparency.css");
        if (url1 == null) throw new AssertionError();
        scene.getStylesheets().add(url0.toString());
        scene.getStylesheets().add(url1.toString());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage loginStage = new Stage();
        loginStage.getIcons().add(new Image(url_icon.toString()));
        Parent panelLogin = FXMLLoader.load(getClass().getResource("/fxml/LoginPanel.fxml"));
        Scene sceneLogin = new Scene(panelLogin);
        addStylesheet(sceneLogin);
        loginStage.setScene(sceneLogin);
        loginStage.setAlwaysOnTop(true);
        loginStage.setTitle("登录");
        loginStage.setResizable(false);
        loginStage.showAndWait();

        if (loginStatus) {
            primaryStage.getIcons().add(new Image(url_icon.toString()));
            primaryStage.setTitle("网易云音乐");
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));
            Scene scene = new Scene(root);
            addStylesheet(scene);
            primaryStage.setScene(scene);
            primaryStage.setMinHeight(850);
            primaryStage.setMinWidth(1000);
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        try {
            if (propFile.exists()) {
                prop.load(new FileInputStream(propFile));
            } else {
                propFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        launch(args);
    }
}
