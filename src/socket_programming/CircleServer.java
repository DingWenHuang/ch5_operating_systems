package socket_programming;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CircleServer {

    ObjectInputStream fromClient;
    DataOutputStream toClient;

    public CircleServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.println("Server started.");

            while (true) {
                Socket socket = serverSocket.accept();
                fromClient = new ObjectInputStream(socket.getInputStream());
                toClient = new DataOutputStream(socket.getOutputStream());
                //Circle object = (Circle) fromClient.readObject();
                //System.out.println(object.getRadius());
                Circle object = (Circle) fromClient.readObject();
                Double radius = object.getRadius();
                toClient.writeDouble(Math.PI * radius * radius);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fromClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new CircleServer();
    }
}