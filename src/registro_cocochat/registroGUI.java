/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registro_cocochat;

import GUI.InicioSesionGUI;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

public class registroGUI extends JFrame {

    private JTextField campoUsuario;
    private JPasswordField campoPassword;
    private JComboBox<String> campoPregunta;
    private JTextField campoRespuesta;
    private JButton btnRegistrar;
    private String respuesta = "";
    private ClienteConexion cc = new ClienteConexion();

    public registroGUI() {
        initComponents();
        this.setLocationRelativeTo(null); // Centrar ventana
    }

    private void initComponents() {
        
        new Thread(() -> {
            cc.conectarse();
        }).start();

        // IMPORTANTE: para que todo funcione bien con setBounds()
        this.setLayout(null);

        // Panel principal con color pastel
        JPanel panel = new JPanel();
        panel.setBackground(new Color(230, 245, 255));
        panel.setLayout(null);
        panel.setBounds(0, 0, 340, 420);

        // Título
        JLabel lblTitulo = new JLabel("COCOCHAT – Registro");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setBounds(40, 20, 300, 30);
        panel.add(lblTitulo);

        // -------- Usuario --------
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsuario.setBounds(40, 80, 200, 20);
        panel.add(lblUsuario);

        campoUsuario = new JTextField();
        campoUsuario.setBounds(40, 105, 250, 30);
        panel.add(campoUsuario);

        // -------- Contraseña --------
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setBounds(40, 150, 200, 20);
        panel.add(lblPassword);

        campoPassword = new JPasswordField();
        campoPassword.setBounds(40, 175, 250, 30);
        panel.add(campoPassword);

        // -------- Pregunta de Seguridad --------
        JLabel lblPregunta = new JLabel("Pregunta de Seguridad:");
        lblPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPregunta.setBounds(40, 220, 200, 20);
        panel.add(lblPregunta);

        campoPregunta = new JComboBox<>(new String[]{
            "¿Nombre de tu primera mascota?",
            "¿Ciudad donde naciste?",
            "¿Comida favorita?",
            "¿Nombre de tu mejor amigo?"
        });
        campoPregunta.setBounds(40, 245, 250, 30);
        panel.add(campoPregunta);

        // -------- Respuesta --------
        JLabel lblRespuesta = new JLabel("Respuesta:");
        lblRespuesta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblRespuesta.setBounds(40, 285, 200, 20);
        panel.add(lblRespuesta);

        campoRespuesta = new JTextField();
        campoRespuesta.setBounds(40, 310, 250, 30);
        panel.add(campoRespuesta);

        // -------- Botón Registrar --------
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(90, 355, 150, 35);
        btnRegistrar.setBackground(new Color(0, 153, 255));
        btnRegistrar.setForeground(Color.white);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(evt -> registrarUsuario());
        panel.add(btnRegistrar);
        JButton btnIniciar = new JButton("Iniciar Sesión");
btnIniciar.setBounds(90, 400, 150, 30);
btnIniciar.setBackground(new Color(100, 100, 255));
btnIniciar.setForeground(Color.white);
btnIniciar.setFont(new Font("Segoe UI", Font.BOLD, 14));
btnIniciar.setFocusPainted(false);
panel.add(btnIniciar);

btnIniciar.addActionListener(evt -> {
    new InicioSesionGUI().setVisible(true);  
    this.dispose();                  
});

        // Config ventana
        this.add(panel);
        this.setSize(340, 460);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Registro - COCOCHAT");
    }

    // ---------------------- MÉTODO PARA REGISTRAR ------------------------
    private void registrarUsuario() {

        String usuario = campoUsuario.getText();
        String password = new String(campoPassword.getPassword());
        String pregunta = campoPregunta.getSelectedItem().toString();
        String respuestaSeg = campoRespuesta.getText();

        if (usuario.isEmpty() || password.isEmpty() || pregunta.isEmpty() || respuestaSeg.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Llena todos los campos");
            return;
        }

        // Formato EXACTO que necesitas
        String mensaje = "REGISTRO:" + usuario + "|" + password + "|" + pregunta + "|" + respuestaSeg;

        // Enviar al servidor
        
        
        respuesta = Recibir.registro(mensaje,cc.getsocket());
       

        if (respuesta == null) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar al servidor.");
            return;
        }

        switch (respuesta) {
            case "1":
                JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
                campoUsuario.setText("");
                campoPassword.setText("");
                campoRespuesta.setText("");
                campoPregunta.setSelectedIndex(0);
                break;

            case "0":
                JOptionPane.showMessageDialog(this, "El usuario ya existe.");
                break;

            default:
                JOptionPane.showMessageDialog(this, "Respuesta desconocida del servidor: " + respuesta);
                break;
        }
    }
    
    public ClienteConexion getconexion(){
        return cc;
    }
}
