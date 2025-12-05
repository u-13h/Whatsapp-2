/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POJO;

/**
 *
 * @author aliso
 */
public class Usuario {
    private Integer id; 
    private String nombre; 
    private String contrasena; 
    private String resSeguridad;

    public Usuario() {
        id = 0;
        nombre = "";
        contrasena = "";
        resSeguridad = "";
    }

    public String getResSeguridad() {
        return resSeguridad;
    }

    public void setResSeguridad(String resSeguridad) {
        this.resSeguridad = resSeguridad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
}
