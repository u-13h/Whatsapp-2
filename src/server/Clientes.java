/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.net.Socket;

/**
 *
 * @author Uriel
 */
public class Clientes {
    private Socket socket;
    private int estado;
    
    public Clientes(Socket socket, int estado){
        this.socket = socket;
        this.estado = estado;
    }
    
    public Socket getSocket() { 
        return socket; 
    }
    public int getEstado() { 
        return estado; 
    }
    public void setEstado(int estado) { 
        this.estado = estado; 
    }
}
