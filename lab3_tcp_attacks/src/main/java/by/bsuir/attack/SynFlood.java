package by.bsuir.attack;

import by.bsuir.common.TCPSegment;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SynFlood {

    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buffer;

    public SynFlood() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }
    
    public void start() {
        try {
            for(int i = 0; i < 10000; i++)
                connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TCPSegment buildSyn() {
        TCPSegment segment = new TCPSegment();
        segment.syn = true;
        segment.sourcePort = socket.getLocalPort();
        segment.destinationPort = socket.getPort();
        return segment;
    }

    public void connect() throws IOException {
        // sending
        TCPSegment segment = buildSyn();
        buffer = segment.getData().getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 8000);
        socket.send(packet);

        // receiving
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("From server: " + TCPSegment.fromString(received));
    }

    public static void main(String[] args) throws IOException {
        new SynFlood().start();
    }
}
