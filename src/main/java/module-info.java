module lk.ijse.chat_app {
    requires javafx.fxml;
    requires com.jfoenix;
    requires javafx.controls;


    opens lk.ijse.chat_app to javafx.fxml;
    opens lk.ijse.chat_app.controller to javafx.fxml;
    exports lk.ijse.chat_app;
}