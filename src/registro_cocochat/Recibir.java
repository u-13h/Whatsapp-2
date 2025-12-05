/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registro_cocochat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 *
 * @author Uriel
 */
public class Recibir {
    public static Socket socket;
    static PrintWriter out;
    
    public static String registro(String mensaje,Socket socket){
        Recibir.socket = socket;
        try{
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(mensaje);
            byte[] arr = new byte[100];
            int len = socket.getInputStream().read(arr);
            String msg = new String(arr, 0, len, "UTF-8");
            return msg;

        } catch (IOException e) {
            System.out.println("Error al conectar al server: " + e.getMessage());
            return null;
        }
    }
    
    public static String inicio(String mensaje,Socket socket){
        Recibir.socket = socket;
        try{
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(mensaje);
            byte[] arr = new byte[100];
            int len = socket.getInputStream().read(arr);
            String msg = new String(arr, 0, len, "UTF-8");
            return msg;

        } catch (IOException e) {
            System.out.println("Error al conectar al server: " + e.getMessage());
            return null;
        }
    }
    
    public static String recuperar(String mensaje,Socket socket){
        Recibir.socket = socket;
        try{
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(mensaje);
            byte[] arr = new byte[100];
            int len = socket.getInputStream().read(arr);
            String msg = new String(arr, 0, len, "UTF-8");
            return msg;

        } catch (IOException e) {
            System.out.println("Error al conectar al server: " + e.getMessage());
            return null;
        }
    }
}
