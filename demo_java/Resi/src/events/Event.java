package events;

import elements.*;

public abstract class Event {
	public long pid; //packet ID
	public long startTime;
	public long endTime;
	
	public Element elem;
	
	public void execute()
	{}
}
