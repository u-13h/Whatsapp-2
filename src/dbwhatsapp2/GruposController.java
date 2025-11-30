/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbwhatsapp2;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelos.Grupos;
import modelos.MensajeGrupo;

/**
 *
 * @author house
 */
public class GruposController extends Conexion{
    public GruposController() {super();}
    public int crearGrupo(Grupos grupos, int idUsuarioCreador) {
        int idGrupo = -1;
        try {
            // Insertar grupo
            PreparedStatement sql = get().prepareStatement(
                "INSERT INTO grupos(nombre) VALUES(?)", 
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            sql.setString(1, grupos.getNombre());
            sql.executeUpdate();
            
            // Obtener ID del grupo creado
            ResultSet rs = sql.getGeneratedKeys();
            if (rs.next()) {
                idGrupo = rs.getInt(1);
                
                // Agregar creador como admin
                agregarMiembroAGrupo(idGrupo, idUsuarioCreador, "admin", "aceptado");
            }
            
            rs.close();
            sql.close();
            
        } catch (SQLException ex) {
            System.out.println("Error al crear grupo: " + ex.getMessage());
        }
        return idGrupo;
    }
    
    public void agregarMiembroAGrupo(int idGrupo, int idUsuario, String rol, String estado) {
        try {
            PreparedStatement sql = get().prepareStatement(
                "INSERT INTO grupo_miembros(id_grupo, id_usuario, rol, estado) VALUES(?,?,?,?)"
            );
            sql.setInt(1, idGrupo);
            sql.setInt(2, idUsuario);
            sql.setString(3, rol);
            sql.setString(4, estado);
            sql.executeUpdate();
            sql.close();
            
        } catch (SQLException ex) {
            System.out.println("Error al agregar miembro: " + ex.getMessage());
        }
    }
    
    public List<Grupos> obtenerGruposDeUsuario(int idUsuario) {
        List<Grupos> grupos = new ArrayList<>();
        try {
            PreparedStatement sql = get().prepareStatement(
                "SELECT g.* FROM grupos g " +
                "JOIN grupo_miembros gm ON g.id_grupo = gm.id_grupo " +
                "WHERE gm.id_usuario = ? AND gm.estado = 'aceptado'"
            );
            sql.setInt(1, idUsuario);
            ResultSet rs = sql.executeQuery();
            
            while (rs.next()) {
                Grupos grupo = new Grupos(
                    rs.getInt("id_grupo"),
                    rs.getString("nombre")
                );
                grupos.add(grupo);
            }
            
            rs.close();
            sql.close();
            
        } catch (SQLException ex) {
            System.out.println("Error al obtener grupos: " + ex.getMessage());
        }
        return grupos;
    }
    
    public void enviarMensajeGrupo(MensajeGrupo mensaje) {
        try {
            PreparedStatement sql = get().prepareStatement(
                "INSERT INTO messages_grupo(id_grupo, emisor, texto) VALUES(?,?,?)"
            );
            sql.setInt(1, mensaje.getIdGrupo());
            sql.setInt(2, mensaje.getEmisor());
            sql.setString(3, mensaje.getTexto());
            sql.executeUpdate();
            sql.close();
            
        } catch (SQLException ex) {
            System.out.println("Error al enviar mensaje grupal: " + ex.getMessage());
        }
    }
    
    public List<MensajeGrupo> obtenerMensajesGrupo(int idGrupo) {
        List<MensajeGrupo> mensajes = new ArrayList<>();
        try {
            PreparedStatement sql = get().prepareStatement(
                "SELECT mg.*, u.usuario as nombre_emisor " +
                "FROM messages_grupo mg " +
                "JOIN usuarios u ON mg.emisor = u.id_usuario " +
                "WHERE mg.id_grupo = ? " +
                "ORDER BY mg.id_mensaje ASC"
            );
            sql.setInt(1, idGrupo);
            ResultSet rs = sql.executeQuery();
            
            while (rs.next()) {
                MensajeGrupo mensaje = new MensajeGrupo(
                    rs.getInt("id_mensaje"),
                    rs.getInt("id_grupo"),
                    rs.getInt("emisor"),
                    rs.getString("texto")
                );
                mensaje.setNombreEmisor(rs.getString("nombre_emisor"));
                mensajes.add(mensaje);
            }
            
            rs.close();
            sql.close();
            
        } catch (SQLException ex) {
            System.out.println("Error al obtener mensajes grupales: " + ex.getMessage());
        }
        return mensajes;
    }
    
    public List<String> obtenerMiembrosGrupoConNombres(int idGrupo) {
        List<String> miembros = new ArrayList<>();
        try {
            PreparedStatement sql = get().prepareStatement(
                "SELECT u.usuario FROM usuarios u " +
                "JOIN grupo_miembros gm ON u.id_usuario = gm.id_usuario " +
                "WHERE gm.id_grupo = ? AND gm.estado = 'aceptado'"
            );
            sql.setInt(1, idGrupo);
            ResultSet rs = sql.executeQuery();
            
            while (rs.next()) {
                miembros.add(rs.getString("usuario"));
            }
            
            rs.close();
            sql.close();
            
        } catch (SQLException ex) {
            System.out.println("Error al obtener miembros del grupo: " + ex.getMessage());
        }
        return miembros;
    }
    
    public boolean esMiembroDelGrupo(int idGrupo, int idUsuario) {
        try {
            PreparedStatement sql = get().prepareStatement(
                "SELECT 1 FROM grupo_miembros WHERE id_grupo = ? AND id_usuario = ? AND estado = 'aceptado'"
            );
            sql.setInt(1, idGrupo);
            sql.setInt(2, idUsuario);
            ResultSet rs = sql.executeQuery();
            
            boolean esMiembro = rs.next();
            
            rs.close();
            sql.close();
            
            return esMiembro;
            
        } catch (SQLException ex) {
            System.out.println("Error al verificar membresía: " + ex.getMessage());
            return false;
        }
    }
}
