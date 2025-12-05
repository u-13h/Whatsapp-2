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
public class RecuperarContra {
    public Usuario usr; 
    private static Socket server;
    String request, res; 
    public RecuperarContra(){
        usr = new Usuario();
    }
    public String enviar(Socket socket){
        server = socket;
        byte[] arr = new byte[100];
        request = "RECUPERAR:"+usr.getNombre()+"|"+usr.getResSeguridad();
        try{
            server.getOutputStream().write(request.getBytes("UTF-8"));
            int len = socket.getInputStream().read(arr);
            res = new String(arr, 0, len, "UTF-8");
            System.out.println(res);
            if(!res.equals("incorrecto")){
                usr.setContrasena(res);
                return "Tu contraseña es: " + res;
            }else {
                return "Datos incorrectos";
            }
        }catch(IOException ex){
            return "Error al iniciar sesion";
        }
    }
    
    public String bsucarusuario(Socket socket,String user){
        server = socket;
        byte[] arr = new byte[100];
        request = "USUARIO:"+user;
        try{
            server.getOutputStream().write(request.getBytes("UTF-8"));
            int len = socket.getInputStream().read(arr);
            res = new String(arr, 0, len, "UTF-8");
            System.out.println(res);
            if(res.equals("0")){
                return "usuario no encontrado";
            }else{
                return res;
            }
        }catch(IOException ex){
            return "Error al iniciar sesion";
        }
    }
    
    
    
    
}
