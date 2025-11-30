/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import cocochat.Cocochat;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

/**
 *
 * @author Uriel
 */
public class EnviarMensaje{
    private String msg, texto, receptor;
    private Socket cliente;
    
    public EnviarMensaje(String msg, Socket cliente){
        this.msg = msg;
        this.cliente = cliente;
    }

    public void enviar() {
        String[] partes = msg.split("\\|", 2);
                receptor = partes[0].substring(4);
                texto = partes[1];
                
                for(Map.Entry<String,Clientes> m : Cocochat.usuarios.entrySet()){
                if(m.getKey().equals(receptor)){
                    cliente = m.getValue().getSocket();
                }
                }
                
                try {
                    cliente.getOutputStream().write(texto.getBytes("UTF-8"));
                } catch (IOException e) {
            try {
                cliente.close();
            } catch (IOException ex) {
                System.getLogger(EnviarMensaje.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
                }
    }
    
}
