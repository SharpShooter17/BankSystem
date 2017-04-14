import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Model {
	
	private static Model model;

	List<Client> clients; 
	
	private Model() {
		try {
			loadClients();
		} catch (ParserConfigurationException e) {
			System.err.println("Can not load clients data base - Parser Configuration exception: " + e.getMessage());
		} catch (SAXException e){
			System.err.println("Can not load clients data base - SAX exception: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Can not load clients data base - IO exception: " + e.getMessage());
		} catch(Exception e){
			System.err.println("Can not load clients data base: " + e.getMessage());
		}
	}
	
	//Load clients from xml file
	private void loadClients() throws Exception{
		clients = new ArrayList<Client>();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document document = builder.parse(ClassLoader.getSystemResourceAsStream("db.xml"));
		
		 NodeList nodeList = document.getDocumentElement().getChildNodes();
		 
		 for (int i = 0; i < nodeList.getLength(); i++){
			 Node node = nodeList.item(i);
			 
			 if ( node instanceof Element ){
				 Client client = new Client();
				 Address address = new Address();
				 client.setAccountNumber(node.getAttributes().getNamedItem("id").getNodeValue());
				 NodeList childNodes = node.getChildNodes();
				 
				 for(int j = 0; j < childNodes.getLength(); j++){
					 Node cNode = childNodes.item(j);
					 if (cNode instanceof Element){
						 String content = cNode.getLastChild().getTextContent().trim();
						 switch(cNode.getNodeName()){
						 case "name":
							 client.setName(content);
							 break;
						 case "surname": 
							 client.setSurname(content);
							 break;
						 case "accountBalance": 
							 client.setAccountBalance(Double.parseDouble(content));
							 break;
						 case "pesel": 
							 client.setPesel(content);
							 break;
						 case "city": 
							 address.setCity(content);						
							 break;
						 case "street": 
							 address.setStreet(content);						
							 break;
						 case "house": 
							 address.setHouseNomber(Integer.parseInt(content));						
							 break;
						 case "postcode": 
							 address.setPostalCode(Integer.parseInt(content));						
							 break;
						 default:
							 throw new Exception("Bad data:");
						 }
					 }
				 }
				 client.setAddress(address);
				 clients.add(client);
			 }
		 }
	}
	
	public static Model get(){
		if ( !(model instanceof Model) ){
			model = new Model();
		}
		
		return model;
	}
	
	public List<Client> searchByName(String name){
		List<Client> result = new ArrayList<Client>();
		
		for (Client client : this.clients){
			if (client.getName().equals(name)){
				result.add(client);
			}
		}
		
		return result;
	}
	
	public List<Client> searchBySurname(String surname){
		List<Client> result = new ArrayList<Client>();
		
		for (Client client : this.clients){
			if (client.getSurname().equals(surname)){
				result.add(client);
			}
		}
		
		return result;
	}
	
	public List<Client> searchByPesel(String pesel){
		List<Client> result = new ArrayList<Client>();
		
		for (Client client : this.clients){
			if (client.getPesel().equals(pesel)){
				result.add(client);
			}
		}
		
		return result;
	}
	
	public List<Client> searchByClientNumber(String clientNumber){
		List<Client> result = new ArrayList<Client>();
		
		for (Client client : this.clients){
			if (client.getAccountNumber().equals(clientNumber)){
				result.add(client);
			}
		}
		
		return result;
	}
	
	public List<Client> searchByAddress(String[] address){
		List<Client> result = new ArrayList<Client>();

		int postCode = Integer.parseInt(address[2]);
		int house = Integer.parseInt(address[3]);
		
		Address pattern = new Address(address[1], address[0], postCode, house);
		
		for (Client client : this.clients){
			Address addr = client.getAddress();
			
			if (addr.compareSelectedFields(pattern)){
				result.add(client);
			}
		}
		
		return result;
	}
	
	public List<Client> getClients(){
		return clients;
	}
}
