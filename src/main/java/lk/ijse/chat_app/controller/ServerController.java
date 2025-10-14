package lk.ijse.chat_app.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    public BorderPane mainPane;
    public ScrollPane scrollPane;
    public JFXTextArea textArea;
    public TextField messageField;
    public Button sendButton;

    ServerSocket serverSocket;
    Socket socket;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    String message="";

    public void sendMessageOnAction(ActionEvent actionEvent) {
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(messageField.getText());
            dataOutputStream.flush();
            messageField.clear();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(4000);
                textArea.appendText("Server started\n");
                socket = serverSocket.accept();
                textArea.appendText("Client connected\n");
                dataInputStream = new DataInputStream(socket.getInputStream());
                while (true) {
                    message=dataInputStream.readUTF();
                    textArea.appendText("Client :"+message+"\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }
}
