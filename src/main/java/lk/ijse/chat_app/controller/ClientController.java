package lk.ijse.chat_app.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public BorderPane mainPane;
    public ScrollPane scrollPane;
    public JFXTextArea textArea;
    public TextField messageField;
    public ImageView imageView;

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
                socket = new Socket("192.168.43.130",3000);

                dataInputStream = new DataInputStream(socket.getInputStream());
                while (!message.equals("exit")){
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
                    textArea.appendText("Server :"+message+"\n");
//                    Process process = Runtime.getRuntime().exec(new String[]{"cmd.exe","/c",message});
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
