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
    byte [] arr;
    public RecuperarContra(){
        usr = new Usuario();
        arr = new byte[50];
    }
    public String enviar(){
        request = "RECUPERAR:"+usr.getNombre()+"|"+usr.getResSeguridad();
        try{
            server.getOutputStream().write(request.getBytes("UTF-8"));
            server.getInputStream().read(arr);
            res = new String(arr, "UTF-8");
            if(!res.equals("incorrecto")){
                usr.setContrasena(res);
                return "Tu contraseña es" + res;
            }else {
                return "Datos incorrectos";
            }
        }catch(IOException ex){
            return "Error al iniciar sesion";
        }
    }
    public String conectar(){
        try{
            server = new Socket("192.200.3.1", 1234);
            return "Conectado";
            
        }catch(IOException ex){
            return "No se pudo conectar al servidor";
        }
        
    }
}
