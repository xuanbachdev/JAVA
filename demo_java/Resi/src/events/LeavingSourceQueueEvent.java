package events;

import config.Constant;
import elements.Element;
import elements.ExitBuffer;
import network.Packet;
import network.host.SourceQueue;
import states.exb.X00;
import states.exb.X01;
import states.exb.X10;
import states.exb.X11;
import states.packet.StateP2;
import states.sourcequeue.Sq2;

enum TypeB
{
	B, B1, B2, B3, B4
}

public class LeavingSourceQueueEvent extends Event {
	public TypeB type = TypeB.B;
	//Event dai dien cho su kien loai (B): goi tin roi khoi Source Queue
	
	public LeavingSourceQueueEvent(Element elem, Packet p)
	{
		this.elem = elem;
		this.pid = p.id;
	}
	
	@Override
	public void execute()
	{
		elem.removeExecutedEvent(this);
		SourceQueue sQueue = (SourceQueue)elem;
		ExitBuffer exb = sQueue.phyLayer.EXBs[0];//Kiem tra xem EXB co cho trong hay khong?
		//int index = exb.indexOfEmpty();
		//if(index < Constant.QUEUE_SIZE
		if(((exb.state instanceof X00 ) || (exb.state instanceof X01))
				&& (sQueue.state instanceof Sq2)
				)//neu EXB con cho trong
		{
			Packet p = sQueue.allPackets.remove(0);
			exb.insertPacket(p);
			p.state = new StateP2();
			p.state.act(this);
			
			int index = exb.indexOfEmpty();
			if(index == Constant.QUEUE_SIZE)
			{
				if(exb.state instanceof X00) { exb.state = new X10(); }
				if(exb.state instanceof X01) { exb.state = new X11(); }
			}
			exb.state.elem = exb;
			exb.state.act(this);
			//To be continued...
			//Event e = new LeavingSourceQueueEvent(sQueue);
			//e.startTime = sQueue.phyLayer.sim.time();
			//e.endTime = e.startTime;
			//e.pid = this.p.id;
			//sQueue.insertEvents(e);//chen them su kien moi vao
		}
	}
}
