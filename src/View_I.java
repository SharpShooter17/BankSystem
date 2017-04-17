import java.util.List;

public interface View_I {
	public int showMainMenu();
	public int showMenuFindClient();
	public String ask(String what);
	public void message(String msg);
	public String [] payment();
	public String [] payOff();
	public String [] transfer();
	public String [] addNewClient();
	public String [] searchByAddress();
	public void showClients(List<Client> clients);
	public void showError(String error);
	public boolean bSure();
	public boolean bSure(String msg);
}
