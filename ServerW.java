import java.net.*;
import java.util.*;


public class ServerW {
    

    private static Vector<Socket> vSocket = new Vector<Socket>();
    private static Vector<String> vectorUser = new Vector<>();
    public static void main(String[] args) {
    
        try{
            ServerSocket wSocket = new ServerSocket(3000);
            while (true){
                Socket socket = wSocket.accept();
                vSocket.add(socket);
                HiloPrincipal hPrincipal= new HiloPrincipal(socket, vSocket,vectorUser);
                Thread hilo = new Thread(hPrincipal);
                hilo.start();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
