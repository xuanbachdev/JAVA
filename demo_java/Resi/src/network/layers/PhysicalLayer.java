package network.layers;

import elements.*;
import network.Device;
import network.host.*;
import simulator.Simulator;

public class PhysicalLayer {
	public ExitBuffer[] EXBs;
	public EntranceBuffer[] ENBs;
	public SourceQueue sq;
	public Simulator sim;
	public Device node;
	
	public PhysicalLayer(Host host)
	{
		ENBs = null;
		EXBs = new ExitBuffer[1];
		sq = new SourceQueue(host.id);
		EXBs[0] = new ExitBuffer();
		EXBs[0].phyLayer = this;
		sq.phyLayer = this;
		this.node = host;
	}
	
	/*public void addLocationOfEvents()
	{
		sim.addLocationOfEvents(node);
	}*/
}
