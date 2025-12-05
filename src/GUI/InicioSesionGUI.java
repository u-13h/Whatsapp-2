/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Funciones.IniciarSesion;
import Funciones.RecuperarContra;
import POJO.Usuario;
import cliente.ClienteMain;
import java.net.Socket;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import registro_cocochat.ClienteConexion;
import registro_cocochat.registroGUI;

/**
 *
 * @author aliso
 */
public class InicioSesionGUI extends JFrame{
    registroGUI r = new registroGUI();
    ClienteConexion cc = r.getconexion();
   JTextField usuario, //nombre de usuario
           contrasena, //contrasena
           resSeguridad; //respuesta a la pregunta de seguridad para recuperar la contraseña
   IniciarSesion sesion; 
   RecuperarContra recuperar;
    Usuario usr;
    String lapregunta = "";
    private JLabel texto5;
    
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
        getContentPane().removeAll();
        getContentPane().revalidate();
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
        registrarB.addActionListener((ae) -> registro());
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
        
        JLabel texto1, texto2, texto3, texto4, usuarioT, contrasenaT; 
        JButton recuperarB, regresar, buscarusu;
        
        usuario = new JTextField();
        resSeguridad = new JTextField();
        buscarusu = new JButton("Buscar usuario");
        recuperarB = new JButton("Recuperar contraseña");
        regresar = new JButton("Regresar");
        texto1 = new JLabel("Escribe tu nombre de usuario");
        texto2 = new JLabel("Y responde la pregunta de seguridad");
        texto3 = new JLabel("Para recuperar tu contraseña");
        texto4 = new JLabel("Usuario: ");
        texto5 = new JLabel("");
       
        recuperarB.addActionListener((ae) -> recuperarContra());
        regresar.addActionListener((ae) -> iniciarGUI());
        buscarusu.addActionListener((ae) -> buscarusu());
        
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
                .addComponent(buscarusu)
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
                .addComponent(buscarusu)
                .addComponent(recuperarB)
                .addComponent(regresar)
        );
        gp.setAutoCreateContainerGaps(true);
        gp.setAutoCreateGaps(true);
        setLayout(gp);
        pack();
    }
    void buscarusu(){
        recuperar = new RecuperarContra();
        lapregunta = recuperar.bsucarusuario(cc.getsocket(), usuario.getText());
        texto5.setText(lapregunta);
    }
    void recuperarContra(){
        String res = "";
        recuperar = new RecuperarContra();
        if(usuario.getText().equals("") || resSeguridad.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Favor de completar ambos campos");
        }else{
            recuperar.usr.setNombre(usuario.getText());
            recuperar.usr.setResSeguridad(resSeguridad.getText());
                res = recuperar.enviar(cc.getsocket());
                JOptionPane.showMessageDialog(null, res);
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
                res = sesion.enviar(cc.getsocket());
                JOptionPane.showMessageDialog(null, res);
                if(res.equals("Inicio de sesión exitoso")){
                    ClienteMain cm = new ClienteMain(cc.getsocket(), usuario.getText());
                    cm.setVisible(true);
                    this.dispose();
                }
        }
    }
    
    void registro(){
    registroGUI r = new registroGUI();
    r.setVisible(true);  
    this.dispose();   
    }
    
}
