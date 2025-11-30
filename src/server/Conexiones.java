/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.net.Socket;
import java.util.Map;
import cocochat.Cocochat;
import static cocochat.Cocochat.usuarios;
import java.io.IOException;

/**
 *
 * @author Uriel
 */
public class Conexiones implements Runnable{

    @Override
    public void run() {
        while (true) {
                for (Map.Entry<String, Clientes> c : usuarios.entrySet()) {
                    Socket s = c.getValue().getSocket();
                    try {
                        if (s.getInputStream().read() <=0) {
                            c.getValue().setEstado(0); 
                        }
                        else {
                            c.getValue().setEstado(1); 
                        }
                    } catch (IOException e) {
                        c.getValue().setEstado(0);
                    }
                }
                try { Thread.sleep(5000); } catch (InterruptedException e) {}
            }
    }
    
    
}
