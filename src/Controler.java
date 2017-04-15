import java.util.List;

public class Controler {
	View view;
	
	Controler(){
		view = new View();
	}
	
	public boolean run(){
		switch(view.showMainMenu()){
		case SHOWALLCLIENTS:
			showAllClients();
			break;
		case EXIT:
			return exit();
		case FINDCLIENT:
			findClient();
			break;
		case NEWCLIENT:
			newClient();
			break;
		case PAYMENT:
			try {
				payment();
			}catch(Exception e){
				view.showError(e.getMessage());
			}
			break;
		case REMOVECLIENT:
			removeClient();
			break;
		case TRANSFER:
			transfer();
			break;
		case PAYOFF:
			try {
				payOff();
			}catch(Exception e){
				view.showError(e.getMessage());
			}
			break;
		default:
			break;
		}
	
		return true;
	}
	
	private void showAllClients(){
		List<Client> clients = Model.get().getClients();
		if (!clients.isEmpty()){
			view.showClients(clients);
		} else {
			view.showError("Lista klientow jest pusta!");
		}
	}
	
	private void removeClient(){
		String parms = view.remove();
		
		if (!view.bSure()){
			return;
		}
		
		List<Client> clients = null;
		
		try {
			clients = Model.get().searchByPesel(new Pesel(parms));
			Model.get().removeAccounts(clients);
		} catch(PeselIsNotValidException e){
			view.showError("Pesel is not valid. " + e.getMessage());
		} catch (IllegalArgumentException e){
			view.showError("No clients to remove. " + e.getMessage());
		}
	}
	
	private void newClient(){
		String [] parms = view.addNewClient();
		
		if (!view.bSure()){
			return;
		}
		
		try{
			Model.get().addNewClient(parms);
		}catch(PeselIsNotValidException e){
			view.showError(e.getMessage());
		}catch(IllegalArgumentException e){
			view.showError(e.getMessage());
		}
	}
	
	private void transfer(){
		String [] parm = view.transfer();
		Client sender = null;
		try {
			sender = Model.get().searchByPesel(new Pesel(parm[0])).get(0);
		} catch (PeselIsNotValidException e1) {
			view.showError( "The sender's PESEL is not valid." + e1.getMessage());
			return;
		} catch (IllegalArgumentException e){
			view.showError(e.getMessage());
			return;
		}
		
		Client receiver = null;
		
		try {
			receiver = Model.get().searchByPesel(new Pesel(parm[1])).get(0);
		} catch (PeselIsNotValidException e1) {
			view.showError( "The receiver's PESEL is not valid." + e1.getMessage());
			return;
		} catch (IllegalArgumentException e){
			view.showError(e.getMessage());
			return;
		}
		
		if (!view.bSure()){
			return;
		}
		
		try {
		Model.get().transfer(sender, receiver, Double.parseDouble(parm[2]));
		}catch (Exception e){
			view.showError(e.getMessage());
		}
	}
	
	private void payOff() throws IllegalArgumentException{
		String[] param = view.payOff();
		Client client;
		try {
			client = Model.get().searchByPesel( new Pesel(param[0]) ).get(0);
		} catch (PeselIsNotValidException e) {
			view.showError( "The PESEL is not valid." + e.getMessage());
			return;
		}
		
		double cash = -Double.parseDouble(param[1]);
		if (cash > 0.0){
			throw new IllegalArgumentException("Bad value");
		}
		
		if (!view.bSure()){
			return;
		}
		
		Model.get().changeAccountBalance(client, cash);
	}
	
	private void payment() throws IllegalArgumentException{
		String[] param = view.payment();
		Client client = null;
		try {
			client = Model.get().searchByPesel( new Pesel(param[0]) ).get(0);
		}catch(PeselIsNotValidException e){
			view.showError("Pesel is not Valid. " + e.getMessage() );
			return;
		} catch (Exception e){
			view.showError("Other: " + e.getMessage() );
			return;
		}
		
		double cash = Double.parseDouble(param[1]);
		
		if (cash < 0.0){
			throw new IllegalArgumentException("Bad value");
		}
		
		if (!view.bSure()){
			return;
		}
		
		Model.get().changeAccountBalance(client, cash);
	}
	
	private void findClient(){
		switch(view.showMenuFindClient()){
		case ADDRESS:
			findClientByAddress();
			break;
		case CANCEL:
			return;
		case CLIENTNUMBER:
			findClientByClientNumber();
			break;
		case NAME:
			findClientByName();
			break;
		case PESEL:
			showClientsByPesel();
			break;
		case SURNAME:
			findClientBySurname();
			break;
		default:
			break;
		}
	}	
	
	private void findClientByAddress(){
		String [] parms = view.searchByAddress();
		List<Client> clients;
		try{
			clients = Model.get().searchByAddress(parms);
		} catch( IllegalArgumentException e ){
			view.showError(e.getMessage());
			return;
		}
		
		view.showClients(clients);
	}
	
	private void findClientByClientNumber(){
		String parms = view.enter("client number");
		List<Client> clients;
		try {
			clients = Model.get().searchByClientNumber(parms);
		} catch(IllegalArgumentException e){
			view.showError(e.getMessage());
			return;
		}
		
		view.showClients(clients);
	}
	
	private void findClientByName(){
		String parms = view.enter("name");
		List<Client> clients;
		try {
			clients = Model.get().searchByName(parms);
		} catch (IllegalArgumentException e){
			view.showError(e.getMessage());
			return;
		}
		view.showClients(clients);
	}
	
	private void findClientBySurname(){
		String parms = view.enter("surname");
		List<Client> clients;
		try{
			clients = Model.get().searchBySurname(parms);
		} catch(IllegalArgumentException e){
			view.showError(e.getMessage());
			return;
		}
		
		view.showClients(clients);
	}
	
	
	private void showClientsByPesel(){
		try {
			view.showClients(Model.get().searchByPesel(new Pesel(view.enter("PESEL"))));
		} catch (PeselIsNotValidException e) {
			view.showError( "The PESEL is not valid." + e.getMessage());
		} catch (IllegalArgumentException e){
			view.showError(e.getMessage());
		} catch (Exception e){
			view.showError(e.getMessage());
		}
	}
	
	private boolean exit(){
		if (!view.bSure()){
			return true;
		}
		
		Model.get().saveToXML();
		
		return false;
	}
}
