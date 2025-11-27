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
        String receptor = "";
        String texto = "";
         try {
            byte[] arr = new byte[50];
            while(true){
            int len = socket.getInputStream().read(arr);
            if (len <= 0) continue;
            String msg = new String(arr, 0, len, "UTF-8");
            System.out.println(msg);
            
            
            if(msg.startsWith("USER:")){
                String persona = msg.substring(5);
                Cocochat.usuarios.put(persona,socket);
            }
            
            else if(msg.startsWith("MSG:")){
                String[] partes = msg.split("\\|", 2);
                receptor = partes[0].substring(4);
                texto = partes[1];
                
                for(Map.Entry<String,Socket> m : Cocochat.usuarios.entrySet()){
                if(m.getKey().equals(receptor)){
                    cliente = m.getValue();
                }
                }
                
                try {
                    cliente.getOutputStream().write(texto.getBytes("UTF-8"));
                } catch (IOException e) {
                    cliente.close();
                }
                
            }
            
            }
            
            } catch (IOException ex) {
                //System.out.println("Error");
        }
    }
    
   
}

