package socket_programming;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

public class MultiTreadServerForSendingFiles {
    private int client_number;

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            while (true) {
                Socket socket = serverSocket.accept();
                client_number++;
                System.out.println("Client number : " + client_number + " connected");
                new Thread(new HandleClient(socket)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class HandleClient implements Runnable {
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
                    String filename = "";
                    char i = ' ';
                    while ((int) i != 10) {
                        //(int) i != 10就表示還沒讀取到\n的地方，也就是換行的意思
                        i = inputFromClient.readChar();
                        if ((int) i != 10) {
                            //這邊如果沒這樣寫，會把換行的\n也寫進檔名，就會出問題
                            filename += i;
                        }
                    }
                    try {
                        File file = new File(filename);
                        byte[] fileContent = Files.readAllBytes(file.toPath());
                        //做一個byte的陣列來放讀取到的全部資料
                        outputToClient.writeInt(fileContent.length);
                        //告訴Client要接收的資料有多長
                        for (int j = 0; j < fileContent.length; j++) {
                            outputToClient.writeByte(fileContent[j]);
                        }
                        //把要傳送的資料做成一個一個的byte去傳送
                    } catch (NoSuchFileException e) {
                        String message = "File not found ! Please try again.\n";
                        outputToClient.writeInt(message.length());
                        outputToClient.writeBytes(message);
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        new MultiTreadServerForSendingFiles().startServer();
    }
}
