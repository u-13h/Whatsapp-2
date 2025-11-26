/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbwhatsapp2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Uriel
 */
public class Conexion {
    private Connection conexion;
    
    public Conexion(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/whatsapp2","root","");
        }catch(ClassNotFoundException ex){
            System.out.println("Error en conexión"+ex.getMessage());
        }catch(SQLException e){
            System.out.println("Error en conexión"+e.getMessage());
        }
    }
    
    protected Connection get(){
        return conexion;
    }
}
