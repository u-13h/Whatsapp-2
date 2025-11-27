/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cocochat;

import dbwhatsapp2.UsuariosController;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelos.Usuarios;
import server.Recibir;

/**
 *
 * @author Uriel
 */
public class Cocochat {

    private static ServerSocket ss;
    private static String txt;
    private static Socket socket1 = null;
    public static List<Socket> clientes = new ArrayList<>();
    public static Map<String, Socket> usuarios = new HashMap();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Usuarios usuario = new Usuarios("uri2","12345");
        //UsuariosController ucontroller = new UsuariosController();
        //ucontroller.addusuario(usuario);
        //ucontroller.getlistausuarios();
        //ucontroller.getusuario(1);
        
        try {
            ss = new ServerSocket(1234);
        } catch (IOException ex) {
            System.out.println("Error");
            return;
        }
                
                
        new Thread(() -> {
            byte[] arr = new byte[50];
            while(true){
            try {
             socket1 = ss.accept();
            Recibir recibir = new Recibir(socket1);
            Thread hilo = new Thread(recibir);
            hilo.start();
             
            } catch (IOException ex) {
                System.out.println("Error");
            }
            }
        }).start();
    }
    
}
