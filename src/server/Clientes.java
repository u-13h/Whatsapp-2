package server;

import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Uriel
 */
public class Clientes {

    private Socket socket;
    private int estado;
    private ArrayList<String> amigos;  

    public Clientes(Socket socket, int estado) {
        this.socket = socket;
        this.estado = estado;
        this.amigos = new ArrayList<>(); 
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

   
    public ArrayList<String> getAmigos() {
        return amigos;
    }

    public void setAmigos(ArrayList<String> amigos) {
        this.amigos = amigos;
    }

    public void addAmigo(String nombre) {
        if (!amigos.contains(nombre)) {
            amigos.add(nombre);
        }
    }

    public void removeAmigo(String nombre) {
        amigos.remove(nombre);
    }
}
