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
import java.util.List;
import modelos.Usuarios;
import server.Recibir;

/**
 *
 * @author Uriel
 */
public class Cocochat {
    private static Socket socket;
    private static ServerSocket ss;
    private static String txt;
    private static Socket socket1 = null;
    public static List<Socket> clientes = new ArrayList<>();

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
            System.out.println("Esperando");
            socket = ss.accept();
            System.out.println("Conectado");
            String num = "1";
            socket.getOutputStream().write(num.getBytes("UTF-8"));
        } catch (IOException ex) {
            System.out.println("Error");
        }
        
        Recibir recibir = new Recibir(socket);
        Thread hilo = new Thread(recibir);
        hilo.start();
                
                
        new Thread(() -> {
            while(true){
            try {
             socket1 = ss.accept();
             String num = "0";
             clientes.add(socket1);
             socket1.getOutputStream().write("0".getBytes("UTF-8"));
             
            } catch (IOException ex) {
                System.out.println("Error");
            }
            }
        }).start();
    }
    
}
