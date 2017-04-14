import java.util.List;
import java.util.Scanner;

public class View {
	Scanner sc = new Scanner(System.in);
	
	View(){
		
	}
	
	public MAINMENU showMainMenu(){
		System.out.println("Menu: ");
		System.out.println("[1] Add new client");
		System.out.println("[2] Payment");
		System.out.println("[3] Remove client");
		System.out.println("[4] Show all clients");
		System.out.println("[5] Transfer");
		System.out.println("[6] Find client");
		System.out.println("[0] Exit\n");
		
		System.out.print("Select: ");
		
		return MAINMENU.values()[getInt()];
	}
	
	public MENUFINDCLIENT showMenuFindClient(){
		System.out.println("Select search category");
		System.out.println("[1] Name");
		System.out.println("[2] Surname");
		System.out.println("[3] Address");
		System.out.println("[4] Client number");
		System.out.println("[5] PESEL");
		System.out.println("[0] Cancel");
		return MENUFINDCLIENT.values()[getInt()];
	}
	
	public String searchBy(String by){
		System.out.print("Enter searching " + by + ": ");
		return getString();
	}
	
	public String[] searchByAddress(){
		String [] result = new String[4];
		
		System.out.println("Searching by Address. If you want skip parameters you should write '0'.");
		System.out.print("City: ");
		result[0] = getString();
		System.out.print("Street: ");
		result[1] = getString();
		System.out.print("PostCode: ");
		result[2] = getString();
		System.out.print("Number of house: ");
		result[3] = getString();
		
		return result;
	}
	
	public void showClients(List<Client> clients){
		for (Client client : clients){
			System.out.println(client.getData());
			System.out.println("-------------------------------\n");
		}
	}
	
	private int getInt(){
		boolean done = true;
		int result = 0;
		do {
			done = true;
			try {
				result = sc.nextInt();
			} catch(Exception e){
				done = false;
				sc.nextLine();
			}
		
		} while (!done);
		
		return result;
	}
	
	private String getString(){
		String result = new String();
		while((result = sc.nextLine()).isEmpty());
		return result;
	}
}
