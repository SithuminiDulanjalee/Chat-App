package lk.ijse.chat_app.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    public BorderPane mainPane;
    public ScrollPane scrollPane;
    public JFXTextArea textArea;
    public TextField messageField;
    public Button sendButton;
    public ImageView imageView;

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
                serverSocket = new ServerSocket(3000);
                textArea.appendText("Server started\n");
                socket = serverSocket.accept();
                textArea.appendText("Client connected\n");
                dataInputStream = new DataInputStream(socket.getInputStream());
                while (!message.equals("exit")) {
                    message=dataInputStream.readUTF();
                    if (message.equals("IMAGE")) {
                        int length=dataInputStream.readInt();
                        byte[] imageBytes=new byte[length];
                        dataInputStream.readFully(imageBytes);
                        ByteArrayInputStream bais=
                                new ByteArrayInputStream(imageBytes);
                        Image image=new Image(bais);
                        imageView.setImage(image);
                    }
                    textArea.appendText("Client :"+message+"\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    public void sendFileOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file=fileChooser.showOpenDialog(new Stage());
        if (file!=null){
            try {
                byte[] imageBytes=
                        Files.readAllBytes(file.toPath());
                dataOutputStream=new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("IMAGE");
                dataOutputStream.writeInt(imageBytes.length);
                dataOutputStream.write(imageBytes);
                dataOutputStream.flush();
                textArea.appendText(file.getName()+"\n");
                textArea.appendText(file.getAbsolutePath()+"\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
