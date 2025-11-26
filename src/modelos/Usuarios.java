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
    
    public Usuarios(String usuario, String contraseña){
        this.usuario = usuario;
        this.contraseña = contraseña;
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
    
}
