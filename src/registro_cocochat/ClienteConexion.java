package registro_cocochat;

import java.io.*;
import java.net.Socket;

public class ClienteConexion {

    private static final String SERVER_IP = "127.0.0.1"; // Cambia por tu servidor
    private static final int SERVER_PORT = 5000; // Cambia por tu puerto

    public static String enviarRegistro(String mensaje) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(mensaje);
            return in.readLine();

        } catch (IOException e) {
            System.out.println("Error al conectar al server: " + e.getMessage());
            return null;
        }
    }
}
