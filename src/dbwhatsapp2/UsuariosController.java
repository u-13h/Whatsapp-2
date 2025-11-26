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
import modelos.Usuarios;

/**
 *
 * @author Uriel
 */
public class UsuariosController extends Conexion{
    public UsuariosController(){super();}
    
    public void addusuario(Usuarios u){
        try {
            PreparedStatement sql;
            sql = get().prepareStatement("INSERT INTO usuarios(usuario,contrasena) VALUES(?,?)");
            sql.setString(1, u.getusuario());
            sql.setString(2, u.getcontraseña());
            sql.executeUpdate();
            
            }
        catch (SQLException ex) {
            System.out.println("Error"+ex.getMessage());
        }
    }
    
    public List<Usuarios> getlistausuarios(){
        List<Usuarios> lista = new ArrayList<>();
        
        try {
            PreparedStatement sql;
            sql = get().prepareStatement("SELECT * FROM usuarios");
            ResultSet rs = sql.executeQuery();
        
        while (rs.next()) {
            Usuarios u = new Usuarios(rs.getString("usuario"), rs.getString("contrasena"));
            lista.add(u);
        }
        
        rs.close();
        sql.close();
            
            
            
        } catch (SQLException ex) {
            System.out.println("Error al obtener usuarios"+ex.getMessage());
        }
        
        return lista;
    }
    
    public Usuarios getusuario(int id){
        Usuarios usuario = new Usuarios("","");
        
        try {
            PreparedStatement sql;
            sql = get().prepareStatement("SELECT * FROM usuarios WHERE id_usuario = ?");
            sql.setInt(1, id);
            ResultSet rs = sql.executeQuery();
            
            if(rs.next()){
            usuario.setusuario(rs.getString("usuario"));
            usuario.setcontraseña(rs.getString("contrasena"));
            }
            
            rs.close();
            sql.close();
            
        } catch (SQLException ex) {
            System.out.println("Error al obtener usuario"+ex.getMessage());
        }
        
        System.out.println(usuario.getusuario());
        System.out.println(usuario.getcontraseña());
        
        return usuario;
    }
}
