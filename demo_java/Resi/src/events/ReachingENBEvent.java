package events;

enum TypeD
{
	D, D1, D2
}

public class ReachingENBEvent extends Event {
	public TypeD type = TypeD.D;
	//Event dai dien cho su kien loai (D): goi tin den duoc ENB cua nut tiep theo 
}
