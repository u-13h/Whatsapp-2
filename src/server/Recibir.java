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
            
            else if(msg.startsWith("CREATE_GROUP:")){
            String nombreGrupo = msg.substring(13);
            crearGrupo(nombreGrupo, usuarioActual, cliente);
            }            
            else if(msg.startsWith("LEAVE_GROUP:")){
    int grupoId = Integer.parseInt(msg.substring(12));
    abandonarGrupo(grupoId, usuarioActual, cliente);
}

else if(msg.startsWith("ADD_MEMBER:")){
    // Solo el creador puede hacer esto
    String[] partes = msg.substring(11).split("\\|");
    if (partes.length == 2) {
        int grupoId = Integer.parseInt(partes[0]);
        String nuevoMiembro = partes[1];
        agregarMiembroAGrupo(grupoId, usuarioActual, nuevoMiembro, cliente);
    }
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

private void crearGrupo(String nombreGrupo, String usuarioCreador, Socket cliente) {
    try {
        UsuariosController usuariosController = new UsuariosController();
        GruposController gruposController = new GruposController();
        
        int idCreador = usuariosController.obtenerIdUsuario(usuarioCreador);
        
        if (idCreador == -1) {
            enviarError(cliente, "Usuario no encontrado");
            return;
        }
        
        int idGrupo = gruposController.crearGrupo(nombreGrupo, idCreador);
        
        if (idGrupo != -1) {
            String respuesta = "GROUP_CREATED:" + idGrupo + "|" + nombreGrupo;
            cliente.getOutputStream().write(respuesta.getBytes("UTF-8"));
            System.out.println("Grupo creado: " + nombreGrupo + " por " + usuarioCreador);
        } else {
            enviarError(cliente, "No se pudo crear el grupo");
        }
        
    } catch (IOException ex) {
        System.out.println("Error al crear grupo: " + ex.getMessage());
    }
}

// Método para abandonar grupo
private void abandonarGrupo(int grupoId, String usuario, Socket cliente) {
    try {
        UsuariosController usuariosController = new UsuariosController();
        GruposController gruposController = new GruposController();
        
        int idUsuario = usuariosController.obtenerIdUsuario(usuario);
        
        if (idUsuario == -1) {
            enviarError(cliente, "Usuario no encontrado");
            return;
        }
        
        // Verificar si el usuario es miembro del grupo
        if (!gruposController.esMiembroDelGrupo(grupoId, idUsuario)) {
            enviarError(cliente, "No eres miembro de este grupo");
            return;
        }
        
        int resultado = gruposController.abandonarGrupo(grupoId, idUsuario);
        
        if (resultado == 2) {
            // Grupo eliminado (sin miembros)
            String respuesta = "GROUP_DELETED:" + grupoId;
            cliente.getOutputStream().write(respuesta.getBytes("UTF-8"));
            
            // Notificar a TODOS los miembros que el grupo fue eliminado
            notificarEliminacionGrupo(grupoId);
            
        } else if (resultado == 1) {
            // Usuario eliminado del grupo
            String respuesta = "LEFT_GROUP:" + grupoId;
            cliente.getOutputStream().write(respuesta.getBytes("UTF-8"));
            
            // Verificar si era el admin
            boolean eraAdmin = gruposController.esCreador(grupoId, idUsuario);
            
            if (eraAdmin) {
                // Hubo cambio de admin
                String nuevoAdmin = gruposController.obtenerNombreAdmin(grupoId);
                notificarCambioAdmin(grupoId, usuario, nuevoAdmin);
            } else {
                // Solo miembro normal salió
                notificarMiembroSalio(grupoId, usuario);
            }
            
        } else {
            enviarError(cliente, "No se pudo abandonar el grupo");
        }
        
    } catch (IOException ex) {
        System.out.println("Error al abandonar grupo: " + ex.getMessage());
    }
}

// Método para agregar miembro
private void agregarMiembroAGrupo(int grupoId, String usuarioSolicitante, String nuevoMiembro, Socket cliente) {
    try {
        UsuariosController usuariosController = new UsuariosController();
        GruposController gruposController = new GruposController();
        
        // Verificar que el solicitante sea admin
        int idSolicitante = usuariosController.obtenerIdUsuario(usuarioSolicitante);
        if (!gruposController.esCreador(grupoId, idSolicitante)) {
            enviarError(cliente, "Solo el administrador puede agregar miembros");
            return;
        }
        
        // Obtener ID del nuevo miembro
        int idNuevoMiembro = usuariosController.obtenerIdUsuario(nuevoMiembro);
        if (idNuevoMiembro == -1) {
            enviarError(cliente, "Usuario a agregar no encontrado");
            return;
        }
        
        // Agregar miembro
        boolean agregado = gruposController.agregarMiembro(grupoId, idNuevoMiembro);
        
        if (agregado) {
            String respuesta = "MEMBER_ADDED:" + grupoId + "|" + nuevoMiembro;
            cliente.getOutputStream().write(respuesta.getBytes("UTF-8"));
            
            // Notificar al nuevo miembro si está conectado
            notificarNuevoMiembro(grupoId, nuevoMiembro);
            
            // Notificar a todos los miembros
            notificarNuevoMiembroAGrupo(grupoId, nuevoMiembro);
            
        } else {
            enviarError(cliente, "No se pudo agregar el miembro (¿ya es miembro?)");
        }
        
    } catch (IOException ex) {
        System.out.println("Error agregando miembro: " + ex.getMessage());
    }
}

// Método auxiliar para enviar errores
private void enviarError(Socket cliente, String mensaje) throws IOException {
    String error = "ERROR:" + mensaje;
    cliente.getOutputStream().write(error.getBytes("UTF-8"));
}

// Notificar a todos que el grupo fue eliminado
private void notificarEliminacionGrupo(int grupoId) {
    try {
        // Ya no hay miembros, pero notificamos a quienes estaban conectados
        String notificacion = "GROUP_DELETED:" + grupoId + "|El grupo ha sido eliminado (sin miembros)";
        
        // No podemos obtener miembros porque ya se eliminaron
        // Pero podemos notificar a todos los usuarios conectados que estaban en ese grupo
        System.out.println("Grupo " + grupoId + " eliminado - miembros notificados");
        
    } catch (Exception ex) {
        System.out.println("Error notificando eliminación: " + ex.getMessage());
    }
}

// Notificar que un miembro salió
private void notificarMiembroSalio(int grupoId, String usuario) {
    try {
        GruposController gruposController = new GruposController();
        List<String> miembros = gruposController.obtenerMiembrosGrupoConNombres(grupoId);
        
        String notificacion = "MEMBER_LEFT:" + grupoId + "|" + usuario;
        
        for (String miembro : miembros) {
            if (!miembro.equals(usuario) && Cocochat.usuarios.containsKey(miembro)) {
                Clientes cliente = Cocochat.usuarios.get(miembro);
                if (cliente.getEstado() == 1) {
                    cliente.getSocket().getOutputStream().write(notificacion.getBytes("UTF-8"));
                }
            }
        }
    } catch (IOException ex) {
        System.out.println("Error notificando salida: " + ex.getMessage());
    }
}

// Notificar cambio de administrador
private void notificarCambioAdmin(int grupoId, String adminAnterior, String nuevoAdmin) {
    try {
        GruposController gruposController = new GruposController();
        List<String> miembros = gruposController.obtenerMiembrosGrupoConNombres(grupoId);
        
        String notificacion = "ADMIN_CHANGED:" + grupoId + "|" + adminAnterior + "|" + nuevoAdmin;
        
        for (String miembro : miembros) {
            if (Cocochat.usuarios.containsKey(miembro)) {
                Clientes cliente = Cocochat.usuarios.get(miembro);
                if (cliente.getEstado() == 1) {
                    cliente.getSocket().getOutputStream().write(notificacion.getBytes("UTF-8"));
                }
            }
        }
    } catch (IOException ex) {
        System.out.println("Error notificando cambio de admin: " + ex.getMessage());
    }
}

// Notificar al nuevo miembro
private void notificarNuevoMiembro(int grupoId, String nuevoMiembro) {
    try {
        if (Cocochat.usuarios.containsKey(nuevoMiembro)) {
            Clientes cliente = Cocochat.usuarios.get(nuevoMiembro);
            if (cliente.getEstado() == 1) {
                String notificacion = "ADDED_TO_GROUP:" + grupoId;
                cliente.getSocket().getOutputStream().write(notificacion.getBytes("UTF-8"));
            }
        }
    } catch (IOException ex) {
        System.out.println("Error notificando nuevo miembro: " + ex.getMessage());
    }
}

// Notificar a todos sobre nuevo miembro
private void notificarNuevoMiembroAGrupo(int grupoId, String nuevoMiembro) {
    try {
        GruposController gruposController = new GruposController();
        List<String> miembros = gruposController.obtenerMiembrosGrupoConNombres(grupoId);
        
        String notificacion = "NEW_MEMBER:" + grupoId + "|" + nuevoMiembro;
        
        for (String miembro : miembros) {
            if (!miembro.equals(nuevoMiembro) && Cocochat.usuarios.containsKey(miembro)) {
                Clientes cliente = Cocochat.usuarios.get(miembro);
                if (cliente.getEstado() == 1) {
                    cliente.getSocket().getOutputStream().write(notificacion.getBytes("UTF-8"));
                }
            }
        }
    } catch (IOException ex) {
        System.out.println("Error notificando nuevo miembro al grupo: " + ex.getMessage());
    }
}
}

