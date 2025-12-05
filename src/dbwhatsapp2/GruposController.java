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

// Método para crear grupo
public int crearGrupo(String nombreGrupo, int idUsuarioCreador) {
    int idGrupo = -1;
    try {
        // 1. Insertar grupo (sin campo creador)
        PreparedStatement sqlGrupo = get().prepareStatement(
            "INSERT INTO grupos(nombre) VALUES(?)", 
            PreparedStatement.RETURN_GENERATED_KEYS
        );
        sqlGrupo.setString(1, nombreGrupo);
        sqlGrupo.executeUpdate();
        
        // Obtener ID del grupo creado
        ResultSet rs = sqlGrupo.getGeneratedKeys();
        if (rs.next()) {
            idGrupo = rs.getInt(1);
        }
        rs.close();
        sqlGrupo.close();
        
        // 2. Agregar creador como ADMIN (único admin)
        if (idGrupo != -1) {
            PreparedStatement sqlMiembro = get().prepareStatement(
                "INSERT INTO grupo_miembros(id_grupo, id_usuario, rol, estado) VALUES(?, ?, 'admin', 'aceptado')"
            );
            sqlMiembro.setInt(1, idGrupo);
            sqlMiembro.setInt(2, idUsuarioCreador);
            sqlMiembro.executeUpdate();
            sqlMiembro.close();
        }
        
        System.out.println("Grupo creado por " + idUsuarioCreador + ": " + nombreGrupo + " (ID: " + idGrupo + ")");
        
    } catch (SQLException ex) {
        System.out.println("Error al crear grupo: " + ex.getMessage());
    }
    return idGrupo;
}

// Verificar si usuario es el creador (admin)
public boolean esCreador(int idGrupo, int idUsuario) {
    try {
        PreparedStatement sql = get().prepareStatement(
            "SELECT 1 FROM grupo_miembros WHERE id_grupo = ? AND id_usuario = ? AND rol = 'admin'"
        );
        sql.setInt(1, idGrupo);
        sql.setInt(2, idUsuario);
        ResultSet rs = sql.executeQuery();
        boolean esCreador = rs.next();
        rs.close();
        sql.close();
        return esCreador;
    } catch (SQLException ex) {
        System.out.println("Error verificando creador: " + ex.getMessage());
        return false;
    }
}

// Verificar si hay otros administradores (debería ser solo 1)
public int contarAdministradores(int idGrupo) {
    int count = 0;
    try {
        PreparedStatement sql = get().prepareStatement(
            "SELECT COUNT(*) as count FROM grupo_miembros WHERE id_grupo = ? AND rol = 'admin'"
        );
        sql.setInt(1, idGrupo);
        ResultSet rs = sql.executeQuery();
        if (rs.next()) {
            count = rs.getInt("count");
        }
        rs.close();
        sql.close();
    } catch (SQLException ex) {
        System.out.println("Error contando administradores: " + ex.getMessage());
    }
    return count;
}

// Método para abandonar grupo
public int abandonarGrupo(int idGrupo, int idUsuario) {
    // 0 = no se pudo, 1 = usuario eliminado, 2 = grupo eliminado
    try {
        // 1. Verificar si el usuario es el creador (admin)
        boolean esCreador = esCreador(idGrupo, idUsuario);
        
        // 2. Eliminar al usuario del grupo
        PreparedStatement sqlEliminar = get().prepareStatement(
            "DELETE FROM grupo_miembros WHERE id_grupo = ? AND id_usuario = ?"
        );
        sqlEliminar.setInt(1, idGrupo);
        sqlEliminar.setInt(2, idUsuario);
        int filasAfectadas = sqlEliminar.executeUpdate();
        sqlEliminar.close();
        
        if (filasAfectadas == 0) return 0; // No era miembro
        
        // 3. Si era el creador, verificar si quedan miembros
        if (esCreador) {
            int miembrosRestantes = contarMiembros(idGrupo);
            if (miembrosRestantes == 0) {
                // No quedan miembros, eliminar grupo
                eliminarGrupoCompleto(idGrupo);
                return 2; // Grupo eliminado
            } else {
                // Hay miembros pero sin admin, asignar nuevo admin
                asignarNuevoAdmin(idGrupo);
                return 1; // Solo usuario eliminado
            }
        }
        
        return 1; // Usuario normal eliminado
        
    } catch (SQLException ex) {
        System.out.println("Error al abandonar grupo: " + ex.getMessage());
        return 0;
    }
}

// Contar miembros del grupo
private int contarMiembros(int idGrupo) {
    try {
        PreparedStatement sql = get().prepareStatement(
            "SELECT COUNT(*) as count FROM grupo_miembros WHERE id_grupo = ?"
        );
        sql.setInt(1, idGrupo);
        ResultSet rs = sql.executeQuery();
        rs.next();
        int count = rs.getInt("count");
        rs.close();
        sql.close();
        return count;
    } catch (SQLException ex) {
        System.out.println("Error contando miembros: " + ex.getMessage());
        return 0;
    }
}

// Asignar nuevo admin cuando el creador abandona
private void asignarNuevoAdmin(int idGrupo) {
    try {
        // Obtener primer miembro disponible
        PreparedStatement sql = get().prepareStatement(
            "SELECT id_usuario FROM grupo_miembros WHERE id_grupo = ? LIMIT 1"
        );
        sql.setInt(1, idGrupo);
        ResultSet rs = sql.executeQuery();
        
        if (rs.next()) {
            int nuevoAdmin = rs.getInt("id_usuario");
            rs.close();
            sql.close();
            
            // Actualizar a admin
            PreparedStatement sqlUpdate = get().prepareStatement(
                "UPDATE grupo_miembros SET rol = 'admin' WHERE id_grupo = ? AND id_usuario = ?"
            );
            sqlUpdate.setInt(1, idGrupo);
            sqlUpdate.setInt(2, nuevoAdmin);
            sqlUpdate.executeUpdate();
            sqlUpdate.close();
            
            System.out.println("Nuevo admin asignado para grupo " + idGrupo + ": usuario " + nuevoAdmin);
        } else {
            rs.close();
            sql.close();
        }
        
    } catch (SQLException ex) {
        System.out.println("Error asignando nuevo admin: " + ex.getMessage());
    }
}

// Eliminar grupo completo
private void eliminarGrupoCompleto(int idGrupo) {
    try {
        System.out.println("Eliminando grupo " + idGrupo + " (sin miembros)");
        
        // 1. Eliminar mensajes del grupo
        PreparedStatement sqlMensajes = get().prepareStatement(
            "DELETE FROM messages_grupo WHERE id_grupo = ?"
        );
        sqlMensajes.setInt(1, idGrupo);
        sqlMensajes.executeUpdate();
        sqlMensajes.close();
        
        // 2. Eliminar miembros del grupo (ON DELETE CASCADE ya lo hace)
        
        // 3. Eliminar el grupo
        PreparedStatement sqlGrupo = get().prepareStatement(
            "DELETE FROM grupos WHERE id_grupo = ?"
        );
        sqlGrupo.setInt(1, idGrupo);
        sqlGrupo.executeUpdate();
        sqlGrupo.close();
        
        System.out.println("Grupo " + idGrupo + " eliminado completamente");
        
    } catch (SQLException ex) {
        System.out.println("Error eliminando grupo: " + ex.getMessage());
    }
}

// Obtener creador/admin del grupo
public int obtenerCreadorGrupo(int idGrupo) {
    int creador = -1;
    try {
        PreparedStatement sql = get().prepareStatement(
            "SELECT id_usuario FROM grupo_miembros WHERE id_grupo = ? AND rol = 'admin' LIMIT 1"
        );
        sql.setInt(1, idGrupo);
        ResultSet rs = sql.executeQuery();
        
        if (rs.next()) {
            creador = rs.getInt("id_usuario");
        }
        
        rs.close();
        sql.close();
        
    } catch (SQLException ex) {
        System.out.println("Error obteniendo creador: " + ex.getMessage());
    }
    return creador;
}

// Método para agregar miembros
public boolean agregarMiembro(int idGrupo, int idUsuario) {
    try {
        // Verificar si ya es miembro
        if (esMiembroDelGrupo(idGrupo, idUsuario)) {
            return false;
        }
        
        PreparedStatement sql = get().prepareStatement(
            "INSERT INTO grupo_miembros(id_grupo, id_usuario, rol, estado) VALUES(?, ?, 'miembro', 'aceptado')"
        );
        sql.setInt(1, idGrupo);
        sql.setInt(2, idUsuario);
        int resultado = sql.executeUpdate();
        sql.close();
        
        return resultado > 0;
        
    } catch (SQLException ex) {
        System.out.println("Error agregando miembro: " + ex.getMessage());
        return false;
    }
}

// Obtener admin actual del grupo
public String obtenerNombreAdmin(int idGrupo) {
    try {
        PreparedStatement sql = get().prepareStatement(
            "SELECT u.usuario FROM usuarios u " +
            "JOIN grupo_miembros gm ON u.id_usuario = gm.id_usuario " +
            "WHERE gm.id_grupo = ? AND gm.rol = 'admin' LIMIT 1"
        );
        sql.setInt(1, idGrupo);
        ResultSet rs = sql.executeQuery();
        
        String admin = "";
        if (rs.next()) {
            admin = rs.getString("usuario");
        }
        
        rs.close();
        sql.close();
        return admin;
        
    } catch (SQLException ex) {
        System.out.println("Error obteniendo nombre admin: " + ex.getMessage());
        return "";
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
