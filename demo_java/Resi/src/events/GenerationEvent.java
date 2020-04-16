package events;

import elements.Element;
import network.host.*;
import simulator.Simulator;
import network.Packet;
import states.packet.*;
import states.sourcequeue.Sq1;
import states.sourcequeue.Sq2;

public class GenerationEvent extends Event {
	//Event dai dien cho su kien loai (A): goi tin duoc sinh ra
	public int numSent = 0;
	
	public GenerationEvent() {
		
	}
	
	public GenerationEvent(Element elem)
	{
		this.elem = elem;
	}
	
	@Override
	
	public void execute()
	{
		//if(elem instanceof SourceQueue)
		{
			elem.removeExecutedEvent(this);
			Packet p = ((SourceQueue)elem).dequeue(this.startTime);
			if(p == null) return;
			p.setId(numSent);
			this.pid = p.id;
			p.state = new StateP1((SourceQueue)elem, p);
			p.state.act(this);
			
			if(elem.state instanceof Sq1)//it means that elem is an instance of SourceQueue 
			{
				elem.state = new Sq2((SourceQueue)elem);
				elem.state.act(this);
			}
		}
	}
}
