/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cliente;

import interfaces.PanelUsuarios;
import interfaces.PanelAmigos;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import registro_cocochat.ClienteConexion;
import registro_cocochat.registroGUI;

/**
 *
 * @author Renata
 */
public class ClienteMain extends JFrame{

    public static Socket socket;
    public static OutputStream out;
    public static PanelUsuarios panelUsuarios;
    public static PanelAmigos panelAmigos;
    String user;
   
    List<String> usuarios = new ArrayList<>();
    
    public ClienteMain(Socket socket, String usuario){
        this.socket = socket;
        user = usuario;
        configurarInterfaz();
    }
    
    private void configurarInterfaz() {
        setTitle("CocoChat - Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(null);

       
        panelUsuarios = new PanelUsuarios(socket);
        panelUsuarios.setBounds(10, 10, 180, 400);
        add(panelUsuarios);

       
        panelAmigos = new PanelAmigos();
        panelAmigos.setBounds(200, 10, 180, 400);
        add(panelAmigos);

        setVisible(true);
        mostrarusuarios();
    }
    
    private void mostrarusuarios(){
        String string = "USER:"+user;
        try {
            socket.getOutputStream().write(string.getBytes("UTF-8"));
        } catch (IOException ex) {
            System.getLogger(ClienteMain.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        new Thread(() -> {
            while(true){
            try {
                socket.getOutputStream().write("hola".getBytes("UTF-8"));
                Thread.sleep(2000);
            } catch (IOException ex) {
                System.getLogger(ClienteMain.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (InterruptedException ex) {
                System.getLogger(ClienteMain.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
           }
        }).start();
        
        
        
        new Thread(() -> {
            while(true){
            try {
                byte[] arr = new byte[100];
                int len = socket.getInputStream().read(arr);
                String msg = new String(arr, 0, len, "UTF-8");
                if(msg.startsWith("USERS:")){
                   String data = msg.substring(6); 
                   String[] usuariosArr = data.split(";");
                   
                   for (String u : usuariosArr) {
                       if (u.trim().isEmpty()) continue; 
                       String[] partes = u.split(",");
                       String nombre = partes[0];
                       int estado = Integer.parseInt(partes[1]);
                       String texto = nombre + (estado == 1 ? " (Online)" : " (Offline)");
                       
                       boolean existe = false;
                       for (int i = 0; i < usuarios.size(); i++) {
                           String nombreExistente = usuarios.get(i).split(" ")[0]; 
                           if (nombreExistente.equals(nombre)) {
                               usuarios.set(i, texto); 
                               existe = true;
                               break;
                           }
                       }
                       if (!existe && !nombre.equals(user)) {
                           usuarios.add(texto);
                       }
                   }
                   javax.swing.SwingUtilities.invokeLater(() -> {
                       panelUsuarios.actualizarUsuarios(usuarios);
                   });
                }
            } catch (IOException ex) {
                System.out.println("error");
            }
           }
        }).start();
        
        
        new Thread(() -> {
             byte[] arr = new byte[50];
        while(true){
        try {
            int len = socket.getInputStream().read(arr);
            if (len <= 0) continue;
            String msg = new String(arr, 0, len, "UTF-8");
            if(msg.startsWith("MENSAJE:")){
                String mensaje = msg.substring(8);
            }
        } catch (IOException ex) {
            
        }
        }
        }).start();
    }

}