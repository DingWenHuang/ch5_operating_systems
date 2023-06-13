package socket_programming;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class HandleClient implements Runnable {
    private Socket socket;

    public HandleClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream inputFromClient = new DataInputStream(this.socket.getInputStream());
            DataOutputStream outputToClient = new DataOutputStream(this.socket.getOutputStream());

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
}

public class MultiThreadedServer {

    private int client_number = 0;

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);

            while (true) {
                //listen for a new connection
                Socket socket = serverSocket.accept();

                client_number++;
                System.out.println("socket_programming.Client number # " + client_number + " connecting");

                //create and start new thread
                new Thread(new HandleClient(socket)).start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MultiThreadedServer().start();
    }
}