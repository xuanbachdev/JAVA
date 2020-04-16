package elements;

import config.Constant;
import network.Packet;
import states.State;

public abstract class LimitedBuffer extends Buffer {
	public Packet[] allPackets;
	
	public int indexOfEmpty()
	{
		boolean found = false;
		int i;
		for(i = 0; i < Constant.QUEUE_SIZE && !found; i++)
		{
			if(allPackets[i] == null)
			{
				found = true;
			}
		}
		if(found) return i;
		return (Constant.QUEUE_SIZE + 1);
	}
	public LimitedBuffer()
	{
		allPackets = new Packet[Constant.QUEUE_SIZE];
	}
	
	/**
	 * Phuong thuc insertPacket se lam nhiem vu chen goi tin p 
	 * vao trong bo dem cua no
	 * @param p la goi tin can chen vao
	 * @return true neu nhu chen duoc goi tin
	 *         false neu nhu KHONG chen duoc goi tin vao (tuc bo dem da day)
	 */
	public boolean insertPacket(Packet p)
	{
		boolean found = false;
		for(int i = 0; i < Constant.QUEUE_SIZE && !found; i++)
		{
			if(allPackets[i] == null)
			{
				allPackets[i] = p;
				found = true;
			}
		}
		return found;
	}
}
