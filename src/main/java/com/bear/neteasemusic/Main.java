package com.bear.neteasemusic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.bear.neteasemusic.api.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;


public class Main extends Application {

    private URL url_icon = getClass().getClassLoader().getResource("icons/ncm_icon.png");

    public static NeteaseAPI api = new NeteaseAPI();

    public static Properties prop;

    private void addStylesheet(Scene scene){
        URL url0 = getClass().getClassLoader().getResource("com/sun/javafx/scene/control/skin/modena/modena.css");
        if (url0 == null) throw new AssertionError();
        URL url1 = getClass().getClassLoader().getResource("com/sun/javafx/scene/control/skin/modena/modena-no-transparency.css");
        if (url1 == null) throw new AssertionError();
        scene.getStylesheets().add(url0.toString());
        scene.getStylesheets().add(url1.toString());
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.getIcons().add(new Image(url_icon.toString()));
        primaryStage.setTitle("登录");

        Parent panelLogin = FXMLLoader.load(getClass().getResource("/fxml/LoginPanel.fxml"));
        Scene sceneLogin = new Scene(panelLogin);
        addStylesheet(sceneLogin);
        primaryStage.setScene(sceneLogin);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
        primaryStage.setTitle("网易云音乐");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));
        Scene scene = new Scene(root);
        addStylesheet(scene);
        primaryStage.setScene(scene);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
