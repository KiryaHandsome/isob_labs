package by.bsuir.attack;

import by.bsuir.common.TCPSegment;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TCPReset {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buffer;

    public TCPReset() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }

    public void start() {
        try {
//            for(int i = 0; i < 10000; i++)
            connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TCPSegment buildRst() {
        TCPSegment segment = new TCPSegment();
        segment.rst = true;
        return segment;
    }

    public void connect() throws IOException {
        // sending
        TCPSegment segment = buildRst();
        System.out.println("Sending to server segment: " + segment);
        buffer = segment.getData().getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 8000);
        socket.send(packet);
    }

    public static void main(String[] args) throws IOException {
        new TCPReset().start();
    }
}
