module no.stonedstonar.chatapplication {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires java.logging;

  opens no.stonedstonar.chatapplication.model to javafx.fxml, javafx.graphics;
  opens no.stonedstonar.chatapplication.ui.controllers to javafx.fxml, javafx.graphics;
  opens no.stonedstonar.chatapplication.ui.windows to javafx.fxml, javafx.graphics;
  opens no.stonedstonar.chatapplication.ui to javafx.fxml, javafx.graphics;

  exports no.stonedstonar.chatapplication.backend;
  exports no.stonedstonar.chatapplication.model.message;
  exports no.stonedstonar.chatapplication.model;
  exports no.stonedstonar.chatapplication.model.conversation;
  exports no.stonedstonar.chatapplication.ui;
  exports no.stonedstonar.chatapplication.ui.windows;
  exports no.stonedstonar.chatapplication.ui.controllers;
  opens no.stonedstonar.chatapplication.model.message to javafx.fxml, javafx.graphics;
  opens no.stonedstonar.chatapplication.model.conversation to javafx.fxml, javafx.graphics;
}