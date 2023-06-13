package socket_programming;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            Socket socket = serverSocket.accept();

            DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
            DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

            while (true) {
                double radius = inputFromClient.readDouble();
                double perimeter = 2 * Math.PI * radius;

                outputToClient.writeDouble(perimeter);
                outputToClient.flush();
                System.out.println("Perimeter is " + perimeter + ", and has already sent to client.");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        startServer();
    }
}