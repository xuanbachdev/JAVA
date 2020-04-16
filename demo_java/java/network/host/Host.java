package network.host;

import config.Constant;
import network.Link;
import network.Node;
import network.Packet;
import network.port.IntegratedPort;
import network.port.OutputPort;
import simulator.Event;

/**
 * Created by Dandoh on 6/27/17.
 */
public class Host extends Node {

    private SourceQueue sourceQueue;
    public IntegratedPort portToSwitch;

    public int receivedPacketInHost = 0;
    public double lastRx = 0;
    public double firstTx = -1;

    public static int COUNT = 0;

    public Host(int id) {
        super(id);
    }

    public void generatePacket(Integer destination) {
        this.sourceQueue = new SourceQueue(this.id, destination);
        portToSwitch.getOutPort().swFlag = true;
        sendToOutPort();
    }

    public void sendToOutPort(){//outport nay la cua HOST
        COUNT++;
        Host self = this;
        OutputPort outPort = portToSwitch.getOutPort();
        double currentTime = sim.getTime();

        if (outPort.canReceive()) {
            if (!sourceQueue.isEmpty((long) currentTime)) {
                Packet p = this.sourceQueue.dequeue((long) sim.getTime());
                p.setId(sim.numSent++);
                outPort.addPacketToBuffer(p);
                if ((outPort.getBuffer().size() == 1) && (!outPort.stFlag)) {
                    outPort.stFlag = true;//tuc la duoc phep gui
                    forwardToLink();//se gui du lieu tu outport sang LINK
                }
            }

            // Schedule for next packet
            double nextRetryTime = currentTime + Constant.RETRY_TIME;
            double nextPacketTime = sourceQueue.getNextPacketTime();
            sim.addEvent(new Event(sim, Math.max(nextRetryTime, nextPacketTime)) {
                @Override
                public void actions() {
                    self.sendToOutPort();
                }//Tao them packet moi de gui di
            });
        }
        else outPort.swFlag = false;
    }

    public void forwardToLink(){
        Host self = this;
        OutputPort outPort = portToSwitch.getOutPort();
        Link link = portToSwitch.getLink();

        if (!outPort.isBufferEmpty() && (outPort.getCreditCount() > 0) && !link.isBusy(self)) {
            Packet p = outPort.removeTopPacket();
            portToSwitch.getLink().transferPacket(portToSwitch, p);
            outPort.decreaseCreditCount();//Giam co nghia la bo buffer da mat di 01 cho trong

            if (!outPort.swFlag){
                sim.addEvent(new Event(sim, sim.getTime()) {
                    @Override
                    public void actions() {
                        self.sendToOutPort();
                    }//Gui du lieu ra output port cua HOST
                });
            }

            double nextTryTime = sim.getTime() + Constant.RETRY_TIME;
            sim.addEvent(new Event(sim, nextTryTime) {
                @Override
                public void actions() {//Neu loi thi gui tiep lai
                    self.forwardToLink();
                }
            });
        }
        else outPort.stFlag = false;
    }

    public void receivePacket(Packet p){
        Host self = this;
        double currentTime = sim.getTime();
        sim.numReceived++;
        if(this.receivedPacketInHost == 0) {
            this.firstTx = p.getStartTime();
            //System.out.println("Thoi gian goi tin dau tien den voi host " + self.id + " la: " + this.firstTx);
        }
        this.receivedPacketInHost ++;
        this.lastRx = currentTime;
        sim.receivedPacket[(int) (currentTime / Constant.EXPERIMENT_INTERVAL + 1)]++;
        p.setEndTime(currentTime);
        sim.totalPacketTime += p.timeTravel();
        sim.totalHop += p.nHop;

        portToSwitch.getInPort().removeTopPacket();
        sendCreditSignal(portToSwitch.getLink());
    }

    private void sendCreditSignal(Link linkToPreviousPort){
        IntegratedPort previousPort = linkToPreviousPort.getOtherPort(this);
        OutputPort previousOutputPort = previousPort.getOutPort();
        sim.getEventList().add(new Event(sim, sim.getTime() + Constant.CREDIT_DELAY) {
            @Override
            public void actions() {
                previousOutputPort.increaseCreditCount();
            }
        });
    }

    @Override
    public void clear() {

    }
}
