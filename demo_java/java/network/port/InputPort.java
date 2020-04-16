package network.port;

public class InputPort extends Port {
    private OutputPort nextOutPort;
    public boolean rcFlag = false;

    public InputPort(int id) {
        super(id);
    }

    public OutputPort getNextOutPort() {
        return nextOutPort;
    }

    public void setNextOutPort(OutputPort nextOutPort) {
        this.nextOutPort = nextOutPort;
    }

}
