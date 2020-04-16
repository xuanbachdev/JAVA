package network.host;

import config.Constant;
import network.Packet;

public class SourceQueue {
    private int sourceId;
    private int destinationId;
    private long front;

    public SourceQueue(int sourceId, int destinationId){
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.front = -1;
    }

    public Packet dequeue(long currentTime) {
        if (this.isEmpty(currentTime)) return null;

        front++;
        double timeSent = front * Constant.HOST_DELAY;
        Packet p = new Packet(-1, sourceId, destinationId, timeSent);
        return p;
    }

    public boolean isEmpty(long currentTime){
        long r = currentTime/Constant.HOST_DELAY;
        return r<=front;
    }

    public double getNextPacketTime(){
        return (double)(front+1)*Constant.HOST_DELAY;
    }

}
