import java.net.*;
import java.io.*;
import java.util.*;


public class HiloPrincipal implements Runnable {
    private Socket socket;
    private Vector<Socket> vSocket;
    private Vector<String> vectorUser;
    private String arreglo ="";
    public HiloPrincipal(Socket socket, Vector<Socket>vSocket, Vector<String>vectorUser){
        this.socket = socket;
        this.vSocket = vSocket;
        this.vectorUser = vectorUser;
    }
    public void run(){
        try {
            String res="";   
            String res2="";
            while(true){
               
                DataInputStream netint = new DataInputStream(socket.getInputStream());
                String msg = netint.readUTF();
                StringTokenizer status = new StringTokenizer(msg,"-");
                
                if(status.countTokens()==1){
                    System.out.println("Inicio sesion");
                    
                    res +=status.nextToken();
                    vectorUser.addElement(res);


                }if(status.countTokens()==0){
                    System.out.println("Traer a los usuarios");
                    enviaUsuario();
                }
                if(status.countTokens()==2){
                    System.out.println("Cerrar sesion");
                    String user = status.nextToken();
                    
                    if(vectorUser.size()==1){//si solo hay un solo usuario logedo
                        System.out.println("El usuario: "+vectorUser.get(0)+" Se fue!" );
                        vectorUser.remove(0);
                    }
                    else{
                        for(int i=0;i<vectorUser.size();i++){//si hay mas usuarios logiados
                            if(vectorUser.get(i).equals(user)){
                                System.out.println("El usuario: "+vectorUser.get(i)+" Se fue!" );
                                vectorUser.remove(i);
                            }
                        }
                    }
                    vSocket.remove(socket);
                    enviaUsuario();
                    socket.close();
                }
                if(status.countTokens()>2){//Indica que esta recibiendo mensajes
                    System.out.println("Mensajes");
                    res2 =status.nextToken().trim();
                    arreglo = res2;
                    enviaMensaje();
                    
                }

                
            }
			//por cada peticion se crearan sockets distintos
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }
    private void enviaUsuario(){
    
        for(Socket socCliente: vSocket){
            try {
                System.out.println(socCliente);
                DataOutputStream netOut = new DataOutputStream(socCliente.getOutputStream());
                String convertir;
                convertir = "Usuario-"+vectorUser.toString()+"-Usuario";
                
                netOut.writeUTF(convertir);
                
            } catch (IOException ioe) {
                System.out.println("No se puede enviar los datos!");
                
            }
        }
    }
    private void enviaMensaje(){
        for(Socket socCliente: vSocket){
            try {
                DataOutputStream neOut = new DataOutputStream(socCliente.getOutputStream());
                neOut.writeUTF(arreglo);
    
            } catch (Exception e) {
                System.out.println("No se puede enviar los Mensajes!");
                e.printStackTrace();
            }
        }

    }
}
