package elements;

import events.Event;
import states.State;

public abstract class Element {
	public int id;
	public State state;
	public long soonestEndTime = 0;
	
	public void getNextState()
	{
		
	}
	
	public void updateSoonestEndTime()
	{
		
	}
	
	public void removeExecutedEvent(Event ev)
	{
		
	}
}
