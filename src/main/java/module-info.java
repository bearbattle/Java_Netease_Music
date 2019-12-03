module neteasemusic {
    requires fastjson;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.media;
    requires jfx.asynctask;
    requires java.sql;
    requires org.testng;
    exports com.bear.neteasemusic;
    exports com.bear.neteasemusic.panel;
    exports com.bear.neteasemusic.api;
    opens com.bear.neteasemusic.panel;
}