
public enum MAINMENU {
	EXIT(0),
	NEWCLIENT(1),
	PAYMENT(2),
	PAYOFF(3),
	REMOVECLIENT(4),
	SHOWALLCLIENTS(5),
	TRANSFER(6),
	FINDCLIENT(7);
	
	private final int value;
	MAINMENU(int value){
		this.value = value;
	}
	
	public int toInt(){
		return this.value;
	}
	
}
