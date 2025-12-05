/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registro_cocochat;

import java.io.*;
import java.net.Socket;

public class ClienteConexion{

    private static final String SERVER_IP = "127.0.0.1"; // Cambia por tu servidor
    private static final int SERVER_PORT = 1234; // Cambia por tu puerto
    private Socket server;

 
    public void conectarse() {
        while (true) {
            try {
                server = new Socket("127.0.0.1", 1234);
                System.out.println("conectado");
                break;
            } catch (IOException ex) {System.out.println("Error");}
        }
    
    }
    
    public Socket getsocket(){
        return this.server;
    }
}
