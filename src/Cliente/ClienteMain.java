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
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Renata
 */
public class ClienteMain {

    public static Socket socket;
    public static OutputStream out;
    public static PanelUsuarios panelUsuarios;
    public static PanelAmigos panelAmigos;

    public static void main(String[] args) {

        try {
            socket = new Socket("127.0.0.1", 1234);
            out = socket.getOutputStream();

            JFrame ventana = new JFrame("CocoChat - Cliente");
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setSize(400, 500);
            ventana.setLayout(null);

            panelUsuarios = new PanelUsuarios();
            panelUsuarios.setBounds(10, 10, 180, 400);
            ventana.add(panelUsuarios);

            panelAmigos = new PanelAmigos();
            panelAmigos.setBounds(200, 10, 180, 400);
            ventana.add(panelAmigos);

            ventana.setVisible(true);

            Thread escuchar = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] buffer = new byte[200];
                        while (true) {
                            int len = socket.getInputStream().read(buffer);
                            if (len <= 0) continue;

                            String msg = new String(buffer, 0, len, "UTF-8");
                        }
                    } catch (IOException e) {
                        System.out.println("Desconectado del servidor.");
                    }
                }
            });

            escuchar.start();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error conectando al servidor.");
        }
    }

    public static void enviar(String msg) {
        try {
            out.write(msg.getBytes("UTF-8"));
        } catch (IOException e) {
            System.out.println("Error enviando mensaje.");
        }
    }
}