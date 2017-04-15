public class Client{
  private String name;
  private String surname;
  private Address address;
  private Double accountBalance;
  private Pesel pesel;
  private String accountNumber;
  
  	Client(	String name, 
  			String surname, 
  			Pesel pesel, 
  			Address address, 
  			Double accountBalance, 
  			String accountNumber){
		this.setName(name);
		this.setSurname(surname);
		this.setPesel(pesel);
		this.setAddress(address);
		this.setAccountBalance(accountBalance);
		this.setAccountNumber(accountNumber);
  	}
  	
  	Client(){
  	}
	
	public Pesel getPesel() {
		return pesel;
	}

	public void setPesel(Pesel pesel) {
		this.pesel = pesel;
	}
	
	public Double getAccountBalance() {
		return accountBalance;
	}
	
	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getData(){
		String result = new String( "Name: " + name + 
							"\nSurname: " + surname + 
							"\nPESEL: " + pesel.toString() +
							"\nAccount balance: " + accountBalance + 
							"\nAccount number: " + accountNumber);
		
		if (address instanceof Address){
			result += "\nAddress: " + address.getFullAddress();
		}
		
		return result;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
}