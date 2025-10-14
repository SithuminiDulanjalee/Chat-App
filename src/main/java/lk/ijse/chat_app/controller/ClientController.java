package lk.ijse.chat_app.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public BorderPane mainPane;
    public ScrollPane scrollPane;
    public JFXTextArea textArea;
    public TextField messageField;

    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String message="";

    public void sendMessageOnAction(ActionEvent actionEvent) {
        try {
            dataOutputStream=new DataOutputStream(socket.getOutputStream());
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
                socket = new Socket("localhost",4000);
                dataInputStream = new DataInputStream(socket.getInputStream());
                while (true){
                    message=dataInputStream.readUTF();
                    textArea.appendText("Server :"+message+"\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
