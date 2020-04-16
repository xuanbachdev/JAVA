package factories;

import elements.Element;
import network.host.*;
import network.Packet;
import states.packet.StateP1;

public class FactoryPacket extends Factory {
	public void updateState(Packet p, Element e)
	{
		/*if(p.state == null && (e instanceof SourceQueue))
		{
			p.state = new StateP1();
		}
		else {
			p.state.getNextState(p);
		}*/
		
	}
}
