package lk.ijse.chat_app.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class ServerController {
    public BorderPane mainPane;
    public ScrollPane scrollPane;
    public JFXTextArea textArea;
    public TextField messageField;
    public Button sendButton;

    public void sendMessageOnAction(ActionEvent actionEvent) {
    }
}
