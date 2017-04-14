
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
			return false;
		case FINDCLIENT:
			findClient();
			break;
		case NEWCLIENT:
			break;
		case PAYMENT:
			break;
		case REMOVECLIENT:
			break;
		case TRANSFER:
			break;
		default:
			break;
		}
	
		return true;
	}
	
	private void findClient(){
		switch(view.showMenuFindClient()){
		case ADDRESS:
			view.showClients(Model.get().searchByAddress(view.searchByAddress()));
			break;
		case CANCEL:
			return;
		case CLIENTNUMBER:
			view.showClients(Model.get().searchByClientNumber(view.searchBy("client number")));
			break;
		case NAME:
			view.showClients(Model.get().searchByName(view.searchBy("name")));
			break;
		case PESEL:
			view.showClients(Model.get().searchByPesel(view.searchBy("PESEL")));
			break;
		case SURNAME:
			view.showClients(Model.get().searchBySurname(view.searchBy("surname")));
			break;
		default:
			break;
		}
	}	
}
