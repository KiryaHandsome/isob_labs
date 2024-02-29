package by.bsuir.client;

import by.bsuir.common.TCPSegment;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {

    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buffer;

    public Client() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }

    public String connect(String msg) throws IOException {
        // sending
        buffer = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 8000);
        socket.send(packet);

        // receiving
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("From server: " + TCPSegment.fromString(received));
        return received;
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        TCPSegment segment = new TCPSegment();
        segment.syn = true;
        segment.sourcePort = client.socket.getLocalPort();
        segment.destinationPort = client.socket.getPort();
        client.connect(segment.getData());
    }
}
