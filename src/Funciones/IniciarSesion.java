/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Funciones;

import POJO.Usuario;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author aliso
 */
public class IniciarSesion {
    public Usuario usr; 
    private static Socket server;
    String request, res; 
    public IniciarSesion(){
        usr = new Usuario();
    }
    public String enviar(Socket socket){
        server = socket;
        byte[] arr = new byte[100];
        request = "LOGIN:"+usr.getNombre()+"|"+usr.getContrasena();
        try{
            server.getOutputStream().write(request.getBytes("UTF-8"));
            int len = socket.getInputStream().read(arr);
            res = new String(arr, 0, len, "UTF-8");
            System.out.println(res);
            if(res.equals("1")){
                return "Inicio de sesión exitoso";
            }else{
                return "Credenciales incorrectas";
            }
        }catch(IOException ex){
            return "Error al iniciar sesion";
        }
    }
    
}