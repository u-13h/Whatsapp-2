/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import cocochat.Cocochat;
import dbwhatsapp2.GruposController;
import dbwhatsapp2.UsuariosController;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import modelos.MensajeGrupo;
import modelos.Usuarios;

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
            byte[] arr = new byte[100];
            while(true){
            int len = socket.getInputStream().read(arr);
            if (len <= 0) continue;
            String msg = new String(arr, 0, len, "UTF-8");
            System.out.println(msg);
            msg = msg.replace("\n", "").replace("\r", "");
            
            
            if(msg.startsWith("USER:")){//
                usuarioActual = msg.substring(5); //para Grupos
                Clientes nuevo = new Clientes(cliente,1);
                Cocochat.usuarios.put(usuarioActual,nuevo);
            }
            
            else if(msg.startsWith("MSG:")){
                EnviarMensaje enviar = new EnviarMensaje(msg,cliente);
                enviar.enviar();
                
            }
            else if(msg.startsWith("REGISTRO:")){
                String cadena = msg.substring(9);
                String[] res = cadena.split("\\|");
                String usuario = res[0];
                String contraseña = res[1];
                String pregunta = res[2];
                String respuesta = res[3];
                Usuarios usu = new Usuarios(usuario,contraseña,pregunta,respuesta);
                UsuariosController uc = new UsuariosController();
                List<Usuarios> listau = uc.getlistausuarios();
                boolean existe = listau.stream().anyMatch(u -> u.getusuario().equals(usuario));
                if(existe){
                    cliente.getOutputStream().write("0".getBytes("UTF-8"));
                }else{
                   uc.addusuario(usu); 
                   cliente.getOutputStream().write("1".getBytes("UTF-8"));
                }
                
            }
            else if(msg.startsWith("LOGIN:")){
                String cadena = msg.substring(6);
                String[] res = cadena.split("\\|");
                String usuario = res[0];
                String contraseña = res[1];
                Usuarios usu = new Usuarios(usuario,contraseña,"","");
                UsuariosController uc = new UsuariosController();
                String resultado = uc.login(usuario, contraseña);
                System.out.println(resultado);
                cliente.getOutputStream().write(resultado.getBytes("UTF-8"));
            }
            else if(msg.startsWith("USUARIO:")){
                String usuario = msg.substring(8);
                Usuarios usu = new Usuarios(usuario,"","","");
                UsuariosController uc = new UsuariosController();
                String resultado = uc.pregunta(usuario);
                System.out.println(resultado);
                cliente.getOutputStream().write(resultado.getBytes("UTF-8"));
            }
            else if(msg.startsWith("RECUPERAR:")){
                String cadena = msg.substring(10);
                String[] res = cadena.split("\\|");
                String usuario = res[0];
                String respuesta = res[1];
                Usuarios usu = new Usuarios(usuario,"","",respuesta);
                UsuariosController uc = new UsuariosController();
                String resultado = uc.recuperar(usuario,respuesta);
                System.out.println(resultado);
                cliente.getOutputStream().write(resultado.getBytes("UTF-8"));
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

