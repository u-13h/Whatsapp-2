/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.*;

public class PanelChat extends JPanel {

    private JTextArea areaMensajes;
    private JTextField campoMensaje;
    private JButton botonEnviar;
    private JScrollPane scrollPane;

    public PanelChat() {
        setLayout(new BorderLayout());

        areaMensajes = new JTextArea();
        areaMensajes.setEditable(false);
        areaMensajes.setLineWrap(true);
        areaMensajes.setWrapStyleWord(true);
        areaMensajes.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        scrollPane = new JScrollPane(areaMensajes);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        campoMensaje = new JTextField();
        campoMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelInferior.add(campoMensaje, BorderLayout.CENTER);

        botonEnviar = new JButton("Enviar");
        panelInferior.add(botonEnviar, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);
    }

    public void agregarMensaje(String usuario, String mensaje) {
        areaMensajes.append(usuario + ": " + mensaje + "\n");
        areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());
    }

    public void setListenerBotonEnviar(ActionListener listener) {
        botonEnviar.addActionListener(listener);
    }

    public void setListenerCampoMensaje(ActionListener listener) {
        campoMensaje.addActionListener(listener);
    }

    public String getMensaje() {
        return campoMensaje.getText().trim();
    }

    public void limpiarCampo() {
        campoMensaje.setText("");
    }
}
