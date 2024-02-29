package by.bsuir.server;

import by.bsuir.common.TCPSegment;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Server {

    private final List<String> waitingConnections = new ArrayList<>();
    private final List<String> activeConnections = new ArrayList<>();
    private final DatagramSocket serverSocket;

    public Server() throws IOException {
        serverSocket = new DatagramSocket(8000);
    }

    public void start() throws IOException {
        byte[] receive = new byte[2048];
        DatagramPacket packet;
        System.out.println("Server started!!!");
        while (true) {
            packet = new DatagramPacket(receive, receive.length);
            serverSocket.receive(packet);
            TCPSegment segment = toData(packet.getData());
            System.out.printf("From client with address=%s. Segment: %s\n",
                    packet.getSocketAddress(), segment);

            if (segment.syn) {
                System.out.printf("Received SYN from %s:%d", packet.getAddress(), segment.sourcePort);
                waitingConnections.add(String.valueOf(packet.getSocketAddress()));
                byte[] toSend = buildSynAck(segment);
                DatagramPacket data = new DatagramPacket(toSend, toSend.length, packet.getSocketAddress());
                serverSocket.send(data);
            }
            if (segment.ack) {
                System.out.printf("Received ACK from %s", packet.getSocketAddress());
                waitingConnections.remove(packet.getSocketAddress().toString());
                activeConnections.add(packet.getSocketAddress().toString());
            }
            if (segment.rst) {
                System.out.printf("Received RST from %s", packet.getSocketAddress());
                activeConnections.remove(packet.getSocketAddress().toString());
            }
        }
    }

    private byte[] buildSynAck(TCPSegment source) {
        TCPSegment segment = new TCPSegment();
        segment.destinationPort = source.sourcePort;
        segment.sourcePort = serverSocket.getLocalPort();
        segment.syn = true;
        segment.ack = true;
        return segment.getData().getBytes();
    }

    public static TCPSegment toData(byte[] a) {
        StringBuilder data = new StringBuilder();
        int i = 0;
        while (a[i] != 0) {
            data.append((char) a[i]);
            i++;
        }
        return TCPSegment.fromString(data.toString());
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}
