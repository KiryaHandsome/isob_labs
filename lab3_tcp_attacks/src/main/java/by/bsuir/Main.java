package by.bsuir;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
//        SynFlood.start();
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 8000));
        System.out.println("Connected to server");
    }

}