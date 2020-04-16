package network.host;

import config.Constant;
import network.Packet;
import elements.Buffer;
import elements.Element;
import java.util.*;
import events.Event;
import states.sourcequeue.*;
import states.State;

public class SourceQueue  extends Buffer{
    private int sourceId;
    private int destinationId;
    private long front;
    
    
    public ArrayList<Packet> allPackets = new ArrayList<Packet>();
       
    public SourceQueue(int sourceId)
    {
    	this.sourceId = sourceId;
    	this.destinationId = sourceId;
    	this.front = -1;
    	state = new Sq1(this);
    }

    public SourceQueue(int sourceId, int destinationId){
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.front = -1;
        state = new Sq1(this);
    }
    
    public void setDestionationID(int destionationID)
    {
    	this.destinationId = destionationID;
    }
    public int getDestionationID()
    {
    	return this.destinationId;
    }

    public Packet dequeue(long currentTime) {
        if (this.isEmpty(currentTime)) return null;

        front++;
        double timeSent = front * Constant.HOST_DELAY;
        Packet p = new Packet(-1, sourceId, destinationId, timeSent);
        allPackets.add(p);
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
