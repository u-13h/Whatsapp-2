/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import cliente.ClienteMain;
import static cliente.ClienteMain.socket;
import java.awt.BorderLayout;
import java.awt.Font;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Renata
 */

public class PanelUsuarios extends JPanel {

    private JList<String> listaUsuarios;
    private DefaultListModel<String> modeloUsuarios;
    JButton chat,amigos;

    public PanelUsuarios(Socket socket) {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Usuarios del servidor");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);
        
        chat = new JButton("Chatear");
        amigos = new JButton("Enviar solicitud de amistad");
        
        chat.setVisible(false);
        amigos.setVisible(false);

        modeloUsuarios = new DefaultListModel<>();
        listaUsuarios = new JList<>(modeloUsuarios);

        JScrollPane scroll = new JScrollPane(listaUsuarios);
        add(scroll, BorderLayout.CENTER);
        
        listaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            int index = listaUsuarios.locationToIndex(evt.getPoint());
            if (index >= 0) {
                
                PanelChat pc = new PanelChat();
                pc.setVisible(true);
                String usuario = modeloUsuarios.getElementAt(index);
                usuario = usuario.split(" ")[0];
                String txt = "holaaaa";
                String texto = "MSG:"+usuario+"|"+txt;
                try {
            socket.getOutputStream().write(texto.getBytes("UTF-8"));
        } catch (IOException ex) {
            System.getLogger(ClienteMain.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
 
            }
        }
    }
});
        
        
    }

    public void actualizarUsuarios(List<String> usuarios) {
        modeloUsuarios.clear();
        for (String u : usuarios) {
            modeloUsuarios.addElement(u);
        }
    }
    
    
}