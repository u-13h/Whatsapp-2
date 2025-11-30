/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import cocochat.Cocochat;
import dbwhatsapp2.GruposController;
import dbwhatsapp2.UsuariosController;
import java.io.IOException;
import java.util.List;
import modelos.MensajeGrupo;

/**
 *
 * @author house
 */
public class EnviarMensajeGrupo {
    private String msg;
    private String userEmisor;
    
    public EnviarMensajeGrupo(String msg, String userEmisor){
        this.msg = msg;
        this.userEmisor = userEmisor;
    }
    public void enviar(){
        try{
            String[] partes = msg.substring(10).split("\\|",  2);
            if(partes.length != 2) return;
            
            int grupoId = Integer.parseInt(partes[0]);
            String txtMensaje = partes[1];
            
            UsuariosController usuariosController = new UsuariosController();
            int idEmisor = usuariosController.obtenerIdUsuario(userEmisor);
            
            if(idEmisor == -1){
                System.out.println("No se encontro el usuario: " + userEmisor);
                return;
            }
            
            GruposController gruposController = new GruposController();
            if(!gruposController.esMiembroDelGrupo(grupoId, idEmisor)){
                System.out.println("El usuario no es miembro del grupo");
                return;
            }
            
            MensajeGrupo msg = new MensajeGrupo(grupoId, idEmisor, txtMensaje);
            gruposController.enviarMensajeGrupo(msg);
            
            List<String> miembros = gruposController.obtenerMiembrosGrupoConNombres(grupoId);
            
            String msgFormateado = "GROUP_MSG:" + grupoId + "|" + userEmisor + ": " + txtMensaje;
            for(String miembro : miembros){
                if(Cocochat.usuarios.containsKey(miembro)){
                    Clientes cliente = Cocochat.usuarios.get(miembro);
                    if(cliente.getEstado() == 1 && !cliente.getSocket().isClosed())
                        try{
                            cliente.getSocket().getOutputStream().write(msgFormateado.getBytes("UST-8"));
                        } catch(IOException e){
                            System.out.println("Error enviando a " + miembro);
                        }
                }
            }
        } catch(Exception e){
            System.out.println("Eror en envio grupal: " + e.getMessage());
        }
    }
    
}
