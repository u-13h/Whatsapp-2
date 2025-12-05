/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Funciones.IniciarSesion;
import Funciones.RecuperarContra;
import POJO.Usuario;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author aliso
 */
public class InicioSesionGUI extends JFrame{
   JTextField usuario, //nombre de usuario
           contrasena, //contrasena
           resSeguridad; //respuesta a la pregunta de seguridad para recuperar la contraseña
   IniciarSesion sesion; 
   RecuperarContra recuperar;
    Usuario usr;
    
    public InicioSesionGUI(){
        super();
        iniciar();
        usr = new Usuario();
    }
    
    
    private void iniciar(){
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        iniciarGUI();
        
    }
    void iniciarGUI(){
        JLabel texto1, texto2, texto3, texto4, texto5, usuarioT, contrasenaT; 
        JButton enviarB, registrarB, recuperarB; 

        usuario = new JTextField();
        contrasena = new JTextField();
        texto1 = new JLabel("Hola!");
        texto2 = new JLabel("Bienvenido al cocochat");
        texto3 = new JLabel("Porfavor inicia sesión para comenzar");
        texto4 = new JLabel("No tienes un usuario?");
        texto5 = new JLabel("No te sabes tu contraseña?");
        usuarioT = new JLabel("Nombre de usuario");
        contrasenaT = new JLabel("Contraseña");
        enviarB = new JButton("Iniciar sesion");
        registrarB = new JButton("Registrarse");
        recuperarB = new JButton("Recuperar contraseña");
       
        
        enviarB.addActionListener((ae) -> iniciarSesion());
        recuperarB.addActionListener((ae) -> recuperarContraGUI());
        //registrarB.addActionListener((ae) -> registrar());
        GroupLayout gp;
        gp = new GroupLayout(getContentPane());
        gp.setHorizontalGroup(
                gp.createParallelGroup()
                        .addComponent(texto1)
                        .addComponent(texto2)
                        .addComponent(texto3)
                        .addComponent(usuarioT)
                        .addComponent(usuario)
                        .addComponent(contrasenaT)
                        .addComponent(contrasena)
                        .addComponent(enviarB)
                        .addComponent(texto4)
                        .addComponent(registrarB)
                        .addComponent(texto5)
                        .addComponent(recuperarB));
        gp.setVerticalGroup(
                gp.createSequentialGroup()
                        .addComponent(texto1)
                        .addComponent(texto2)
                        .addComponent(texto3)
                        .addComponent(usuarioT)
                        .addComponent(usuario)
                        .addComponent(contrasenaT)
                        .addComponent(contrasena)
                        .addComponent(enviarB)
                        .addComponent(texto4)
                        .addComponent(registrarB)
                        .addComponent(texto5)
                        .addComponent(recuperarB));
        gp.setAutoCreateContainerGaps(true);
        gp.setAutoCreateGaps(true);
        setLayout(gp);
        pack();
    }
    void recuperarContraGUI(){
        setTitle("Recuperar contraseña");
        getContentPane().removeAll();
        getContentPane().revalidate();
        
        JLabel texto1, texto2, texto3, texto4, texto5, usuarioT, contrasenaT; 
        JButton recuperarB, regresar;
        
        usuario = new JTextField();
        resSeguridad = new JTextField();
        recuperarB = new JButton("Recuperar contraseña");
        regresar = new JButton("Regresar");
        texto1 = new JLabel("Escribe tu nombre de usuario");
        texto2 = new JLabel("Y responde la pregunta de seguridad");
        texto3 = new JLabel("Para recuperar tu contraseña");
        texto4 = new JLabel("Usuario: ");
        texto5 = new JLabel("¿Cuál fue el nombre de tu primera mascota?");
       
        recuperarB.addActionListener((ae) -> recuperarContra());
        regresar.addActionListener((ae) -> iniciarGUI());
        
        GroupLayout gp;
        gp = new GroupLayout(getContentPane());
        gp.setHorizontalGroup(
                gp.createParallelGroup()
                        .addComponent(texto1)
                        .addComponent(texto2)
                        .addComponent(texto3)
                .addGroup(
                        gp.createSequentialGroup()
                                .addComponent(texto4)
                                .addComponent(usuario))
                .addComponent(texto5)
                .addComponent(resSeguridad)
                .addComponent(recuperarB)
                .addComponent(regresar)
        );
        gp.setVerticalGroup(
                gp.createSequentialGroup()
                        .addComponent(texto1)
                        .addComponent(texto2)
                        .addComponent(texto3)
                .addGroup(
                        gp.createParallelGroup()
                                .addComponent(texto4)
                                .addComponent(usuario))
                .addComponent(texto5)
                .addComponent(resSeguridad)
                .addComponent(recuperarB)
                .addComponent(regresar)
        );
        gp.setAutoCreateContainerGaps(true);
        gp.setAutoCreateGaps(true);
        setLayout(gp);
        pack();
    }
    void registrarGUI(){
        setTitle("Registrar un nuevo usuario");
        getContentPane().removeAll();
        getContentPane().revalidate();
    }
    void recuperarContra(){
        String res = "";
        recuperar = new RecuperarContra();
        if(usuario.getText().equals("") || resSeguridad.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Favor de completar ambos campos");
        }else{
            recuperar.usr.setNombre(usuario.getText());
            recuperar.usr.setResSeguridad(resSeguridad.getText());
            res = recuperar.conectar();
            JOptionPane.showMessageDialog(null, sesion.conectar());
            if(res.equals("Conectado")){
                res = sesion.enviar();
                JOptionPane.showMessageDialog(null, res);
            }
        }
    }
    void iniciarSesion(){
        String res = "";
        sesion = new IniciarSesion();
        if(usuario.getText().equals("") || contrasena.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Favor de completar ambos campos");
        }else{
            sesion.usr.setNombre(usuario.getText());
            sesion.usr.setContrasena(contrasena.getText());
            res = sesion.conectar();
            JOptionPane.showMessageDialog(null, sesion.conectar());
            if(res.equals("Conectado")){
                res = sesion.enviar();
                JOptionPane.showMessageDialog(null, res);
            }
        }
    }
    
}
