
public enum MENUFINDCLIENT {
	CANCEL(0),
	NAME(1),
	SURNAME(2),
	ADDRESS(3),
	CLIENTNUMBER(4),
	PESEL(5);
	
	private final int value;
	MENUFINDCLIENT(int value){
		this.value = value;
	}
	
	public int toInt(){
		return this.value;
	}
}
