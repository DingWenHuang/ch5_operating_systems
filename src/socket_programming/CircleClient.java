package socket_programming;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CircleClient {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 8000);
            ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
            DataInputStream fromServer = new DataInputStream(socket.getInputStream());

            Circle c = new Circle(5);
            toServer.writeObject(c);

            System.out.println(fromServer.readDouble());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}