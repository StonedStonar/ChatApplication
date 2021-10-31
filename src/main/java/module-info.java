module no.stonedstonar.chatapplication {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires java.logging;

  opens no.stonedstonar.chatapplication.ui.controllers to javafx.fxml, javafx.graphics;
  opens no.stonedstonar.chatapplication.ui.windows to javafx.fxml, javafx.graphics;
  opens no.stonedstonar.chatapplication.ui to javafx.fxml, javafx.graphics;

  exports no.stonedstonar.chatapplication.backend;
  exports no.stonedstonar.chatapplication.model.message;
  exports no.stonedstonar.chatapplication.model.conversation;
  exports no.stonedstonar.chatapplication.ui;
  exports no.stonedstonar.chatapplication.ui.windows;
  exports no.stonedstonar.chatapplication.ui.controllers;
  opens no.stonedstonar.chatapplication.model.message to javafx.fxml, javafx.graphics;
  opens no.stonedstonar.chatapplication.model.conversation to javafx.fxml, javafx.graphics;
  exports no.stonedstonar.chatapplication.model.messagelog;
  opens no.stonedstonar.chatapplication.model.messagelog to javafx.fxml, javafx.graphics;
  exports no.stonedstonar.chatapplication.model.conversationregister;
  opens no.stonedstonar.chatapplication.model.conversationregister to javafx.fxml, javafx.graphics;
  exports no.stonedstonar.chatapplication.model.conversationregister.server;
  opens no.stonedstonar.chatapplication.model.conversationregister.server to javafx.fxml, javafx.graphics;
  exports no.stonedstonar.chatapplication.model.conversationregister.personal;
  opens no.stonedstonar.chatapplication.model.conversationregister.personal to javafx.fxml, javafx.graphics;
  exports no.stonedstonar.chatapplication.model.user;
  opens no.stonedstonar.chatapplication.model.user to javafx.fxml, javafx.graphics;
  exports no.stonedstonar.chatapplication.model.userregister;
  opens no.stonedstonar.chatapplication.model.userregister to javafx.fxml, javafx.graphics;
  exports no.stonedstonar.chatapplication.model.member;
  opens no.stonedstonar.chatapplication.model.member to javafx.fxml, javafx.graphics;
  exports no.stonedstonar.chatapplication.model.membersregister;
  opens no.stonedstonar.chatapplication.model.membersregister to javafx.fxml, javafx.graphics;
}