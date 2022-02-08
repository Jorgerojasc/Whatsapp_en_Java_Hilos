import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Vector;
import javafx.scene.control.TextArea;
public class Mensaje extends Application  {
    public void start(Stage primaryStage){
        TextArea mensajes = new TextArea();
        Scene scene = new Scene(mensajes,300,300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Principal");
        primaryStage.show(); 

    }
    public static void main(String[] args) {
        launch(args);
    }
    
}
