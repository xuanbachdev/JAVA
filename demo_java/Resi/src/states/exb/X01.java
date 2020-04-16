package states.exb;

import elements.ExitBuffer;
import events.LeavingSourceQueueEvent;
import states.State;
import events.*;

public class X01 extends State {
	//•	State X01: EXB is not full and able to transfer packet.
	@Override
	public void act(LeavingSourceQueueEvent ev)
	{
		ExitBuffer exb = (ExitBuffer)elem;
		if(exb.allPackets[0] != null)
		{
			Event leavingEXB = new LeavingEXBEvent(exb, exb.allPackets[0]);
			exb.insertEvents(leavingEXB);
		}
	}
}
