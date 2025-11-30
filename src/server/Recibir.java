/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import cocochat.Cocochat;
import dbwhatsapp2.GruposController;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import modelos.MensajeGrupo;

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
        String usuarioActual = ""; //Guardar usuario en la conexion actual
        
         try {
            byte[] arr = new byte[50];
            while(true){
            int len = socket.getInputStream().read(arr);
            if (len <= 0) continue;
            String msg = new String(arr, 0, len, "UTF-8");
            System.out.println(msg);
            
            
            if(msg.startsWith("USER:")){//
                usuarioActual = msg.substring(5); //para Grupos
                Clientes nuevo = new Clientes(cliente,1);
                Cocochat.usuarios.put(usuarioActual,nuevo);
            }
            
            else if(msg.startsWith("MSG:")){
                EnviarMensaje enviar = new EnviarMensaje(msg,cliente);
                enviar.enviar();
                
            }
            //para Grupos
            else if(msg.startsWith("GROUP_MSG:")){
                EnviarMensajeGrupo enviarGrupo = new EnviarMensajeGrupo(msg, usuarioActual);
                enviarGrupo.enviar();
            }
            //para Grupos
            else if(msg.startsWith("GET_GROUP_HISTORY:")){
                int grupoId = Integer.parseInt(msg.substring(18));
                enviarHistorialGrupo(grupoId, cliente);
            }
            }
            
            } catch (IOException ex) {
                //System.out.println("Error");
        }
    }
    private void enviarHistorialGrupo(int grupoId, Socket cliente){
        try{
            GruposController gruposController = new GruposController();
            List<MensajeGrupo> mensajes = gruposController.obtenerMensajesGrupo(grupoId);
            
            StringBuilder historial = new StringBuilder("GROUP_HISTORY:" + grupoId + "|");
            for(MensajeGrupo mensaje : mensajes){
                historial.append(mensaje.getNombreEmisor())
                        .append(": ")
                        .append(mensaje.getTexto())
                        .append("\\n");
            } cliente.getOutputStream().write(historial.toString().getBytes("UTF-8"));
        } catch(IOException ex){
            //error
        }
    }
   
}

