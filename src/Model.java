import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
				break;
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
			break;
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
	
	public void addNewClient(String [] sClient) throws Exception{
		if (sClient.length != 8){
			throw new Exception("Bad size of array in new client function!");
		}
		Client client = new Client(	sClient[0], 
									sClient[1], 
									sClient[2], 
									new Address(sClient[5],
												sClient[4],
												Integer.parseInt(sClient[6]),
												Integer.parseInt(sClient[7])),
									Double.parseDouble(sClient[3]),
									geneateAccountNumber(sClient[2]));
		
		clients.add(client);		
	}
	
	public void removeAccounts(List<Client> rClients){
		for (Client client : rClients){
			clients.remove(client);
		}
	}
	
	private String geneateAccountNumber(String Pesel){
		Random rand = new Random();
		return new String("	6788776655" + 
							Pesel + 
							(rand.nextInt(89999) + 10000) );
	}
	
	public void changeAccountBalance(Client client, double cash){
		client.setAccountBalance(client.getAccountBalance() + cash);
	}
	
	public void transfer(Client sender, Client receiver, double cash) throws Exception{
		if (sender.getAccountBalance() < cash){
			throw new Exception("Lack of account funds");
		}
		
		changeAccountBalance(sender, -cash);
		changeAccountBalance(receiver, cash);
	}
	
	public List<Client> getClients(){
		return clients;
	}
	
	public void saveToXML(){
		Document doc;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
	        doc = db.newDocument();
	        Element rootEle = doc.createElement("clients");
	        doc.appendChild(rootEle);
	        
	        for (Client client : clients){
	        	Element eClient = doc.createElement("client");
	        	eClient.setAttribute("id", client.getAccountNumber());
	        	//name
	        	Element element = doc.createElement("name");
	        	element.appendChild(doc.createTextNode(client.getName()));
	        	eClient.appendChild(element);
	        	//surname
	        	element = doc.createElement("surname");
	        	element.appendChild(doc.createTextNode(client.getSurname()));
	        	eClient.appendChild(element);
	        	//street
	        	element = doc.createElement("street");
	        	Address addr = client.getAddress();
	        	element.appendChild(doc.createTextNode(addr.getStreet()));
	        	eClient.appendChild(element);
	        	//city
	        	element = doc.createElement("city");
	        	element.appendChild(doc.createTextNode(addr.getCity()));
	        	eClient.appendChild(element);
	        	//postcode
	        	element = doc.createElement("postcode");
	        	element.appendChild(doc.createTextNode(addr.getPostalCode().toString()));
	        	eClient.appendChild(element);
	        	//house
	        	element = doc.createElement("house");
	        	element.appendChild(doc.createTextNode(addr.getHouseNomber().toString()));
	        	eClient.appendChild(element);
	        	//accountbalance
	        	element = doc.createElement("acountBalance");
	        	element.appendChild(doc.createTextNode(client.getAccountBalance().toString()));
	        	eClient.appendChild(element);
	        	//pesel
	        	element = doc.createElement("pesel");
	        	element.appendChild(doc.createTextNode(client.getPesel()));
	        	eClient.appendChild(element);
	        	
	        	rootEle.appendChild(eClient);
	        }
	        	
        	TransformerFactory transformerFactory = TransformerFactory.newInstance();
    		Transformer transformer = transformerFactory.newTransformer();
    		DOMSource source = new DOMSource(doc);
    		StreamResult result = new StreamResult(new File("db.xml"));
	    		
    		transformer.transform(source, result);
		}catch (ParserConfigurationException e){
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
}
