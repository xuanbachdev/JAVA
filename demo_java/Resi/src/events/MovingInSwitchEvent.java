package events;

enum TypeE{
	E, E1, E2
}

public class MovingInSwitchEvent extends Event {
	public TypeE type = TypeE.E;
	//Event dai dien cho su kien loai (E): goi tin roi khoi ENB cua Switch de sang EXB
}
