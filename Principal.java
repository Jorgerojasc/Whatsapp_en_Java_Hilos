import javafx.application.Application;
import javafx.scene.Scene;  
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;  
import javafx.scene.control.TextField;  
import javafx.scene.layout.*; 
import javafx.stage.Stage;
import javafx.scene.image.Image ;
import javafx.scene.image.ImageView;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;
import java.util.*;
import javafx.collections.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import java.util.List;

public class Principal extends Application{
	private Vector<String> vectorUser = new Vector<>();
	private Vector<String> vectorNumero = new Vector<>();
	private static Vector<Socket> vSockets = new Vector<Socket>();
	private ObservableList<String>  data ;
	private ObservableList<String>  mensajes ;

	ArrayList<String> al = new ArrayList<String>();
	ArrayList<String> tempAl = new ArrayList<String>();  
	ArrayList<String> tempAl2 = new ArrayList<String>();
	ArrayList<String> tempAl3 = new ArrayList<String>();
	ArrayList<String> dataOut = new ArrayList<String>();
	private Socket socket;
	private ComboBox cUser;
	private String localUser;
	ArrayList<Button> chats;
	private GridPane gChat;
	private int indice;
	private int column =0;
	private List<String> myList2;
	private List<String> filtro = new ArrayList<>();
	private TextArea txt;
	private String usuarioSelected="";
	ArrayList<String> tramaMensaje = new ArrayList<String>();
	ArrayList<String> guardartramaMensaje = new ArrayList<String>();
	List<List<String>> bidimensional;
	List<List<String>> bidimensional2 = new ArrayList<>();
	List<List<String>> guardaMensajes = new ArrayList<>();
	List<List<String>> guardaMensajes2 = new ArrayList<>();
	ArrayList<String> buttonRepeat = new ArrayList<String>();
	ArrayList<String> usuariosSlected = new ArrayList<String>();
	String nodoUser;
	Button btn1;
	int numIndice;
	int repetidos;
	public Principal(Vector<String>vectorUser,Vector<String>vectorNumero,Socket socket,String localUser,Stage primaryStage){
		this.vectorUser = vectorUser;
		this.vectorNumero = vectorNumero;
		this.socket = socket;
		this.localUser = localUser;
		start(primaryStage);

	}
	private void conectados(){

		Thread hilo = new Thread(new Runnable(){
			public void run() {

				try {
					DataInputStream netIn = new DataInputStream(socket.getInputStream());
					while(true){
						
						String msg = netIn.readUTF();
						StringTokenizer tk = new StringTokenizer(msg,"-");
						
						if(tk.countTokens()==3){//Aqui sabremos si entran los usuarios, para guardarlos en un array
							String str = msg.replaceAll("Usuario","");
							String str2 = str.replaceAll("-","");
							String str3 = str2.replaceAll("[\\[\\]]", "");
							myList2 = new ArrayList<String>(Arrays.asList(str3.split(",")));
							for(int x=0;x<myList2.size();x++){
								if(!myList2.get(x).trim().equals(localUser.trim())){
									filtro.add(myList2.get(x).trim());
								}
							}
							if(filtro.size()==0){
								filtro.add("Buscando...");
							}
							data = FXCollections.observableArrayList(filtro);
							cUser.setItems(data);
							myList2.clear();
							filtro.clear();
						}
						else{//si no entran usuario, quiere decir que entran mensajes

		
							String str = msg.replaceAll("-","");
							String str2 = str.replaceAll("MSG","");
							String str3 = str2.replaceAll(",,","");
							//Los datos se enivan de la siguiente manera {[Emisor,Receptor, Mensaje]}
							final String[] filas = str3.substring(1, str3.length() - 1).split("\\],\\[");
							bidimensional = new ArrayList<>(filas.length);
							for (String fila : filas) {//Convertimos la cadena en un array bidimensional donde contiene listas del formato del mensaje
														//eje. [[emisor1,receptor2,mensaje],[emisor2,receptor1,Mensaje]]
								List<String> entrada = Arrays.asList(fila.split(","));
								bidimensional.add(entrada);
								guardaMensajes.add(entrada);
							}
							for(int x=0;x<bidimensional.size();x++){//filtra los mensajes(son arreglos) que contengan el nombre del usuario de la cuenta (como emisor y recpetor)
								for(int y=0;y<bidimensional.get(x).size();y++){
									if(bidimensional.get(x).get(y).trim().equals(localUser.trim())){
										
										bidimensional2.add(bidimensional.get(x));										

									}
								}

							}
							
							for(int a=0;a<bidimensional2.size();a++){//filtra los mensajes que sean de la persona que los envio(para que se muestren en el usuario local)
								for(int b=0;b<bidimensional2.get(a).size();b++){
									if(bidimensional2.get(a).get(b).trim().equals(usuarioSelected.trim())){
										tramaMensaje.add(bidimensional.get(a).get(2));
										
									}
								}
							}
							mensajes = FXCollections.observableArrayList(tramaMensaje);
							for(int m=0;m<mensajes.size();m++){//imprimos los mensajes en el textarea
								txt.appendText(mensajes.get(m) + "\r\n");
								
							}
							tramaMensaje.clear();
							bidimensional2.clear();
							bidimensional.clear();

						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}


			}
		});
		hilo.start();

	}
	private void eligeUser(){//Obtenemos el valor del usuario seleccionado del combobox
		indice=0;
		chats = new ArrayList<>();
		repetidos=0;
		cUser.setOnAction(event->{
			String nombreUsuarioSeleccionado = cUser.getValue().toString();
			if(!usuariosSlected.isEmpty()){//Sirve para que no se repitan los botones de los usuarios que agregas con el combobox,
										//hay un usuario ya en el arreglo
				for(int x=0;x<usuariosSlected.size();x++ ){//verificamos que el usuario selecionado no este en el arreglo
					if(usuariosSlected.get(x).trim().equals(nombreUsuarioSeleccionado.trim())){//si esta, se suma uno en el contador
						repetidos++;
					}

				}
				if(repetidos==0){//si no esta el usuario mandamos el nombre que seleccionamos en el combobox
					generaBChat(cUser.getValue().toString());//ejecutamos el metodo generaBCHat
				}
				else{//si esta en el arreglo mandamos repetido
					generaBChat("repetido");
				}
			}
			else{
				generaBChat(cUser.getValue().toString());
			}	

		});
		
	}
	private void generaBChat(String nombre){//crearemos los botones dinamicos
		usuarioSelected = nombre.trim();
		if(!nombre.trim().equals("repetido")){//si cuando se ejecuta el metodo eligeUser y se selecciono un usuario en el combobox
										     // y en el parametro esta un nombre que sea diferente a "Repetido"
			btn1 = new Button(nombre);
			btn1.setStyle("-fx-cursor: hand;-fx-background-color:white;");
			btn1.setMinWidth(290);
			btn1.setMinHeight(70);
			btn1.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent event){
					String Val = ((Button)event.getSource()).getText();//Obtenemos el valor del boton
					txt.clear();
					txt.setText("");
					if(!guardaMensajes.isEmpty()){
						//Seguimos el mismo procedimiento del metodo conectados(), para imprimir los mensajes que nos lleguen y que estan guardados
						txt.clear();
						txt.setText("");
						for(int x=0;x<guardaMensajes.size();x++){
							for(int y=0;y<guardaMensajes.get(x).size();y++){
								if(guardaMensajes.get(x).get(y).trim().equals(localUser)){
									
									guardaMensajes2.add(guardaMensajes.get(x));										
								}
							}

						}
						for(int a=0;a<guardaMensajes2.size();a++){//filtra los mensajes que sean de la persona que los envio
							for(int b=0;b<guardaMensajes2.get(a).size();b++){
								if(guardaMensajes2.get(a).get(b).trim().equals(Val.trim())){
									guardartramaMensaje.add(guardaMensajes2.get(a).get(2));
									
								}
							}
						}
						for(int m=0;m<guardartramaMensaje.size();m++){
							txt.appendText(guardartramaMensaje.get(m) + "\r\n");
							
						}
					}
					guardartramaMensaje.clear();
					
					guardaMensajes2.clear();
					usuarioSelected = Val.trim();
					
				} 
			});
			if(btn1.getText().trim().equals(localUser.trim())){
				numIndice = indice;
			}
			chats.add(btn1);
			gChat.add(btn1,column,indice);
			usuariosSlected.add(nombre.trim());
			indice++;
			repetidos=0;
		}

		else{
			repetidos=0;
		}
	}

	public void start(Stage primaryStage){
		try {
			
			Label lBuscar = new Label("Buscar usuarios en Linea:");
			HBox hBuscar = new HBox();
			Image sendimg = new Image("/Imagenes/Send.png");
			ImageView viewSend = new ImageView(sendimg);
			viewSend.setFitHeight(30);
			viewSend.setFitWidth(30);
			Button enviar = new Button();
			enviar.setStyle("-fx-cursor: hand;");
			enviar.setGraphic(viewSend);
			enviar.setMinHeight(50);
			enviar.setMinWidth(45);

			TextField escribeMSG = new TextField();
			escribeMSG.setPromptText("Escribe algo...");
			escribeMSG.setMinHeight(50);
			escribeMSG.setMinWidth(550);
			AnchorPane contenedorTextfield = new AnchorPane();
			contenedorTextfield.getChildren().addAll(escribeMSG);
			contenedorTextfield.setMaxWidth(550);
			contenedorTextfield.setMaxHeight(80);

			txt = new TextArea();
			txt.setWrapText(true);
			txt.setMinHeight(650);
			txt.setMinWidth(600);
			txt.setDisable(true);
			txt.setStyle("-fx-background-color: #E5DDD5;-fx-font: 24 arial;-fx-text-fill: black;-fx-opacity: 1.0;");
			enviar.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent event){//Formato para enviar los Mensajes[Emisor,Receptor, Mensaje]
					try {
						DataOutputStream netOut = new DataOutputStream(socket.getOutputStream());
						String msgTexto = escribeMSG.getText().trim();
	
						String msg = "-["+localUser+","+usuarioSelected+","+localUser+": "+msgTexto+"]-MSG-MSG";
						netOut.writeUTF(msg);
						escribeMSG.clear();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			GridPane allchatkit = new GridPane();
			allchatkit.addRow(1, txt);
			allchatkit.addRow(2, contenedorTextfield);
			allchatkit.add(enviar, 1, 2);
			gChat = new GridPane();
			allchatkit.setHgap(-50);
			allchatkit.setVgap(1);
			gChat.setHgap(3);
			gChat.setVgap(3);
			gChat.setStyle("-fx-padding:3");
			
			ScrollPane chatP = new ScrollPane();
			chatP.setContent(gChat);
			chatP.setMinWidth(300);
			chatP.setMaxWidth(300);
			chatP.setStyle("-fx-background-color:#EDEDED;");
			
			lBuscar.setStyle("-fx-font: 18 arial;");
			cUser = new ComboBox();
			cUser.setStyle("-fx-pref-width: 600;-fx-background-color:white;-fx-border-color:#a6a6a6;-fx-border-style:solid;-fx-border-width:1.5;-fx-background-radius: 3px, 3px, 2px, 1px;");
			
			AnchorPane aBarra = new AnchorPane();
			aBarra.setStyle("-fx-background-color:#1EBEA4;");
			hBuscar.getChildren().addAll(lBuscar, cUser);
			hBuscar.setStyle("-fx-padding:30");
			aBarra.getChildren().addAll(hBuscar);
			BorderPane bPrincipal = new BorderPane();
			bPrincipal.setTop(aBarra);
			bPrincipal.setLeft(chatP);
			bPrincipal.setCenter(allchatkit);
			bPrincipal.setStyle("-fx-background-color:#EDEDED");
			conectados();
			eligeUser();
			Scene scene = new Scene(bPrincipal,900,800);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Principal");
			primaryStage.setOnCloseRequest(e->{
	
	
				try {
					DataOutputStream cerrarS =  new DataOutputStream(socket.getOutputStream());
					String env = "-"+localUser+"-cerrar";
					cerrarS.writeUTF(env);
					guardaMensajes.clear();
					txt.clear();
					gChat.getChildren().removeIf(btn1->gChat.getRowIndex(btn1)==numIndice);
					socket.close();
				} catch (Exception ioe) {
					ioe.printStackTrace();
				}
	
			});
			primaryStage.show(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	public static void main(String[] args){
		launch(args);
	}
}