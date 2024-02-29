package by.bsuir.common;

import java.io.Serializable;

public class TCPSegment implements Serializable {

    private static final String DELIMITER = "abc";
    public int sourcePort = 0;
    public int destinationPort = 0;
    public int sequenceNumber = 0;
    public int acknowledgmentNumber = 0;
    public boolean urg = false;
    public boolean ack = false;
    public boolean psh = false;
    public boolean rst = false;
    public boolean syn = false;
    public boolean fin = false;
    public int windowSize;
    public long hash = 0;
    public String data = null;

    public static TCPSegment fromString(String string) {
        TCPSegment segment = new TCPSegment();
        String[] parts = string.split(DELIMITER);

        if (parts.length != 13) {
            throw new IllegalArgumentException("Invalid string format for TCPSegment");
        }

        try {
            segment.sourcePort = Integer.parseInt(parts[0]);
            segment.destinationPort = Integer.parseInt(parts[1]);
            segment.sequenceNumber = Integer.parseInt(parts[2]);
            segment.acknowledgmentNumber = Integer.parseInt(parts[3]);
            segment.urg = Boolean.parseBoolean(parts[4]);
            segment.ack = Boolean.parseBoolean(parts[5]);
            segment.psh = Boolean.parseBoolean(parts[6]);
            segment.rst = Boolean.parseBoolean(parts[7]);
            segment.syn = Boolean.parseBoolean(parts[8]);
            segment.fin = Boolean.parseBoolean(parts[9]);
            segment.windowSize = Integer.parseInt(parts[10]);
            segment.hash = Long.parseLong(parts[11]);
            segment.data = parts[12];
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid data format in TCPSegment string");
        }

        return segment;
    }

    @Override
    public String toString() {
        return "TCPSegment{" +
                "sourcePort=" + sourcePort +
                ", destinationPort=" + destinationPort +
                ", sequenceNumber=" + sequenceNumber +
                ", acknowledgmentNumber=" + acknowledgmentNumber +
                ", URG=" + urg +
                ", ACK=" + ack +
                ", PSH=" + psh +
                ", RST=" + rst +
                ", SYN=" + syn +
                ", FIN=" + fin +
                ", windowSize=" + windowSize +
                ", hash=" + hash +
                ", data='" + data + '\'' +
                '}';
    }

    public String getData() {
        return new StringBuilder()
                .append(sourcePort).append(DELIMITER)
                .append(destinationPort).append(DELIMITER)
                .append(sequenceNumber).append(DELIMITER)
                .append(acknowledgmentNumber).append(DELIMITER)
                .append(urg).append(DELIMITER)
                .append(ack).append(DELIMITER)
                .append(psh).append(DELIMITER)
                .append(rst).append(DELIMITER)
                .append(syn).append(DELIMITER)
                .append(fin).append(DELIMITER)
                .append(windowSize).append(DELIMITER)
                .append(hash).append(DELIMITER)
                .append(data).toString();
    }
}