/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author house
 */
public class MensajeGrupo {
    private int idMensaje;
    private int idGrupo;
    private int emisor;
    private String nombreEmisor;
    private String texto;
    
    public MensajeGrupo(int idGrupo, int emisor, String texto){
        this.idGrupo = idGrupo;
        this.emisor = emisor;
        this.texto = texto;
    }
    
    public MensajeGrupo(int idMensaje, int idGrupo, int emisor, String texto){
        this.idMensaje = idMensaje;
        this.idGrupo = idGrupo;
        this.emisor = emisor;
        this.texto = texto;
    }
    
    public int getMensaje(){return idMensaje;}
    public void setIdMensaje(int idMensaje) {this.idMensaje = idMensaje;}
    public int getIdGrupo() { return idGrupo; }
    public void setIdGrupo(int idGrupo) { this.idGrupo = idGrupo; }
    public int getEmisor() { return emisor; }
    public void setEmisor(int emisor) { this.emisor = emisor; }
    public String getNombreEmisor() { return nombreEmisor; }
    public void setNombreEmisor(String nombreEmisor) { this.nombreEmisor = nombreEmisor; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
}
