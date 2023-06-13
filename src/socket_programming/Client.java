package socket_programming;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void statClient() {
        Socket socket = null;

        try {
            socket = new Socket("127.0.0.1", 8000);

            DataInputStream inputFromServer = new DataInputStream(socket.getInputStream());
            DataOutputStream outputToServer= new DataOutputStream(socket.getOutputStream());

            while (true) {
                Scanner scanner = new Scanner(System.in);
                double radius = scanner.nextDouble();

                outputToServer.writeDouble(radius);
                outputToServer.flush();

                double perimeter = inputFromServer.readDouble();
                System.out.println("Perimeter is " + perimeter);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        statClient();
    }
}