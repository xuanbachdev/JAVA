package network.port;

import common.Queue;
import config.Constant;
import network.Node;
import network.Packet;

public class Port {
    public int id;
    protected Queue<Packet> buffer;

    public Port(int id) {
        this.id = id;
        this.buffer = new Queue<>();
    }

    public boolean addPacketToBuffer(Packet p) {
        if (!canReceive()) {
            return false;
        }
        buffer.enqueue(p);
        return true;
    }

    public Queue<Packet> getBuffer() {
        return buffer;
    }

    public Packet getTopPacket() {
        return buffer.peek();
    }

    public Packet removeTopPacket() {
        return buffer.dequeue();
    }

    public boolean canReceive() {
        return buffer.size() < Constant.QUEUE_SIZE;
    }

    public boolean isBufferEmpty() {
        return buffer.isEmpty();
    }

}
