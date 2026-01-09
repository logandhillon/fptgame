module com.logandhillon.fptgame {
    requires javafx.base;
    requires javafx.media;
    requires javafx.graphics;

    requires org.apache.logging.log4j.core;
    requires com.google.protobuf;
    requires javafx.controls;

    opens com.logandhillon.fptgame to javafx.fxml;
    exports com.logandhillon.fptgame;

    exports com.logandhillon.fptgame.engine;
    exports com.logandhillon.fptgame.entity.ui;
    exports com.logandhillon.fptgame.scene;
    exports com.logandhillon.fptgame.networking.proto;
    exports com.logandhillon.fptgame.entity.core;
    exports com.logandhillon.fptgame.entity.ui.component;
    exports com.logandhillon.fptgame.entity.physics;
    exports com.logandhillon.fptgame.scene.menu;
    exports com.logandhillon.fptgame.scene.component;
}