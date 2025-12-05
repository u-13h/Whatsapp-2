/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author Uriel
 */
public class Usuarios {
    private String usuario;
    private String contraseña;
    private String pregunta;
    private String respuesta;
    
    public Usuarios(String usuario, String contraseña, String pregunta, String respuesta){
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }
    
    public String getusuario(){
        return usuario;
    }
    
    public String getcontraseña(){
        return contraseña;
    }
    
    public void setusuario(String usuario){
        this.usuario = usuario;
    }
    
    public void setcontraseña(String contraseña){
        this.contraseña = contraseña;
    }
    
    public void setpregunta(String pregunta){
        this.pregunta = pregunta;
    }
    
    public String getpregunta(){
        return pregunta;
    }
    
    public void setrespuesta(String respuesta){
        this.respuesta = respuesta;
    }
    
    public String getrespuesta(){
        return respuesta;
    }
    
}
