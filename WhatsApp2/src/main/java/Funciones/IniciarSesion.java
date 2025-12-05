/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.Funciones;

import java.io.IOException;
import java.net.Socket;
import main.java.POJO.Usuario;

/**
 *
 * @author aliso
 */
public class IniciarSesion {
    public Usuario usr; 
    private static Socket server;
    String request, res; 
    int intentos;
    byte [] arr;
    public IniciarSesion(){
        usr = new Usuario();
        arr = new byte[50];
        intentos = 0;
    }
    public String enviar(){
        request = "LOGIN:"+usr.getNombre()+"|"+usr.getContrasena()+"|"+usr.getResSeguridad();
        try{
            server.getOutputStream().write(request.getBytes("UTF-8"));
            server.getInputStream().read(arr);
            res = new String(arr, "UTF-8");
            if(res.equals("1")){
                return "Inicio de sesión exitoso";
            }else{
                intentos++;
                if(intentos == 3){
                    return "Registrar nuevo usuario";
                }else{
                    return "Credenciales incorrectas";
                }
                
            }
        }catch(IOException ex){
            return "Error al iniciar sesion";
        }
    }
    public String conectar(){
        try{
            server = new Socket("192.168.71.2", 1234);
            return "Conectado";
        }catch(IOException ex){
            return "No se pudo conectar al servidor";
        }
        
    }
}
