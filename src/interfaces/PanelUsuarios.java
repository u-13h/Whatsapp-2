package interfaces;


import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.DefaultListModel;
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

    public PanelUsuarios() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Usuarios del servidor");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);

        modeloUsuarios = new DefaultListModel<>();
        listaUsuarios = new JList<>(modeloUsuarios);

        JScrollPane scroll = new JScrollPane(listaUsuarios);
        add(scroll, BorderLayout.CENTER);
    }

    public void actualizarUsuarios(String[] usuarios) {
        modeloUsuarios.clear();
        for (String u : usuarios) {
            modeloUsuarios.addElement(u);
        }
    }
}


