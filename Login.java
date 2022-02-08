import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Vector;
import javafx.scene.control.Alert;

public class Login extends Application{

	private Socket socket;
	private Vector<String> vectorUser = new Vector<>();
	private Vector<String> vectorNumero = new Vector<>();
	private DataOutputStream netOut;
	private String localUser;

	public void start(Stage primaryStage){
		BorderPane login = new BorderPane();
		Label lUsuario = new Label("Usuario");
		Label lNumero = new Label("Numero");
		lUsuario.setStyle("-fx-padding:5;");
		lNumero.setStyle("-fx-padding:5;");
		TextField tNumero = new TextField();
		TextField tUsuario = new TextField();
		tUsuario.setStyle("-fx-border-width: 2px;-fx-border-color: #bfbfbf #bfbfbf #bfbfbf #bfbfbf;-fx-focus-color: none;");
		tUsuario.setOnMouseClicked(e->{
			tUsuario.setStyle("-fx-border-width: 2px;-fx-border-color: #F4F4F4 #F4F4F4 #00AF9C #F4F4F4;-fx-focus-color: none;");
			tNumero.setStyle("-fx-border-width: 1px;-fx-border-color: #bfbfbf #bfbfbf #bfbfbf #bfbfbf;-fx-focus-color: none;");
		});
		tNumero.setStyle("-fx-border-width: 2px;-fx-border-color: #F4F4F4 #F4F4F4 #00AF9C #F4F4F4;-fx-focus-color: none;");
		tNumero.setOnMouseClicked(e->{
			tUsuario.setStyle("-fx-border-width: 1px;-fx-border-color: #bfbfbf #bfbfbf #bfbfbf #bfbfbf;-fx-focus-color: none;");
			tNumero.setStyle("-fx-border-width: 2px;-fx-border-color: #F4F4F4 #F4F4F4 #00AF9C #F4F4F4;-fx-focus-color: none;");
		});
		GridPane inputsGP = new GridPane();
		tUsuario.setPromptText("Ej. Juan123");
		tNumero.setPromptText("Ej. 4420846372");
		HBox hTexto = new HBox(8);
		hTexto.setStyle("-fx-padding:10;fx-background-color:red");


		Label lLogo = new Label("WHATSAPP");
		Image logo = new Image("/Imagenes/logoW.png");
		ImageView viewLogo = new ImageView(logo);
		viewLogo.setFitHeight(50);
		viewLogo.setFitWidth(50);
		lLogo.setGraphic(viewLogo);
		lLogo.setStyle("-fx-font-weight: bold; -fx-text-fill:white;");

		Image iLogoUser = new Image("Imagenes/User.png");
		ImageView vLogoUser = new ImageView(iLogoUser);
		vLogoUser.setFitHeight(150);
		vLogoUser.setFitWidth(150);
		hTexto.getChildren().addAll(vLogoUser);

		AnchorPane anchorepane = new AnchorPane();
		anchorepane.setStyle("-fx-background-color:#1EBEA4;");
		anchorepane.setMinSize(900,200);
		anchorepane.getChildren().addAll(lLogo);

		HBox hBtn = new HBox();
		HBox hUser = new HBox(30);
		HBox hNumero = new HBox(30);
		Button btnlogin = new Button("Entrar");
		btnlogin.setStyle("-fx-cursor: hand;");
		btnlogin.setMinWidth(250);
		btnlogin.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				if(tUsuario.getText().length()>0 && tNumero.getText().length()>0){
					try{
						socket = new Socket("localhost",3000);
						netOut = new DataOutputStream(socket.getOutputStream());
						String datos = "-"+tUsuario.getText();
						localUser = tUsuario.getText();
						netOut.writeUTF(datos); 
						vectorUser.addElement(tUsuario.getText());
						vectorNumero.addElement(tNumero.getText());

						Principal ventanaP = new Principal(vectorUser, vectorNumero, socket,localUser,primaryStage);
						throw new IOException("Error");
					}catch(IOException ioe){
						ioe.printStackTrace();
						

					}
				}else{
					Alert userAlert = new Alert(AlertType.ERROR);
					userAlert.setTitle("Nombre de usuario o telefono vacios!");
					userAlert.setContentText("Ooops, parece que no rellenaste un campo!");
					userAlert.showAndWait();
				}

				
			} 
		});
		hBtn.getChildren().addAll(btnlogin);
		hUser.getChildren().addAll(lUsuario,tUsuario);
		hNumero.getChildren().addAll(lNumero,tNumero);


		login.setTop(anchorepane);

		inputsGP.setHgap(30); 
        inputsGP.setVgap(30);

		inputsGP.addRow(1,hUser);
		inputsGP.addRow(2,hNumero);
		inputsGP.addRow(3,hBtn);

		inputsGP.setStyle("-fx-padding:150");
		inputsGP.setAlignment(Pos.TOP_CENTER);
		hTexto.setAlignment(Pos.TOP_CENTER);
		StackPane stack_pane = new StackPane(hTexto,inputsGP);
		login.setCenter(stack_pane);
		Scene scene = new Scene(login,900,800);
		primaryStage.setScene(scene);
		primaryStage.setTitle("WhatsApp");
		primaryStage.show(); 

	}

	public static void main(String[] args){
		launch(args);	
	}
}
