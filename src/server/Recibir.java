/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import cocochat.Cocochat;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Uriel
 */
public class Recibir implements Runnable{
    private Socket socket;
    
    public Recibir(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        Socket cliente = socket;
         try {
            byte[] arr = new byte[50];
            while(true){
            int len = socket.getInputStream().read(arr);
            if (len <= 0) continue;
            String msg = new String(arr, 0, len, "UTF-8");
            System.out.println(msg);
            
            
            if(msg.startsWith("USER:")){//
                String persona = msg.substring(5);
                Clientes nuevo = new Clientes(cliente,1);
                Cocochat.usuarios.put(persona,nuevo);
            }
            
            else if(msg.startsWith("MSG:")){
                EnviarMensaje enviar = new EnviarMensaje(msg,cliente);
                enviar.enviar();
                
            }
            
            }
            
            } catch (IOException ex) {
                //System.out.println("Error");
        }
    }
    
   
}

