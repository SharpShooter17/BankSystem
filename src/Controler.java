
public class Controler {
	View view;
	
	Controler(){
		view = new View();
	}
	
	public boolean run(){
		switch(view.showMainMenu()){
		case SHOWALLCLIENTS:
			view.showClients(Model.get().getClients());
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
	
	private void removeClient(){
		String parms = view.remove();
		
		if (!view.bSure()){
			return;
		}
		
		Model.get().removeAccounts(Model.get().searchByPesel(parms));
	}
	
	private void newClient(){
		String [] parms = view.addNewClient();
		
		if (!view.bSure()){
			return;
		}
		
		try{
			Model.get().addNewClient(parms);
		}catch(Exception e){
			view.showError(e.getMessage());
		}		
	}
	
	private void transfer(){
		String [] parm = view.transfer();
		Client sender = Model.get().searchByPesel(parm[0]).get(0);
		Client receiver = Model.get().searchByPesel(parm[1]).get(0);
		
		if (!view.bSure()){
			return;
		}
		
		try {
		Model.get().transfer(sender, receiver, Double.parseDouble(parm[2]));
		}catch (Exception e){
			view.showError(e.getMessage());
		}
	}
	
	private void payOff() throws Exception{
		String[] param = view.payOff();
		Client client = Model.get().searchByPesel( param[0] ).get(0);
		double cash = -Double.parseDouble(param[1]);
		if (cash > 0.0){
			throw new Exception("Bad value");
		}
		
		if (!view.bSure()){
			return;
		}
		
		Model.get().changeAccountBalance(client, cash);
	}
	
	private void payment() throws Exception{
		String[] param = view.payment();
		Client client = Model.get().searchByPesel( param[0] ).get(0);
		double cash = Double.parseDouble(param[1]);
		if (cash < 0.0){
			throw new Exception("Bad value");
		}
		
		if (!view.bSure()){
			return;
		}
		
		Model.get().changeAccountBalance(client, cash);
	}
	
	private void findClient(){
		switch(view.showMenuFindClient()){
		case ADDRESS:
			view.showClients(Model.get().searchByAddress(view.searchByAddress()));
			break;
		case CANCEL:
			return;
		case CLIENTNUMBER:
			view.showClients(Model.get().searchByClientNumber(view.enter("client number")));
			break;
		case NAME:
			view.showClients(Model.get().searchByName(view.enter("name")));
			break;
		case PESEL:
			view.showClients(Model.get().searchByPesel(view.enter("PESEL")));
			break;
		case SURNAME:
			view.showClients(Model.get().searchBySurname(view.enter("surname")));
			break;
		default:
			break;
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
