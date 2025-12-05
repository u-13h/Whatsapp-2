package interfaces;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Renata
 */

import javax.swing.*;
import java.awt.*;

public class PanelAmigos extends JPanel {

    private JList<String> listaAmigos;
    private DefaultListModel<String> modeloAmigos;

    public PanelAmigos() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Lista de amigos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);

        modeloAmigos = new DefaultListModel<>();
        listaAmigos = new JList<>(modeloAmigos);

        JScrollPane scroll = new JScrollPane(listaAmigos);
        add(scroll, BorderLayout.CENTER);
    }

    public void actualizarAmigos(String[] amigos) {
        modeloAmigos.clear();
        for (String a : amigos) {
            modeloAmigos.addElement(a);
        }
    }
}

