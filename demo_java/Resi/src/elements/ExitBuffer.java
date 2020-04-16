package elements;

import states.exb.X00;
import states.exb.X01;

public class ExitBuffer extends LimitedBuffer {
	public ExitBuffer()
	{
		state = new X01();
	}
}
