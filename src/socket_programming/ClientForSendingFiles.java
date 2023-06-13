package socket_programming;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientForSendingFiles {
    public void startClient() {
        Socket socket = null;

        try {
            socket = new Socket("127.0.0.1", 8000);
            DataInputStream inputFromServer = new DataInputStream(socket.getInputStream());
            DataOutputStream outputToServer= new DataOutputStream(socket.getOutputStream());


            while (true) {
                System.out.print("Enter a file name : ");
                Scanner scanner = new Scanner(System.in);
                String fileName = scanner.next();

                outputToServer.writeChars(fileName + '\n');
                //加上\n的目的是為了讓伺服器知道檔名的結尾在哪裡
                outputToServer.flush();

                int i = 0;
                int length = inputFromServer.readInt();
                while (i < length) {
                    System.out.print((char) inputFromServer.readByte());
                    i++;
                }
                //把整個檔案內文作為一個一個char輸出直到整個文件的長度結尾為止

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ClientForSendingFiles().startClient();
    }
}