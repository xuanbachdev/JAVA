package events;

import config.Constant;
import elements.Element;
import elements.ExitBuffer;
import network.Packet;
import network.host.SourceQueue;

public class LeavingEXBEvent extends Event {
	//Event dai dien cho su kien loai (C): goi tin roi khoi EXB 
	public LeavingEXBEvent(Element elem, Packet p)
	{
		this.elem = elem;
		this.pid = p.id;
	}
	
	@Override
	public void execute()
	{
		elem.removeExecutedEvent(this);//go bo su kien nay ra khoi danh sach cac su kien
		
		ExitBuffer exb = (ExitBuffer)elem;
		//vong lap for thuc hien viec dich chuyen cac goi tin len truoc
		for(int i = 0; i < Constant.QUEUE_SIZE - 1; i++)
		{
			exb.allPackets[i] = exb.allPackets[i + 1];
		}
		//slot cuoi cung trong bo dem cua EXB phai la null (khong chua goi tin nao)
		exb.allPackets[Constant.QUEUE_SIZE - 1] = null;
		
	}
}
