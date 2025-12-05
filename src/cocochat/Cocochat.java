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
import java.util.Scanner;
import modelos.Usuarios;
import server.Clientes;
import server.Conexiones;
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
    public static Map<String, Clientes> usuarios = new HashMap<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Conexiones con = new Conexiones();
        Scanner scn = new Scanner(System.in);
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
        
        
        Thread hilo = new Thread(con);
        hilo.start();
        
        
        new Thread(() -> {
            while(true){
                String dato = scn.nextLine();
                if(!dato.isBlank() && !dato.isEmpty()){
                    for(Map.Entry<String,Clientes> x : usuarios.entrySet()){
                        
                        System.out.println(x.getKey());
                        System.out.println(x.getValue().getEstado());
                    }
                }
            }
        }).start();
        
        new Thread(() -> {
        while(true){
            
            StringBuilder mensaje = new StringBuilder("USERS:");
            for (Map.Entry<String, Clientes> entry : usuarios.entrySet()) {
                String nombre = entry.getKey();
                int estado = entry.getValue().getEstado();
                mensaje.append(nombre).append(",").append(estado).append(";");
            }
            
            String listaFinal = mensaje.toString();
            
            for (Map.Entry<String, Clientes> entry : usuarios.entrySet()) {
                try {
                    if(!entry.getValue().getSocket().isClosed() && entry.getValue().getEstado()==1){
                        entry.getValue().getSocket().getOutputStream().write(listaFinal.getBytes("UTF-8"));
                    }
                } catch (IOException ex) {
                    System.getLogger(Cocochat.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.getLogger(Cocochat.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            
        }
            
            
        }).start();
        

    }
    
}
