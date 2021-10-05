module no.stonedstonar.chatapplication {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires java.logging;

  opens no.stonedstonar.chatapplication.model to javafx.fxml, javafx.graphics;
  opens no.stonedstonar.chatapplication.ui.controllers to javafx.fxml, javafx.graphics;
  opens no.stonedstonar.chatapplication.ui.windows to javafx.fxml, javafx.graphics;

  exports no.stonedstonar.chatapplication.model;
  exports no.stonedstonar.chatapplication.ui;
  exports no.stonedstonar.chatapplication.ui.windows;
}