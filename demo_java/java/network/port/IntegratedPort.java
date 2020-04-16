package network.port;

import network.Link;
import network.Node;

public class IntegratedPort {
    protected Node node;
    private InputPort inPort;
    private OutputPort outPort;
    private Link link;

    public IntegratedPort(Node node, Link link, int id) {
        this.node = node;
        this.link = link;
        this.inPort = new InputPort(id);
        this.outPort = new OutputPort(id);
    }

    public Node getNode() {
        return node;
    }

    public Link getLink(){
        return link;
    }

    public InputPort getInPort() {
        return inPort;
    }

    public OutputPort getOutPort() {
        return outPort;
    }
}
