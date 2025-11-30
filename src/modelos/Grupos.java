/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author house
 */
public class Grupos {
    private int idGrupo;
    private String nombre;
    //si
    public Grupos(int idGrupo, String nombre)
    {
        this.idGrupo = idGrupo;
        this.nombre = nombre;
    }
    
    public Grupos(String nombre)
    {
        this.nombre = nombre;
    }
    
    public int getIdGrupo()
    {return idGrupo;}
    public void setIdGrupo(int idGrupo)
    {this.idGrupo = idGrupo;}
    public String getNombre(){return nombre;}
    public void setNombre(String nombre){this.nombre = nombre;}
}


