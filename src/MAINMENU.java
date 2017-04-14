
public enum MAINMENU {
	EXIT(0),
	NEWCLIENT(1),
	PAYMENT(2),
	REMOVECLIENT(3),
	SHOWALLCLIENTS(4),
	TRANSFER(5),
	FINDCLIENT(6);
	
	private final int value;
	MAINMENU(int value){
		this.value = value;
	}
	
	public int toInt(){
		return this.value;
	}
	
}
